<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>组织架构管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增组织
          </el-button>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="组织名称">
          <el-input v-model="searchForm.ouname" placeholder="请输入组织名称" clearable />
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
      <el-table :data="tableData" border stripe v-loading="loading" row-key="rowguid">
        <el-table-column prop="rowguid" label="ID" width="100" show-overflow-tooltip />
        <el-table-column prop="oucode" label="组织编码" width="150" />
        <el-table-column prop="ouname" label="组织名称" width="200" />
        <el-table-column prop="ouaddresstel" label="联系电话" width="150" />
        <el-table-column prop="oubankaccount" label="银行账号" width="200" show-overflow-tooltip />
        <el-table-column prop="remark" label="备注" min-width="200" show-overflow-tooltip />
        <el-table-column prop="addtime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEdit(row)">
              编辑
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
      width="600px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="组织编码" prop="oucode">
          <el-input v-model="form.oucode" placeholder="请输入组织编码" />
        </el-form-item>
        
        <el-form-item label="组织名称" prop="ouname">
          <el-input v-model="form.ouname" placeholder="请输入组织名称" />
        </el-form-item>
        
        <el-form-item label="联系电话" prop="ouaddresstel">
          <el-input v-model="form.ouaddresstel" placeholder="请输入联系电话" />
        </el-form-item>
        
        <el-form-item label="银行账号" prop="oubankaccount">
          <el-input v-model="form.oubankaccount" placeholder="请输入银行账号" />
        </el-form-item>
        
        <el-form-item label="备注" prop="remark">
          <el-input 
            v-model="form.remark" 
            type="textarea" 
            :rows="3" 
            placeholder="请输入备注信息" 
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
  ouname: ''
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
  oucode: '',
  ouname: '',
  ouaddresstel: '',
  oubankaccount: '',
  remark: ''
})

// 表单验证规则
const rules = {
  oucode: [
    { required: true, message: '请输入组织编码', trigger: 'blur' }
  ],
  ouname: [
    { required: true, message: '请输入组织名称', trigger: 'blur' }
  ]
}

// 加载列表
const loadList = async () => {
  loading.value = true
  try {
    const response = await request.post('/ouinfo/getpagelist', {
      ouname: searchForm.ouname,
      pagenum: pagination.page,
      pagesize: pagination.pageSize
    })
    
    if (response.data.code === '0000') {
      tableData.value = response.data.data.list || []
      pagination.total = response.data.data.count || 0
    } else {
      ElMessage.error(response.data.message || '获取列表失败')
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
  searchForm.ouname = ''
  pagination.page = 1
  loadList()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增组织'
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑组织'
  isEdit.value = true
  
  Object.keys(form).forEach(key => {
    form[key] = row[key] || ''
  })
  
  dialogVisible.value = true
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除组织 "${row.ouname}" 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const response = await request.post('/ouinfo/delbyguid', {
        rowguid: row.rowguid
      })
      
      if (response.data.code === '0000') {
        ElMessage.success('删除成功')
        loadList()
      } else {
        ElMessage.error(response.data.message || '删除失败')
      }
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
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
      let response
      if (isEdit.value) {
        response = await request.post('/ouinfo/editouinfo', form)
      } else {
        response = await request.post('/ouinfo/addouinfo', form)
      }
      
      if (response.data.code === '0000') {
        ElMessage.success(isEdit.value ? '编辑成功' : '新增成功')
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

// 重置表单
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  Object.keys(form).forEach(key => {
    form[key] = ''
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
