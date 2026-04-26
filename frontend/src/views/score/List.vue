<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>成绩管理</span>
          <div>
            <el-button type="success" @click="handleExport">
              <el-icon><Download /></el-icon>
              导出成绩
            </el-button>
            <el-button type="primary" @click="handleInput">
              <el-icon><Upload /></el-icon>
              录入成绩
            </el-button>
          </div>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="学生姓名">
          <el-input v-model="searchForm.studentName" placeholder="请输入学生姓名" clearable />
        </el-form-item>
        <el-form-item label="班级">
          <el-select v-model="searchForm.className" placeholder="请选择班级" clearable style="width: 150px">
            <el-option label="一年级1班" value="class1" />
            <el-option label="一年级2班" value="class2" />
            <el-option label="二年级1班" value="class3" />
          </el-select>
        </el-form-item>
        <el-form-item label="考试科目">
          <el-select v-model="searchForm.subject" placeholder="请选择科目" clearable style="width: 150px">
            <el-option label="语文" value="chinese" />
            <el-option label="数学" value="math" />
            <el-option label="英语" value="english" />
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
        <el-table-column prop="studentName" label="学生姓名" width="120" />
        <el-table-column prop="className" label="班级" width="150" />
        <el-table-column prop="studentNo" label="学号" width="150" />
        <el-table-column prop="chinese" label="语文" width="100">
          <template #default="{ row }">
            <el-tag :type="getScoreType(row.chinese)">{{ row.chinese }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="math" label="数学" width="100">
          <template #default="{ row }">
            <el-tag :type="getScoreType(row.math)">{{ row.math }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="english" label="英语" width="100">
          <template #default="{ row }">
            <el-tag :type="getScoreType(row.english)">{{ row.english }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="total" label="总分" width="100">
          <template #default="{ row }">
            <strong>{{ row.total }}</strong>
          </template>
        </el-table-column>
        <el-table-column prop="average" label="平均分" width="100">
          <template #default="{ row }">
            {{ row.average.toFixed(1) }}
          </template>
        </el-table-column>
        <el-table-column prop="rank" label="排名" width="80" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button link type="info" size="small" @click="handleDetail(row)">
              详情
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
    
    <!-- 成绩录入对话框 -->
    <el-dialog
      v-model="inputDialogVisible"
      title="录入成绩"
      width="700px"
    >
      <el-alert
        title="提示"
        type="info"
        description="此功能需要后端 API 支持，当前为演示版本。"
        :closable="false"
        style="margin-bottom: 20px"
      />
      
      <el-form :model="inputForm" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="学生姓名">
              <el-input v-model="inputForm.studentName" placeholder="请输入学生姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="班级">
              <el-select v-model="inputForm.className" placeholder="请选择班级" style="width: 100%">
                <el-option label="一年级1班" value="class1" />
                <el-option label="一年级2班" value="class2" />
                <el-option label="二年级1班" value="class3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="语文成绩">
              <el-input-number v-model="inputForm.chinese" :min="0" :max="100" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="数学成绩">
              <el-input-number v-model="inputForm.math" :min="0" :max="100" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="英语成绩">
              <el-input-number v-model="inputForm.english" :min="0" :max="100" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      
      <template #footer>
        <el-button @click="inputDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleInputSubmit">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const inputDialogVisible = ref(false)

// 搜索表单
const searchForm = reactive({
  studentName: '',
  className: '',
  subject: ''
})

// 分页
const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

// 表格数据（模拟数据）
const tableData = ref([
  { id: 1, studentName: '张三', className: '一年级1班', studentNo: '2024001', chinese: 92, math: 88, english: 95, total: 275, average: 91.67, rank: 1 },
  { id: 2, studentName: '李四', className: '一年级1班', studentNo: '2024002', chinese: 85, math: 92, english: 88, total: 265, average: 88.33, rank: 2 },
  { id: 3, studentName: '王五', className: '一年级2班', studentNo: '2024003', chinese: 78, math: 85, english: 82, total: 245, average: 81.67, rank: 3 },
  { id: 4, studentName: '赵六', className: '一年级2班', studentNo: '2024004', chinese: 95, math: 90, english: 92, total: 277, average: 92.33, rank: 4 },
  { id: 5, studentName: '孙七', className: '二年级1班', studentNo: '2024005', chinese: 88, math: 95, english: 90, total: 273, average: 91.00, rank: 5 }
])

// 录入表单
const inputForm = reactive({
  studentName: '',
  className: '',
  chinese: 0,
  math: 0,
  english: 0
})

// 获取分数类型
const getScoreType = (score) => {
  if (score >= 90) return 'success'
  if (score >= 80) return 'primary'
  if (score >= 60) return 'warning'
  return 'danger'
}

// 加载列表
const loadList = () => {
  loading.value = true
  setTimeout(() => {
    // 模拟数据加载
    pagination.total = tableData.value.length
    loading.value = false
  }, 500)
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  loadList()
  ElMessage.info('搜索功能已触发')
}

// 重置
const handleReset = () => {
  searchForm.studentName = ''
  searchForm.className = ''
  searchForm.subject = ''
  pagination.page = 1
  loadList()
}

// 录入成绩
const handleInput = () => {
  inputDialogVisible.value = true
}

// 提交录入
const handleInputSubmit = () => {
  ElMessage.success('成绩录入成功（演示）')
  inputDialogVisible.value = false
}

// 编辑
const handleEdit = (row) => {
  ElMessage.info(`编辑 ${row.studentName} 的成绩（演示）`)
}

// 查看详情
const handleDetail = (row) => {
  ElMessage.info(`查看 ${row.studentName} 的成绩详情（演示）`)
}

// 导出
const handleExport = () => {
  ElMessage.success('成绩导出功能已触发（演示）')
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
