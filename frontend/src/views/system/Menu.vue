<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>菜单权限管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增菜单
          </el-button>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="菜单名称">
          <el-input v-model="searchForm.viewname" placeholder="请输入菜单名称" clearable />
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
        <el-table-column prop="viewname" label="菜单名称" width="200" />
        <el-table-column prop="viewcode" label="菜单编码" width="150" />
        <el-table-column prop="viewurl" label="菜单路径" min-width="250" show-overflow-tooltip />
        <el-table-column prop="vieworder" label="排序" width="80" />
        <el-table-column prop="isshow" label="是否显示" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isshow === '1' ? 'success' : 'info'">
              {{ row.isshow === '1' ? '显示' : '隐藏' }}
            </el-tag>
          </template>
        </el-table-column>
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
      width="700px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="菜单名称" prop="viewname">
              <el-input v-model="form.viewname" placeholder="请输入菜单名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单编码" prop="viewcode">
              <el-input v-model="form.viewcode" placeholder="请输入菜单编码" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="菜单路径" prop="viewurl">
          <el-input v-model="form.viewurl" placeholder="请输入菜单路径，例如：/system/users" />
        </el-form-item>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="图标" prop="icon">
              <el-input v-model="form.icon" placeholder="请输入图标类名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序" prop="vieworder">
              <el-input-number v-model="form.vieworder" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="是否显示" prop="isshow">
          <el-radio-group v-model="form.isshow">
            <el-radio label="1">显示</el-radio>
            <el-radio label="0">隐藏</el-radio>
          </el-radio-group>
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
  viewname: ''
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
  viewname: '',
  viewcode: '',
  viewurl: '',
  icon: '',
  vieworder: 0,
  isshow: '1',
  remark: ''
})

// 表单验证规则
const rules = {
  viewname: [
    { required: true, message: '请输入菜单名称', trigger: 'blur' }
  ],
  viewcode: [
    { required: true, message: '请输入菜单编码', trigger: 'blur' }
  ]
}

// 加载列表
const loadList = async () => {
  loading.value = true
  try {
    const response = await request.post('/view/getpagelist', {
      viewname: searchForm.viewname,
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
  searchForm.viewname = ''
  pagination.page = 1
  loadList()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增菜单'
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑菜单'
  isEdit.value = true
  
  Object.keys(form).forEach(key => {
    form[key] = row[key] !== undefined ? row[key] : ''
  })
  
  dialogVisible.value = true
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除菜单 "${row.viewname}" 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const response = await request.post('/view/delview', {
        rowguid: row.rowguid
      })
      
      if (response.code === '0000') {
        ElMessage.success('删除成功')
        loadList()
      } else {
        ElMessage.error(response.message || '删除失败')
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
        response = await request.post('/view/saveview', form)
      } else {
        response = await request.post('/view/addview', form)
      }
      
      if (response.code === '0000') {
        ElMessage.success(isEdit.value ? '编辑成功' : '新增成功')
        dialogVisible.value = false
        loadList()
      } else {
        ElMessage.error(response.message || '操作失败')
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
  form.rowguid = ''
  form.viewname = ''
  form.viewcode = ''
  form.viewurl = ''
  form.icon = ''
  form.vieworder = 0
  form.isshow = '1'
  form.remark = ''
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
