<template>
  <div class="dashboard-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon student">
              <el-icon :size="40"><Avatar /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.studentCount }}</div>
              <div class="stat-label">学生总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon teacher">
              <el-icon :size="40"><UserFilled /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.teacherCount }}</div>
              <div class="stat-label">教师总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon class">
              <el-icon :size="40"><School /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.classCount }}</div>
              <div class="stat-label">班级数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon exam">
              <el-icon :size="40"><Document /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.examCount }}</div>
              <div class="stat-label">考试场次</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="16">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>成绩趋势分析</span>
            </div>
          </template>
          <div ref="scoreChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
      
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>近期活动</span>
            </div>
          </template>
          <el-timeline class="activity-timeline">
            <el-timeline-item
              v-for="(activity, index) in activities"
              :key="index"
              :timestamp="activity.time"
              placement="top"
            >
              <p>{{ activity.content }}</p>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- AI 助手快捷入口 -->
    <el-row :gutter="20" class="ai-row">
      <el-col :span="24">
        <el-card shadow="hover" class="ai-card">
          <div class="ai-content">
            <div class="ai-icon">
              <el-icon :size="50"><ChatDotRound /></el-icon>
            </div>
            <div class="ai-info">
              <h3>AI 智能助手</h3>
              <p>基于 DeepSeek 大模型，为您提供智能问答、数据分析、内容生成等服务</p>
              <el-button type="primary" @click="goToChat">
                <el-icon><Promotion /></el-icon>
                立即体验
              </el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts'

const router = useRouter()

// 统计数据
const stats = ref({
  studentCount: 1256,
  teacherCount: 89,
  classCount: 45,
  examCount: 128
})

// 近期活动
const activities = ref([
  { content: '新增学生档案 23 条', time: '2024-01-15 14:30' },
  { content: '期末考试安排已发布', time: '2024-01-15 10:00' },
  { content: '教师培训会议通知', time: '2024-01-14 16:20' },
  { content: '系统数据备份完成', time: '2024-01-14 02:00' }
])

// 图表引用
const scoreChartRef = ref(null)

// 初始化成绩趋势图
const initScoreChart = () => {
  if (!scoreChartRef.value) return
  
  const chart = echarts.init(scoreChartRef.value)
  
  const option = {
    title: {
      text: '近6个月平均成绩趋势',
      left: 'center',
      textStyle: {
        fontSize: 14,
        fontWeight: 'normal'
      }
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['平均分', '最高分', '最低分'],
      bottom: 0
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '15%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['8月', '9月', '10月', '11月', '12月', '1月']
    },
    yAxis: {
      type: 'value',
      min: 0,
      max: 100
    },
    series: [
      {
        name: '平均分',
        type: 'line',
        smooth: true,
        data: [78, 82, 85, 83, 87, 89],
        itemStyle: { color: '#409EFF' },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
            { offset: 1, color: 'rgba(64, 158, 255, 0.05)' }
          ])
        }
      },
      {
        name: '最高分',
        type: 'line',
        smooth: true,
        data: [95, 96, 98, 97, 99, 98],
        itemStyle: { color: '#67C23A' }
      },
      {
        name: '最低分',
        type: 'line',
        smooth: true,
        data: [52, 58, 62, 60, 65, 68],
        itemStyle: { color: '#F56C6C' }
      }
    ]
  }
  
  chart.setOption(option)
  
  // 响应式调整
  window.addEventListener('resize', () => {
    chart.resize()
  })
}

// 跳转到 AI 助手
const goToChat = () => {
  router.push('/chat')
}

onMounted(() => {
  initScoreChart()
})
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  cursor: pointer;
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.stat-icon {
  width: 70px;
  height: 70px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.stat-icon.student {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.teacher {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.class {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.exam {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.chart-row {
  margin-bottom: 20px;
}

.card-header {
  font-weight: bold;
  color: #303133;
}

.chart-container {
  height: 350px;
}

.activity-timeline {
  padding-left: 10px;
}

.activity-timeline p {
  margin: 0;
  font-size: 14px;
  color: #606266;
}

.ai-row {
  margin-top: 20px;
}

.ai-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.ai-content {
  display: flex;
  align-items: center;
  gap: 30px;
  padding: 20px;
}

.ai-icon {
  color: white;
}

.ai-info h3 {
  margin: 0 0 10px 0;
  font-size: 24px;
  color: white;
}

.ai-info p {
  margin: 0 0 20px 0;
  font-size: 14px;
  opacity: 0.9;
}

.ai-info .el-button {
  background: white;
  color: #667eea;
  border: none;
}

.ai-info .el-button:hover {
  background: rgba(255, 255, 255, 0.9);
}
</style>
