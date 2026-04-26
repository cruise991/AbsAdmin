<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>文件管理</span>
          <el-upload
            :action="uploadUrl"
            :headers="uploadHeaders"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :before-upload="beforeUpload"
            :limit="1"
            accept="*/*"
          >
            <el-button type="primary">
              <el-icon><Upload /></el-icon>
              上传文件
            </el-button>
          </el-upload>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="文件名">
          <el-input v-model="searchForm.filename" placeholder="请输入文件名" clearable />
        </el-form-item>
        <el-form-item label="文件类型">
          <el-select v-model="searchForm.filetype" placeholder="请选择类型" clearable style="width: 150px">
            <el-option label="图片" value=".jpg" />
            <el-option label="图片" value=".png" />
            <el-option label="PDF" value=".pdf" />
            <el-option label="Word" value=".doc" />
            <el-option label="Word" value=".docx" />
            <el-option label="Excel" value=".xls" />
            <el-option label="Excel" value=".xlsx" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      
      <!-- 数据表格 -->
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="rowguid" label="ID" width="100" show-overflow-tooltip />
        <el-table-column prop="filename" label="文件名" min-width="200" show-overflow-tooltip />
        <el-table-column prop="filetype" label="类型" width="100">
          <template #default="{ row }">
            <el-tag size="small">{{ row.filetype || '-' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="filesize" label="大小" width="120">
          <template #default="{ row }">
            {{ formatFileSize(row.filesize) }}
          </template>
        </el-table-column>
        <el-table-column prop="userguid" label="上传人" width="120" />
        <el-table-column prop="addtime" label="上传时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.addtime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleDownload(row)">
              下载
            </el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
        class="pagination"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/api/request'

const loading = ref(false)
const dialogVisible = ref(false)

// 上传配置
const uploadUrl = computed(() => {
  return '/api/absfile/addfiletolocal'
})

const uploadHeaders = computed(() => {
  const token = localStorage.getItem('token')
  return token ? { 'Authorization': token } : {}
})

// 搜索表单
const searchForm = reactive({
  filename: '',
  filetype: ''
})

// 分页
const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

// 表格数据
const tableData = ref([])

// 格式化日期时间
const formatDateTime = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  const seconds = String(date.getSeconds()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}

// 格式化文件大小
const formatFileSize = (bytes) => {
  if (!bytes || bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return (bytes / Math.pow(k, i)).toFixed(2) + ' ' + sizes[i]
}

// 加载文件列表
const loadFileList = async () => {
  loading.value = true
  try {
    const res = await request.post('/absfile/getpicfilelist', {
      ...searchForm,
      page: pagination.page,
      limit: pagination.pageSize
    })
    
    if (res.code === '0000' || res.code === 200) {
      tableData.value = res.data || []
      pagination.total = res.totalcount || 0
    }
  } catch (error) {
    console.error('加载文件列表失败:', error)
    ElMessage.error('加载文件列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  loadFileList()
}

// 重置
const handleReset = () => {
  searchForm.filename = ''
  searchForm.filetype = ''
  pagination.page = 1
  loadFileList()
}

// 上传前验证
const beforeUpload = (file) => {
  const maxSize = 100 * 1024 * 1024 // 100MB
  if (file.size > maxSize) {
    ElMessage.error('文件大小不能超过 100MB')
    return false
  }
  return true
}

// 上传成功
const handleUploadSuccess = (response) => {
  if (response.code === '0000' || response.code === 200) {
    ElMessage.success('文件上传成功')
    loadFileList()
  } else {
    ElMessage.error(response.msg || '文件上传失败')
  }
}

// 上传失败
const handleUploadError = (error) => {
  console.error('文件上传失败:', error)
  ElMessage.error('文件上传失败，请重试')
}

// 下载文件
const handleDownload = (row) => {
  if (!row.fileurl) {
    ElMessage.warning('文件URL不存在')
    return
  }
  
  // 如果是完整URL，直接打开
  if (row.fileurl.startsWith('http')) {
    window.open(row.fileurl, '_blank')
  } else {
    // 否则拼接API路径
    const downloadUrl = `/api/absfile/download?fileurl=${encodeURIComponent(row.fileurl)}`
    window.open(downloadUrl, '_blank')
  }
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除文件《${row.filename}》吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const res = await request.post('/absfile/deletebyguid', {
        rowguid: row.rowguid
      })
      
      if (res.code === '0000' || res.code === 200) {
        ElMessage.success('删除成功')
        loadFileList()
      }
    } catch (error) {
      console.error('删除文件失败:', error)
      ElMessage.error('删除文件失败')
    }
  }).catch(() => {
    // 用户取消
  })
}

// 分页大小变化
const handleSizeChange = (val) => {
  pagination.pageSize = val
  pagination.page = 1
  loadFileList()
}

// 页码变化
const handlePageChange = (val) => {
  pagination.page = val
  loadFileList()
}

// 组件挂载时加载数据
onMounted(() => {
  loadFileList()
})
</script>

<style scoped>
.page-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
