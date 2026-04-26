<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增用户
          </el-button>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户名">
          <el-input v-model="searchForm.loginname" placeholder="请输入用户名" clearable />
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
        <el-table-column prop="loginname" label="用户名" width="150" />
        <el-table-column prop="username" label="姓名" width="120" />
        <el-table-column prop="ouname" label="所属组织" width="150" />
        <el-table-column prop="userrole" label="角色" min-width="200" show-overflow-tooltip />
        <el-table-column prop="mobile" label="手机号" width="130" />
        <el-table-column prop="email" label="邮箱" width="200" show-overflow-tooltip />
        <el-table-column prop="addtime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.addtime) }}
          </template>
        </el-table-column>
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
            <el-form-item label="用户名" prop="loginname">
              <el-input v-model="form.loginname" placeholder="请输入用户名" :disabled="isEdit" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="username">
              <el-input v-model="form.username" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20" v-if="!isEdit">
          <el-col :span="12">
            <el-form-item label="密码" prop="password">
              <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="form.confirmPassword" type="password" placeholder="请再次输入密码" show-password />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="手机号" prop="mobile">
              <el-input v-model="form.mobile" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="所属组织" prop="ouguid">
          <el-select v-model="form.ouguid" placeholder="请选择组织" style="width: 100%">
            <el-option 
              v-for="org in orgList" 
              :key="org.rowguid" 
              :label="org.ouname" 
              :value="org.rowguid" 
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="角色" prop="userrole">
          <el-select v-model="form.userrole" placeholder="请选择角色" style="width: 100%" multiple>
            <el-option 
              v-for="role in roleList" 
              :key="role.rowguid" 
              :label="role.rolename" 
              :value="role.rowguid" 
            />
          </el-select>
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
  loginname: ''
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
  loginname: '',
  username: '',
  password: '',
  confirmPassword: '',
  mobile: '',
  email: '',
  ouguid: '',
  userrole: []
})

// 组织和角色列表
const orgList = ref([])
const roleList = ref([])

// 表单验证规则
const rules = {
  loginname: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  username: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于 6 位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== form.password) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  mobile: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  ouguid: [
    { required: true, message: '请选择所属组织', trigger: 'change' }
  ]
}

// 格式化日期时间
const formatDateTime = (datetime) => {
  if (!datetime) return '-'
  const date = new Date(datetime)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

// 加载用户列表
const loadUserList = async () => {
  loading.value = true
  try {
    const response = await request.post('/user/getpagelist', {
      loginname: searchForm.loginname,
      pagenum: pagination.page,
      pagesize: pagination.pageSize
    })
    
    if (response.data.code === '0000') {
      // 后端返回格式：{ code: '0000', data: [...], totalcount: N }
      tableData.value = response.data.data || []
      pagination.total = response.data.totalcount || 0
    } else {
      ElMessage.error(response.data.msg || response.data.message || '获取用户列表失败')
    }
  } catch (error) {
    console.error('加载用户列表失败:', error)
    ElMessage.error('加载用户列表失败')
  } finally {
    loading.value = false
  }
}

// 加载组织列表
const loadOrgList = async () => {
  try {
    const response = await request.post('/ouinfo/getouinfolist')
    if (response.data.code === '0000') {
      orgList.value = response.data.data || []
    }
  } catch (error) {
    console.error('加载组织列表失败:', error)
  }
}

// 加载角色列表
const loadRoleList = async () => {
  try {
    const response = await request.post('/role/getrolelist')
    if (response.data.code === '0000') {
      roleList.value = response.data.data || []
    }
  } catch (error) {
    console.error('加载角色列表失败:', error)
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  loadUserList()
}

// 重置
const handleReset = () => {
  searchForm.loginname = ''
  pagination.page = 1
  loadUserList()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增用户'
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑用户'
  isEdit.value = true
  
  // 填充表单数据
  Object.keys(form).forEach(key => {
    if (key === 'userrole') {
      // 角色字段需要特殊处理（分号分隔的字符串转数组）
      form[key] = row[key] ? row[key].split(';').filter(r => r) : []
    } else if (key !== 'password' && key !== 'confirmPassword') {
      form[key] = row[key] || ''
    }
  })
  
  dialogVisible.value = true
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除用户 "${row.username}" 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const response = await request.post('/user/deluser', {
        rowguid: row.rowguid
      })
      
      if (response.data.code === '0000') {
        ElMessage.success('删除成功')
        loadUserList()
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
      const data = { ...form }
      
      // 角色数组转为分号分隔的字符串
      if (Array.isArray(data.userrole)) {
        data.userrole = data.userrole.join(';')
      }
      
      let response
      if (isEdit.value) {
        // 编辑
        response = await request.post('/user/edituser', data)
      } else {
        // 新增
        response = await request.post('/user/adduser', data)
      }
      
      if (response.data.code === '0000') {
        ElMessage.success(isEdit.value ? '编辑成功' : '新增成功')
        dialogVisible.value = false
        loadUserList()
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
    if (key === 'userrole') {
      form[key] = []
    } else {
      form[key] = ''
    }
  })
}

// 分页大小改变
const handleSizeChange = (val) => {
  pagination.pageSize = val
  pagination.page = 1
  loadUserList()
}

// 页码改变
const handlePageChange = (val) => {
  pagination.page = val
  loadUserList()
}

// 初始化
onMounted(() => {
  loadUserList()
  loadOrgList()
  loadRoleList()
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
