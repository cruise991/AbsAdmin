<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>流程定义管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增流程
          </el-button>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="流程名称">
          <el-input v-model="searchForm.flowname" placeholder="请输入流程名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 120px">
            <el-option label="启用" value="1" />
            <el-option label="停用" value="0" />
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
        <el-table-column prop="flowname" label="流程名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="flowtype" label="流程类型" width="150" />
        <el-table-column prop="typename" label="分类" width="150" />
        <el-table-column prop="startname" label="发起人角色" width="150" />
        <el-table-column prop="addtime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.addtime) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === '1' ? 'success' : 'info'">
              {{ row.status === '1' ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button 
              v-if="row.status === '0'"
              link 
              type="success" 
              size="small" 
              @click="handleToggleStatus(row, '1')"
            >
              启用
            </el-button>
            <el-button 
              v-if="row.status === '1'"
              link 
              type="warning" 
              size="small" 
              @click="handleToggleStatus(row, '0')"
            >
              停用
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
      width="700px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="流程名称" prop="flowname">
          <el-input v-model="form.flowname" placeholder="请输入流程名称" />
        </el-form-item>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="流程类型" prop="flowtype">
              <el-input v-model="form.flowtype" placeholder="请输入流程类型" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分类" prop="typename">
              <el-input v-model="form.typename" placeholder="请输入分类" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="发起人角色" prop="startrole">
              <el-input v-model="form.startrole" placeholder="请输入发起人角色" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发起人名称" prop="startname">
              <el-input v-model="form.startname" placeholder="请输入发起人名称" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="组类型" prop="grouptype">
              <el-input v-model="form.grouptype" placeholder="请输入组类型" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="组名称" prop="groupname">
              <el-input v-model="form.groupname" placeholder="请输入组名称" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="描述" prop="description">
          <el-input 
            v-model="form.description" 
            type="textarea" 
            :rows="4" 
            placeholder="请输入流程描述" 
          />
        </el-form-item>
        
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio value="1">启用</el-radio>
            <el-radio value="0">停用</el-radio>
          </el-radio-group>
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
  flowname: '',
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
  flowname: '',
  flowtype: '',
  typename: '',
  startrole: '',
  startname: '',
  grouptype: '',
  groupname: '',
  description: '',
  status: '1'
})

// 表单验证规则
const rules = {
  flowname: [
    { required: true, message: '请输入流程名称', trigger: 'blur' },
    { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
  ],
  flowtype: [
    { required: true, message: '请输入流程类型', trigger: 'blur' }
  ],
  typename: [
    { required: true, message: '请输入分类', trigger: 'blur' }
  ]
}

// 格式化日期时间
const formatDateTime = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

// 加载流程列表
const loadFlowList = async () => {
  loading.value = true
  try {
    const res = await request.post('/flowinfo/getflowlist', {
      ...searchForm,
      page: pagination.page,
      limit: pagination.pageSize
    })
    
    if (res.code === '0000' || res.code === 200) {
      tableData.value = res.data || []
      pagination.total = res.count || 0
    }
  } catch (error) {
    console.error('加载流程列表失败:', error)
    ElMessage.error('加载流程列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  loadFlowList()
}

// 重置
const handleReset = () => {
  searchForm.flowname = ''
  searchForm.status = ''
  pagination.page = 1
  loadFlowList()
}

// 新增
const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增流程定义'
  resetForm()
  dialogVisible.value = true
}

// 编辑
const handleEdit = async (row) => {
  try {
    const res = await request.post('/flowinfo/getflowbyguid', {
      rowguid: row.rowguid
    })
    
    if (res.code === '0000' || res.code === 200) {
      isEdit.value = true
      dialogTitle.value = '编辑流程定义'
      Object.assign(form, {
        rowguid: res.rowguid || res.data?.rowguid,
        flowname: res.flowname || res.data?.flowname || '',
        flowtype: res.flowtype || res.data?.flowtype || '',
        typename: res.typename || res.data?.typename || '',
        startrole: res.startrole || res.data?.startrole || '',
        startname: res.startname || res.data?.startname || '',
        grouptype: res.grouptype || res.data?.grouptype || '',
        groupname: res.groupname || res.data?.groupname || '',
        description: res.description || res.data?.description || '',
        status: res.status || res.data?.status || '1'
      })
      dialogVisible.value = true
    }
  } catch (error) {
    console.error('加载流程详情失败:', error)
    ElMessage.error('加载流程详情失败')
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitLoading.value = true
    try {
      const res = await request.post('/flowinfo/saveflow', {
        rowguid: isEdit.value ? form.rowguid : '',
        flowname: form.flowname,
        flowtype: form.flowtype,
        typename: form.typename,
        startrole: form.startrole,
        startname: form.startname,
        grouptype: form.grouptype,
        groupname: form.groupname,
        description: form.description,
        status: form.status
      })
      
      if (res.code === '0000' || res.code === 200) {
        ElMessage.success(isEdit.value ? '更新成功' : '新增成功')
        dialogVisible.value = false
        loadFlowList()
      }
    } catch (error) {
      console.error('保存流程失败:', error)
      ElMessage.error('保存流程失败')
    } finally {
      submitLoading.value = false
    }
  })
}

// 切换状态
const handleToggleStatus = async (row, newStatus) => {
  const statusText = newStatus === '1' ? '启用' : '停用'
  try {
    const res = await request.post('/flowinfo/triggerflow', {
      rowguid: row.rowguid,
      status: newStatus
    })
    
    if (res.code === '0000' || res.code === 200) {
      ElMessage.success(`${statusText}成功`)
      loadFlowList()
    }
  } catch (error) {
    console.error(`${statusText}失败:`, error)
    ElMessage.error(`${statusText}失败`)
  }
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除流程《${row.flowname}》吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const res = await request.post('/flowinfo/delflow', {
        rowguid: row.rowguid
      })
      
      if (res.code === '0000' || res.code === 200) {
        ElMessage.success('删除成功')
        loadFlowList()
      }
    } catch (error) {
      console.error('删除流程失败:', error)
      ElMessage.error('删除流程失败')
    }
  }).catch(() => {
    // 用户取消
  })
}

// 重置表单
const resetForm = () => {
  form.rowguid = ''
  form.flowname = ''
  form.flowtype = ''
  form.typename = ''
  form.startrole = ''
  form.startname = ''
  form.grouptype = ''
  form.groupname = ''
  form.description = ''
  form.status = '1'
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

// 分页大小变化
const handleSizeChange = (val) => {
  pagination.pageSize = val
  pagination.page = 1
  loadFlowList()
}

// 页码变化
const handlePageChange = (val) => {
  pagination.page = val
  loadFlowList()
}

// 组件挂载时加载数据
onMounted(() => {
  loadFlowList()
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
