<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>流程实例管理</span>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="流程名称">
          <el-input v-model="searchForm.flowname" placeholder="请输入流程名称" clearable />
        </el-form-item>
        <el-form-item label="发起人">
          <el-input v-model="searchForm.startname" placeholder="请输入发起人" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 120px">
            <el-option label="待处理" value="0" />
            <el-option label="处理中" value="1" />
            <el-option label="已完成" value="2" />
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
        <el-table-column prop="startname" label="发起人" width="120" />
        <el-table-column prop="pronodename" label="当前节点" width="150" />
        <el-table-column prop="startdate" label="发起时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.startdate) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusName(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleViewDetail(row)">
              查看详情
            </el-button>
            <el-button 
              v-if="row.status === '0' || row.status === '1'"
              link 
              type="success" 
              size="small" 
              @click="handleNextStep(row)"
            >
              下一步
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
    
    <!-- 详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="流程实例详情"
      width="800px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="流程名称">
          {{ currentInstance.flowname }}
        </el-descriptions-item>
        <el-descriptions-item label="流程类型">
          {{ currentInstance.flowtype }}
        </el-descriptions-item>
        <el-descriptions-item label="发起人">
          {{ currentInstance.startname }}
        </el-descriptions-item>
        <el-descriptions-item label="发起时间">
          {{ formatDateTime(currentInstance.startdate) }}
        </el-descriptions-item>
        <el-descriptions-item label="当前节点">
          {{ currentInstance.pronodename }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(currentInstance.status)">
            {{ getStatusName(currentInstance.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="组类型" :span="2">
          {{ currentInstance.grouptype }}
        </el-descriptions-item>
        <el-descriptions-item label="组名称" :span="2">
          {{ currentInstance.groupname }}
        </el-descriptions-item>
      </el-descriptions>
      
      <el-divider content-position="left">审批历史</el-divider>
      
      <el-timeline v-if="flowHistory.length > 0">
        <el-timeline-item
          v-for="(item, index) in flowHistory"
          :key="index"
          :timestamp="formatDateTime(item.addtime)"
          placement="top"
        >
          <el-card>
            <h4>{{ item.nodename }}</h4>
            <p>顺序号：{{ item.ordernum }}</p>
            <p>状态：{{ item.isend === '1' ? '已完成' : '待处理' }}</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
      
      <el-empty v-else description="暂无审批历史" />
      
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/api/request'

const loading = ref(false)
const detailDialogVisible = ref(false)

// 搜索表单
const searchForm = reactive({
  flowname: '',
  startname: '',
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

// 当前实例详情
const currentInstance = reactive({
  rowguid: '',
  flowname: '',
  flowtype: '',
  startname: '',
  startdate: '',
  pronodename: '',
  status: '',
  grouptype: '',
  groupname: ''
})

// 流程历史
const flowHistory = ref([])

// 格式化日期时间
const formatDateTime = (dateStr) => {
  if (!dateStr) return '-'
  // 如果已经是字符串格式，直接返回
  if (typeof dateStr === 'string' && dateStr.includes('-')) {
    return dateStr
  }
  const date = new Date(dateStr)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  const seconds = String(date.getSeconds()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}

// 获取状态名称
const getStatusName = (status) => {
  const map = {
    '0': '待处理',
    '1': '处理中',
    '2': '已完成'
  }
  return map[status] || '-'
}

// 获取状态标签类型
const getStatusType = (status) => {
  const map = {
    '0': 'warning',
    '1': 'primary',
    '2': 'success'
  }
  return map[status] || 'info'
}

// 加载流程实例列表
const loadInstanceList = async () => {
  loading.value = true
  try {
    const res = await request.post('/flowinfo/getuserfloweventlist', {
      ...searchForm,
      page: pagination.page,
      limit: pagination.pageSize
    })
    
    if (res.code === '0000' || res.code === 200) {
      tableData.value = res.data || []
      pagination.total = res.count || 0
    }
  } catch (error) {
    console.error('加载流程实例列表失败:', error)
    ElMessage.error('加载流程实例列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  loadInstanceList()
}

// 重置
const handleReset = () => {
  searchForm.flowname = ''
  searchForm.startname = ''
  searchForm.status = ''
  pagination.page = 1
  loadInstanceList()
}

// 查看详情
const handleViewDetail = async (row) => {
  try {
    // 加载流程步骤信息
    const res = await request.post('/flowinfo/queryflowstep', {
      eventguid: row.rowguid
    })
    
    if (res.code === '0000' || res.code === 200) {
      // 设置当前实例信息
      Object.assign(currentInstance, {
        rowguid: row.rowguid,
        flowname: row.flowname,
        flowtype: row.flowtype,
        startname: row.startname,
        startdate: row.startdate,
        pronodename: row.pronodename,
        status: row.status,
        grouptype: row.grouptype,
        groupname: row.groupname
      })
      
      // 设置流程历史
      flowHistory.value = res.flowlist || []
      
      detailDialogVisible.value = true
    }
  } catch (error) {
    console.error('加载流程详情失败:', error)
    ElMessage.error('加载流程详情失败')
  }
}

// 下一步
const handleNextStep = async (row) => {
  try {
    const res = await request.post('/flowinfo/nextstep', {
      eventguid: row.rowguid
    })
    
    if (res.code === '0000' || res.code === 200) {
      ElMessage.success('流程推进成功')
      loadInstanceList()
    }
  } catch (error) {
    console.error('流程推进失败:', error)
    ElMessage.error('流程推进失败')
  }
}

// 分页大小变化
const handleSizeChange = (val) => {
  pagination.pageSize = val
  pagination.page = 1
  loadInstanceList()
}

// 页码变化
const handlePageChange = (val) => {
  pagination.page = val
  loadInstanceList()
}

// 组件挂载时加载数据
onMounted(() => {
  loadInstanceList()
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
