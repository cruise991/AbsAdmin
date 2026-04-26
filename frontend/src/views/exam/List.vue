<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>考试管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增考试链接
          </el-button>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="链接地址">
          <el-input v-model="searchForm.siteurl" placeholder="请输入链接地址" clearable />
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
        <el-table-column prop="siteurl" label="链接地址" min-width="400" show-overflow-tooltip>
          <template #default="{ row }">
            <el-link :href="row.siteurl" target="_blank" type="primary">
              {{ row.siteurl }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="tsstatus" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.tsstatus === '0' ? 'success' : 'info'">
              {{ row.tsstatus === '0' ? '未推送' : '已推送' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="addtime" label="创建时间" width="180" />
        <el-table-column prop="tsdate" label="推送时间" width="180" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handlePush(row)">
              推送到百度
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
    
    <!-- 新增对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="新增考试链接"
      width="600px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="链接地址" prop="siteurl">
          <el-input 
            v-model="form.siteurl" 
            placeholder="请输入完整的URL地址，例如：https://www.example.com/exam/123" 
          />
        </el-form-item>
        
        <el-alert
          title="提示"
          type="info"
          description="添加的链接将被推送到百度搜索引擎，以便更好地被索引和收录。"
          :closable="false"
          style="margin-top: 10px"
        />
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/api/request'

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const formRef = ref(null)

// 搜索表单
const searchForm = reactive({
  siteurl: ''
})

// 分页
const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

// 表格数据
const tableData = ref([])

// 表单数据
const form = reactive({
  siteurl: ''
})

// 表单验证规则
const rules = {
  siteurl: [
    { required: true, message: '请输入链接地址', trigger: 'blur' },
    { 
      pattern: /^https?:\/\/.+/, 
      message: '请输入有效的URL地址（以http://或https://开头）', 
      trigger: 'blur' 
    }
  ]
}

// 加载列表
const loadList = async () => {
  loading.value = true
  try {
    const response = await request.post('/tsinfo/getpagelist', {
      siteurl: searchForm.siteurl,
      pagenum: pagination.page,
      pagesize: pagination.pageSize
    })
    
    if (response.code === '0000') {
      // 后端返回格式：{ code: '0000', data: [...], totalcount: N }
      tableData.value = response.data || []
      pagination.total = response.totalcount || 0
    } else {
      ElMessage.error(response.msg || response.message || '获取列表失败')
    }
  } catch (error) {
    console.error('加载列表失败:', error)
    ElMessage.error('加载列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  loadList()
}

// 重置
const handleReset = () => {
  searchForm.siteurl = ''
  pagination.page = 1
  loadList()
}

// 新增
const handleAdd = () => {
  resetForm()
  dialogVisible.value = true
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除链接 "${row.siteurl}" 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const response = await request.post('/tsinfo/delbyguid', {
        rowguid: row.rowguid
      })
      
      if (response.code === '0000') {
        ElMessage.success('删除成功')
        loadList()
      } else {
        ElMessage.error(response.msg || response.message || '删除失败')
      }
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 推送到百度
const handlePush = (row) => {
  ElMessageBox.confirm(
    `确定要将链接 "${row.siteurl}" 推送到百度吗？`,
    '确认推送',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info'
    }
  ).then(async () => {
    try {
      const response = await request.post('/tsinfo/tstobaidu', {
        rowguid: row.rowguid
      })
      
      if (response.code === '0000') {
        ElMessage.success('推送成功')
        loadList()
      } else {
        ElMessage.error(response.msg || response.message || '推送失败')
      }
    } catch (error) {
      console.error('推送失败:', error)
      ElMessage.error('推送失败')
    }
  }).catch(() => {})
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitLoading.value = true
    try {
      const response = await request.post('/tsinfo/addsiteurl', form)
      
      if (response.code === '0000') {
        ElMessage.success('新增成功')
        dialogVisible.value = false
        loadList()
      } else {
        ElMessage.error(response.msg || response.message || '操作失败')
      }
    } catch (error) {
      console.error('提交失败:', error)
      ElMessage.error('操作失败')
    } finally {
      submitLoading.value = false
    }
  })
}

// 重置表单
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  form.siteurl = ''
}

// 分页大小改变
const handleSizeChange = (val) => {
  pagination.pageSize = val
  pagination.page = 1
  loadList()
}

// 页码改变
const handlePageChange = (val) => {
  pagination.page = val
  loadList()
}

// 初始化
onMounted(() => {
  loadList()
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
  justify-content: flex-end;
}
</style>
