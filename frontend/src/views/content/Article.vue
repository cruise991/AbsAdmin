<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>文章管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增文章
          </el-button>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="标题">
          <el-input v-model="searchForm.title" placeholder="请输入标题" clearable />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="searchForm.btype" placeholder="请选择分类" clearable style="width: 150px">
            <el-option label="教育资讯" value="education" />
            <el-option label="通知公告" value="notice" />
            <el-option label="学术动态" value="academic" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 120px">
            <el-option label="草稿" value="1" />
            <el-option label="待审核" value="2" />
            <el-option label="审核不通过" value="3" />
            <el-option label="审核通过" value="4" />
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
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="authorName" label="作者" width="120" />
        <el-table-column prop="btype" label="分类" width="120">
          <template #default="{ row }">
            {{ getCategoryName(row.btype) }}
          </template>
        </el-table-column>
        <el-table-column prop="addtime" label="发布时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.addtime) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusName(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button 
              v-if="row.status === '1' || row.status === '3'"
              link 
              type="success" 
              size="small" 
              @click="handlePublish(row)"
            >
              发布
            </el-button>
            <el-button 
              v-if="row.status === '4'"
              link 
              type="warning" 
              size="small" 
              @click="handleUnpublish(row)"
            >
              取消发布
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
    
    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="900px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="16">
            <el-form-item label="标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入文章标题" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="分类" prop="btype">
              <el-select v-model="form.btype" placeholder="请选择分类" style="width: 100%">
                <el-option label="教育资讯" value="education" />
                <el-option label="通知公告" value="notice" />
                <el-option label="学术动态" value="academic" />
                <el-option label="其他" value="other" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="内容" prop="content">
          <el-input 
            v-model="form.content" 
            type="textarea" 
            :rows="15" 
            placeholder="请输入文章内容（支持HTML格式）" 
          />
        </el-form-item>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="封面图片URL" prop="fmurl">
              <el-input v-model="form.fmurl" placeholder="请输入封面图片URL" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="草稿" value="1" />
                <el-option label="待审核" value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
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
const dialogTitle = ref('')
const formRef = ref(null)
const isEdit = ref(false)

// 搜索表单
const searchForm = reactive({
  title: '',
  btype: '',
  status: ''
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
  rowguid: '',
  title: '',
  content: '',
  btype: '',
  fmurl: '',
  status: '1'
})

// 表单验证规则
const rules = {
  title: [
    { required: true, message: '请输入文章标题', trigger: 'blur' },
    { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入文章内容', trigger: 'blur' }
  ],
  btype: [
    { required: true, message: '请选择分类', trigger: 'change' }
  ]
}

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

// 获取分类名称
const getCategoryName = (btype) => {
  const map = {
    'education': '教育资讯',
    'notice': '通知公告',
    'academic': '学术动态',
    'other': '其他'
  }
  return map[btype] || btype || '-'
}

// 获取状态名称
const getStatusName = (status) => {
  const map = {
    '0': '初始化',
    '1': '草稿',
    '2': '待审核',
    '3': '审核不通过',
    '4': '审核通过'
  }
  return map[status] || '-'
}

// 获取状态标签类型
const getStatusType = (status) => {
  const map = {
    '0': 'info',
    '1': 'info',
    '2': 'warning',
    '3': 'danger',
    '4': 'success'
  }
  return map[status] || 'info'
}

// 加载文章列表
const loadArticleList = async () => {
  loading.value = true
  try {
    const res = await request.post('/article/getarticlelist', {
      ...searchForm,
      page: pagination.page,
      limit: pagination.pageSize
    })
    
    if (res.code === '0000' || res.code === 200) {
      tableData.value = res.data || []
      pagination.total = res.totalcount || 0
    }
  } catch (error) {
    console.error('加载文章列表失败:', error)
    ElMessage.error('加载文章列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  loadArticleList()
}

// 重置
const handleReset = () => {
  searchForm.title = ''
  searchForm.btype = ''
  searchForm.status = ''
  pagination.page = 1
  loadArticleList()
}

// 新增
const handleAdd = async () => {
  try {
    // 先初始化文章
    const res = await request.post('/article/articleadd_init', {})
    if (res.code === '0000' || res.code === 200) {
      isEdit.value = false
      dialogTitle.value = '新增文章'
      resetForm()
      form.rowguid = res.rowguid
      dialogVisible.value = true
    }
  } catch (error) {
    console.error('初始化文章失败:', error)
    ElMessage.error('初始化文章失败')
  }
}

// 编辑
const handleEdit = async (row) => {
  try {
    const res = await request.post('/article/view_init', {
      articleguid: row.rowguid
    })
    
    if (res.code === '0000' || res.code === 200) {
      isEdit.value = true
      dialogTitle.value = '编辑文章'
      Object.assign(form, {
        rowguid: res.rowguid,
        title: res.title || '',
        content: res.content || '',
        btype: res.btype || '',
        fmurl: res.fmurl || '',
        status: res.status || '1'
      })
      dialogVisible.value = true
    }
  } catch (error) {
    console.error('加载文章详情失败:', error)
    ElMessage.error('加载文章详情失败')
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitLoading.value = true
    try {
      const res = await request.post('/article/savearticle', {
        rowguid: form.rowguid,
        title: form.title,
        content: form.content,
        btype: form.btype,
        fmurl: form.fmurl,
        status: form.status
      })
      
      if (res.code === '0000' || res.code === 200) {
        ElMessage.success(isEdit.value ? '更新成功' : '新增成功')
        dialogVisible.value = false
        loadArticleList()
      }
    } catch (error) {
      console.error('保存文章失败:', error)
      ElMessage.error('保存文章失败')
    } finally {
      submitLoading.value = false
    }
  })
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除文章《${row.title}》吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const res = await request.post('/article/delbyguid', {
        rowguids: row.rowguid
      })
      
      if (res.code === '0000' || res.code === 200) {
        ElMessage.success('删除成功')
        loadArticleList()
      }
    } catch (error) {
      console.error('删除文章失败:', error)
      ElMessage.error('删除文章失败')
    }
  }).catch(() => {
    // 用户取消
  })
}

// 发布
const handlePublish = async (row) => {
  try {
    const res = await request.post('/article/shenhepass', {
      rowguid: row.rowguid
    })
    
    if (res.code === '0000' || res.code === 200) {
      ElMessage.success('发布成功')
      loadArticleList()
    }
  } catch (error) {
    console.error('发布失败:', error)
    ElMessage.error('发布失败')
  }
}

// 取消发布
const handleUnpublish = async (row) => {
  try {
    const res = await request.post('/article/shenhenopass', {
      rowguid: row.rowguid
    })
    
    if (res.code === '0000' || res.code === 200) {
      ElMessage.success('已取消发布')
      loadArticleList()
    }
  } catch (error) {
    console.error('取消发布失败:', error)
    ElMessage.error('取消发布失败')
  }
}

// 重置表单
const resetForm = () => {
  form.rowguid = ''
  form.title = ''
  form.content = ''
  form.btype = ''
  form.fmurl = ''
  form.status = '1'
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

// 分页大小变化
const handleSizeChange = (val) => {
  pagination.pageSize = val
  pagination.page = 1
  loadArticleList()
}

// 页码变化
const handlePageChange = (val) => {
  pagination.page = val
  loadArticleList()
}

// 组件挂载时加载数据
onMounted(() => {
  loadArticleList()
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
