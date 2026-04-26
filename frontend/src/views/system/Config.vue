<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>系统配置管理</span>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="配置名称">
          <el-input v-model="searchForm.configname" placeholder="请输入配置名称" clearable />
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
        <el-table-column prop="configname" label="配置名称" width="200" />
        <el-table-column prop="configkey" label="配置键" width="200" />
        <el-table-column prop="configvalue" label="配置值" min-width="250" show-overflow-tooltip />
        <el-table-column prop="configdesc" label="配置说明" min-width="200" show-overflow-tooltip />
        <el-table-column prop="addtime" label="创建时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEdit(row)">
              编辑
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
    
    <!-- 编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="编辑配置"
      width="600px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="配置名称" prop="configname">
          <el-input v-model="form.configname" disabled />
        </el-form-item>
        
        <el-form-item label="配置键" prop="configkey">
          <el-input v-model="form.configkey" disabled />
        </el-form-item>
        
        <el-form-item label="配置值" prop="configvalue">
          <el-input v-model="form.configvalue" placeholder="请输入配置值" />
        </el-form-item>
        
        <el-form-item label="配置说明" prop="configdesc">
          <el-input 
            v-model="form.configdesc" 
            type="textarea" 
            :rows="3" 
            placeholder="请输入配置说明" 
          />
        </el-form-item>
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
import { ElMessage } from 'element-plus'
import request from '@/api/request'

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const formRef = ref(null)

// 搜索表单
const searchForm = reactive({
  configname: ''
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
  configname: '',
  configkey: '',
  configvalue: '',
  configdesc: ''
})

// 表单验证规则
const rules = {
  configvalue: [
    { required: true, message: '请输入配置值', trigger: 'blur' }
  ]
}

// 加载列表
const loadList = async () => {
  loading.value = true
  try {
    const response = await request.post('/sysconfig/getglobalpagelist', {
      configname: searchForm.configname,
      pagenum: pagination.page,
      pagesize: pagination.pageSize
    })
    
    if (response.data.code === '0000') {
      // 后端返回格式：{ code: '0000', data: [...], totalcount: N }
      tableData.value = response.data.data || []
      pagination.total = response.data.totalcount || 0
    } else {
      ElMessage.error(response.data.msg || response.data.message || '获取列表失败')
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
  searchForm.configname = ''
  pagination.page = 1
  loadList()
}

// 编辑
const handleEdit = (row) => {
  Object.keys(form).forEach(key => {
    form[key] = row[key] || ''
  })
  
  dialogVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitLoading.value = true
    try {
      const response = await request.post('/sysconfig/updateconfig', form)
      
      if (response.data.code === '0000') {
        ElMessage.success('更新成功')
        dialogVisible.value = false
        loadList()
      } else {
        ElMessage.error(response.data.message || '操作失败')
      }
    } catch (error) {
      console.error('提交失败:', error)
      ElMessage.error('操作失败')
    } finally {
      submitLoading.value = false
    }
  })
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
