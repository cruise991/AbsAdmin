<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>学生管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增学生
          </el-button>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="姓名">
          <el-input v-model="searchForm.name" placeholder="请输入姓名" clearable />
        </el-form-item>
        <el-form-item label="班级">
          <el-select v-model="searchForm.className" placeholder="请选择班级" clearable>
            <el-option label="一年级1班" value="class1" />
            <el-option label="一年级2班" value="class2" />
            <el-option label="二年级1班" value="class3" />
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
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="{ row }">
            {{ row.gender === 'male' ? '男' : '女' }}
          </template>
        </el-table-column>
        <el-table-column prop="age" label="年龄" width="80" />
        <el-table-column prop="className" label="班级" width="150" />
        <el-table-column prop="studentNo" label="学号" width="150" />
        <el-table-column prop="phone" label="联系电话" width="130" />
        <el-table-column prop="address" label="家庭住址" min-width="200" />
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
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="form.gender">
                <el-radio label="male">男</el-radio>
                <el-radio label="female">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="年龄" prop="age">
              <el-input-number v-model="form.age" :min="6" :max="18" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学号" prop="studentNo">
              <el-input v-model="form.studentNo" placeholder="请输入学号" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="班级" prop="className">
          <el-select v-model="form.className" placeholder="请选择班级" style="width: 100%">
            <el-option label="一年级1班" value="class1" />
            <el-option label="一年级2班" value="class2" />
            <el-option label="二年级1班" value="class3" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        
        <el-form-item label="家庭住址" prop="address">
          <el-input v-model="form.address" type="textarea" :rows="3" placeholder="请输入家庭住址" />
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

// 搜索表单
const searchForm = reactive({
  name: '',
  className: ''
})

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
    name: '张三',
    gender: 'male',
    age: 15,
    className: '一年级1班',
    studentNo: '2024001',
    phone: '13800138000',
    address: '北京市朝阳区xxx街道xxx号'
  },
  {
    id: 2,
    name: '李四',
    gender: 'female',
    age: 14,
    className: '一年级2班',
    studentNo: '2024002',
    phone: '13800138001',
    address: '北京市海淀区xxx街道xxx号'
  }
])

// 表单数据
const form = reactive({
  id: null,
  name: '',
  gender: 'male',
  age: 15,
  className: '',
  studentNo: '',
  phone: '',
  address: ''
})

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  age: [
    { required: true, message: '请输入年龄', trigger: 'blur' }
  ],
  className: [
    { required: true, message: '请选择班级', trigger: 'change' }
  ],
  studentNo: [
    { required: true, message: '请输入学号', trigger: 'blur' }
  ]
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  loadData()
}

// 重置
const handleReset = () => {
  searchForm.name = ''
  searchForm.className = ''
  handleSearch()
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
  dialogTitle.value = '新增学生'
  resetForm()
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑学生'
  Object.assign(form, row)
  dialogVisible.value = true
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该学生吗？', '提示', {
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
  form.name = ''
  form.gender = 'male'
  form.age = 15
  form.className = ''
  form.studentNo = ''
  form.phone = ''
  form.address = ''
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

.search-form {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  justify-content: flex-end;
}
</style>
