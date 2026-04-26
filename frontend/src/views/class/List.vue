<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>班级管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增班级
          </el-button>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="班级名称">
          <el-input v-model="searchForm.className" placeholder="请输入班级名称" clearable />
        </el-form-item>
        <el-form-item label="年级">
          <el-select v-model="searchForm.grade" placeholder="请选择年级" clearable>
            <el-option label="一年级" value="1" />
            <el-option label="二年级" value="2" />
            <el-option label="三年级" value="3" />
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
        <el-table-column prop="class_name" label="班级名称" width="150" />
        <el-table-column prop="grade" label="年级" width="100">
          <template #default="{ row }">
            {{ getGradeName(row.grade) }}
          </template>
        </el-table-column>
        <el-table-column prop="enrollment_year" label="入学年份" width="120" />
        <el-table-column prop="teacher_name" label="班主任" width="120" />
        <el-table-column prop="student_count" label="学生人数" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="200" show-overflow-tooltip />
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
        <el-form-item label="班级名称" prop="class_name">
          <el-input v-model="form.class_name" placeholder="请输入班级名称" />
        </el-form-item>
        <el-form-item label="年级" prop="grade">
          <el-select v-model="form.grade" placeholder="请选择年级" style="width: 100%">
            <el-option label="一年级" value="1" />
            <el-option label="二年级" value="2" />
            <el-option label="三年级" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="入学年份" prop="enrollment_year">
          <el-input-number v-model="form.enrollment_year" :min="2000" :max="2030" style="width: 100%" />
        </el-form-item>
        <el-form-item label="班主任" prop="teacher_name">
          <el-input v-model="form.teacher_name" placeholder="请输入班主任姓名" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
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
import axios from 'axios'

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)

// 搜索表单
const searchForm = reactive({
  className: '',
  grade: ''
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
  rowguid: null,
  class_name: '',
  grade: '',
  enrollment_year: new Date().getFullYear(),
  teacher_name: '',
  status: 1,
  remark: ''
})

// 表单验证规则
const rules = {
  class_name: [
    { required: true, message: '请输入班级名称', trigger: 'blur' }
  ],
  grade: [
    { required: true, message: '请选择年级', trigger: 'change' }
  ],
  enrollment_year: [
    { required: true, message: '请输入入学年份', trigger: 'blur' }
  ]
}

// 获取年级名称
const getGradeName = (grade) => {
  const names = { '1': '一年级', '2': '二年级', '3': '三年级' }
  return names[grade] || grade
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  loadData()
}

// 重置
const handleReset = () => {
  searchForm.className = ''
  searchForm.grade = ''
  handleSearch()
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const response = await axios.post('/class/getpagelist', {
      className: searchForm.className,
      grade: searchForm.grade,
      pagenum: pagination.page,
      pagesize: pagination.pageSize
    })
    
    if (response.data && response.data.code === 200) {
      tableData.value = response.data.data.list || []
      pagination.total = response.data.data.count || 0
    }
  } catch (error) {
    console.error('加载班级数据失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增班级'
  resetForm()
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑班级'
  Object.assign(form, row)
  dialogVisible.value = true
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该班级吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      // TODO: 调用删除 API
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 提交
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        const url = form.rowguid ? '/class/updateclass' : '/class/addclass'
        const response = await axios.post(url, form)
        
        if (response.data && response.data.code === 200) {
          ElMessage.success('保存成功')
          dialogVisible.value = false
          loadData()
        } else {
          ElMessage.error(response.data.msg || '保存失败')
        }
      } catch (error) {
        ElMessage.error('保存失败')
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 重置表单
const resetForm = () => {
  form.rowguid = null
  form.class_name = ''
  form.grade = ''
  form.enrollment_year = new Date().getFullYear()
  form.teacher_name = ''
  form.status = 1
  form.remark = ''
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
