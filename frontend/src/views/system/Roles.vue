<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>角色管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增角色
          </el-button>
        </div>
      </template>
      
      <!-- 数据表格 -->
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="roleName" label="角色名称" width="150" />
        <el-table-column prop="roleCode" label="角色编码" width="150" />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
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
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="form.roleCode" placeholder="请输入角色编码" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入描述" />
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

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)

// 分页
const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

// 表格数据
const tableData = ref([
  {
    id: 1,
    roleName: '管理员',
    roleCode: 'admin',
    description: '系统管理员，拥有所有权限',
    createTime: '2024-01-01 10:00:00'
  },
  {
    id: 2,
    roleName: '教师',
    roleCode: 'teacher',
    description: '教师角色，可以管理学生和成绩',
    createTime: '2024-01-02 10:00:00'
  },
  {
    id: 3,
    roleName: '学生',
    roleCode: 'student',
    description: '学生角色，可以查看自己的信息',
    createTime: '2024-01-03 10:00:00'
  }
])

// 表单数据
const form = reactive({
  id: null,
  roleName: '',
  roleCode: '',
  description: ''
})

// 表单验证规则
const rules = {
  roleName: [
    { required: true, message: '请输入角色名称', trigger: 'blur' }
  ],
  roleCode: [
    { required: true, message: '请输入角色编码', trigger: 'blur' }
  ]
}

// 加载数据
const loadData = () => {
  loading.value = true
  // TODO: 调用 API 获取数据
  setTimeout(() => {
    loading.value = false
  }, 500)
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增角色'
  resetForm()
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑角色'
  Object.assign(form, row)
  dialogVisible.value = true
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该角色吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // TODO: 调用 API 删除
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

// 提交
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate((valid) => {
    if (valid) {
      submitLoading.value = true
      // TODO: 调用 API 保存
      setTimeout(() => {
        submitLoading.value = false
        dialogVisible.value = false
        ElMessage.success('保存成功')
        loadData()
      }, 500)
    }
  })
}

// 重置表单
const resetForm = () => {
  form.id = null
  form.roleName = ''
  form.roleCode = ''
  form.description = ''
}

// 分页大小改变
const handleSizeChange = (val) => {
  pagination.pageSize = val
  loadData()
}

// 页码改变
const handlePageChange = (val) => {
  pagination.page = val
  loadData()
}

onMounted(() => {
  loadData()
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

.pagination {
  margin-top: 20px;
  justify-content: flex-end;
}
</style>
