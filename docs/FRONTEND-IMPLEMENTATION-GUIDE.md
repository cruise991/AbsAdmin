# EduFlow Platform 前端开发实施指南

**创建时间**: 2026-04-24  
**技术栈**: Vue 3 + Vite + Element Plus + Pinia

---

## ✅ 已完成的工作

### Phase 1 & 2: 核心管理功能完整实现（2026-04-26）

**完成度**: 73% (11/15页面)

#### 已完成的页面（11个）

**基础框架**：
1. ✅ 登录页面 (`Login.vue`) - 完整的登录验证和Token管理
2. ✅ 主布局 (`Layout.vue`) - 侧边栏、顶部导航、面包屑
3. ✅ 仪表盘 (`dashboard/Index.vue`) - 统计数据和图表展示
4. ✅ AI助手 (`Chat.vue`) - 真实接入DeepSeek API

**教育教学模块**（100%）：
5. ✅ 学生管理 (`student/List.vue`) - 完整CRUD功能
6. ✅ 班级管理 (`class/List.vue`) - 完整CRUD功能，对接后端API
7. ✅ 考试管理 (`exam/List.vue`) - 考试链接管理，百度推送
8. ✅ 成绩管理 (`score/List.vue`) - 成绩展示、搜索、导出Excel

**系统管理模块**（100%）：
9. ✅ 用户管理 (`system/Users.vue`) - 完整CRUD，表单验证
10. ✅ 角色管理 (`system/Roles.vue`) - 完整CRUD，前端分页
11. ✅ 组织架构 (`system/Organization.vue`) - 组织信息管理
12. ✅ 菜单权限 (`system/Menu.vue`) - 菜单CRUD，图标配置
13. ✅ 系统配置 (`system/Config.vue`) - 配置项编辑

**待开发**（4个）：
- ⏳ 文章管理 (`content/Article.vue`) - Phase 3
- ⏳ 文件管理 (`content/File.vue`) - Phase 3
- ⏳ 流程定义 (`workflow/Definition.vue`) - Phase 3
- ⏳ 流程实例 (`workflow/Instance.vue`) - Phase 3

### Phase 3: 内容管理和工作流引擎（进行中）

**目标**: 完成剩余4个页面，系统完成度达到100%

---

## 📋 待完成的工作

### 第一步：安装依赖并启动开发服务器

```bash
cd /Users/workspace/order/AbsAdmin/frontend
npm install
npm run dev
```

**访问地址**: http://localhost:3000

### 第二步：创建核心页面

需要创建以下页面文件（参考 Layout.vue 的风格）：

#### 1. 登录页面 (src/views/Login.vue)

```vue
<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <h1>🎓 EduFlow Platform</h1>
        <p>智慧教育管理平台</p>
      </div>
      
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef">
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="用户名"
            size="large"
            prefix-icon="User"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="密码"
            size="large"
            prefix-icon="Lock"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            style="width: 100%"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const router = useRouter()
const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const response = await axios.post('/login/userlogin', loginForm)
        if (response.data.code === '0000') {
          localStorage.setItem('token', response.data.data.token)
          localStorage.setItem('username', response.data.data.username)
          ElMessage.success('登录成功')
          router.push('/')
        } else {
          ElMessage.error(response.data.msg || '登录失败')
        }
      } catch (error) {
        ElMessage.error('网络错误，请稍后重试')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-box {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 10px 40px rgba(0,0,0,0.2);
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h1 {
  margin: 0;
  color: #303133;
}

.login-header p {
  margin: 10px 0 0;
  color: #909399;
  font-size: 14px;
}
</style>
```

#### 2. 仪表盘页面 (src/views/dashboard/Index.vue)

```vue
<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6" v-for="stat in stats" :key="stat.title">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" :style="{ background: stat.color }">
              <el-icon :size="30"><component :is="stat.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stat.value }}</div>
              <div class="stat-title">{{ stat.title }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>学生成绩趋势</span>
            </div>
          </template>
          <div ref="chartRef" style="height: 300px"></div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card>
          <template #header>近期活动</template>
          <el-timeline>
            <el-timeline-item
              v-for="(activity, index) in activities"
              :key="index"
              :timestamp="activity.timestamp"
            >
              {{ activity.content }}
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'

const stats = ref([
  { title: '学生总数', value: '1,234', icon: 'Avatar', color: '#409EFF' },
  { title: '教师总数', value: '56', icon: 'User', color: '#67C23A' },
  { title: '班级数量', value: '28', icon: 'School', color: '#E6A23C' },
  { title: '今日考试', value: '3', icon: 'Document', color: '#F56C6C' }
])

const activities = ref([
  { content: '新增学生张三', timestamp: '2026-04-24 10:00' },
  { content: '完成期中考试录入', timestamp: '2026-04-24 09:30' },
  { content: '系统配置更新', timestamp: '2026-04-24 09:00' }
])

const chartRef = ref(null)

onMounted(() => {
  const chart = echarts.init(chartRef.value)
  chart.setOption({
    xAxis: {
      type: 'category',
      data: ['周一', '周二', '周三', '周四', '周五']
    },
    yAxis: { type: 'value' },
    series: [{
      data: [82, 85, 88, 86, 90],
      type: 'line',
      smooth: true,
      areaStyle: {}
    }]
  })
})
</script>

<style scoped>
.stat-card {
  display: flex;
  align-items: center;
  gap: 15px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.stat-title {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.card-header {
  font-weight: bold;
}
</style>
```

### 第三步：创建 API 请求工具

创建 `src/api/request.js`:

```javascript
import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

const request = axios.create({
  baseURL: '/api',  // 通过 Vite 代理到后端
  timeout: 10000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = token
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== '0000') {
      if (res.code === 'NOLOGIN') {
        ElMessage.error('登录已过期，请重新登录')
        localStorage.removeItem('token')
        router.push('/login')
      } else {
        ElMessage.error(res.msg || '请求失败')
      }
      return Promise.reject(new Error(res.msg || '请求失败'))
    }
    return res
  },
  error => {
    ElMessage.error('网络错误')
    return Promise.reject(error)
  }
)

export default request
```

### 第四步：构建并集成到后端

```bash
# 构建前端
cd frontend
npm run build

# 构建产物会输出到: ../src/main/resources/static/admin/
# 然后重新构建 Docker 镜像
cd ..
docker-compose down
docker-compose up -d --build
```

### 第五步：更新路由配置

修改 `AbsWebMvcConfigurerAdapter.java`:

```java
@Override
public void addViewControllers(ViewControllerRegistry registry) {
    // 根路径重定向到前端管理后台
    registry.addRedirectViewController("/", "/admin/index.html");
    registry.addRedirectViewController("/index", "/admin/index.html");
    
    // AI 对话页面（保留旧版）
    registry.addRedirectViewController("/chat", "/canvas/index.html");
}
```

---

## 🚀 快速启动指南

### 方式一：开发模式（推荐）

```bash
# 1. 启动后端服务
cd /Users/workspace/order/AbsAdmin
docker-compose up -d

# 2. 安装前端依赖
cd frontend
npm install

# 3. 启动开发服务器
npm run dev

# 访问 http://localhost:3000
```

**优点**:
- 热更新，修改即生效
- 开发体验好
- 易于调试

### 方式二：生产模式

```bash
# 1. 构建前端
cd frontend
npm run build

# 2. 重新构建 Docker
cd ..
docker-compose down
docker-compose up -d --build

# 访问 http://localhost:8020
```

**优点**:
- 单端口访问
- 无需跨域配置
- 适合生产环境

---

## 🔧 配置说明

### 开发环境代理配置

`vite.config.js` 中已配置：

```javascript
server: {
  port: 3000,
  proxy: {
    '/api': {
      target: 'http://localhost:8020',  // 后端地址
      changeOrigin: true,
      rewrite: (path) => path.replace(/^\/api/, '')
    }
  }
}
```

这样前端请求 `/api/login/userlogin` 会被代理到 `http://localhost:8020/login/userlogin`

### Token 认证

所有需要登录的接口都需要在 Header 中携带 Token：

```javascript
Authorization: <token_value>
```

### 环境变量

创建 `frontend/.env.development`:

```env
VITE_API_BASE_URL=http://localhost:8020
```

---

## 📊 项目架构

```
┌─────────────────────────────────────────────────────────┐
│  浏览器 (http://localhost:3006)                           │
├─────────────────────────────────────────────────────────┤
│  Vue 3 + Element Plus + Pinia 前端应用                    │
│  - 11个完整功能页面 (73%完成度)                           │
│  - 路由守卫 (登录验证)                                     │
│  - 状态管理 (Pinia)                                      │
│  - HTTP 请求 (Axios + 拦截器)                             │
│  - Excel 导出 (xlsx)                                     │
├─────────────────────────────────────────────────────────┤
│  Docker 容器编排 (docker-compose)                         │
│  - eduflow-frontend: Nginx (端口 3006)                  │
│  - absadmin-app: Spring Boot (端口 8020)                │
│  - absadmin-mysql: MySQL 8.0 (端口 3306)                │
│  - absadmin-nginx: Nginx反向代理 (端口 80/443)           │
│  - 统一网络: absadmin-network                            │
├─────────────────────────────────────────────────────────┤
│  Spring Boot 后端 (http://localhost:8020)                │
│  - 23个Controller                                        │
│  - Token 认证                                            │
│  - DeepSeek AI 集成                                      │
│  - RESTful API                                           │
├─────────────────────────────────────────────────────────┤
│  MySQL 8.0 数据库 (absframe)                             │
└─────────────────────────────────────────────────────────┘
```

---

## 🔧 重要修复记录

### 2026-04-26: API请求失败修复

**问题**: 所有管理页面提示"请求失败"和"加载列表失败"

**根本原因**:
1. Token格式错误：前端发送`Bearer {token}`，后端期望`{token}`
2. 响应拦截器code判断错误：后端返回字符串`"0000"`，前端判断数字`200`

**修复**:
- ✅ 修正`request.js`：去掉Bearer前缀
- ✅ 响应拦截器兼容`"0000"`和`200`
- ✅ 实现真实的Excel导出功能

**文档**: `BUGFIX-API-REQUEST-FAILURE.md`

### 2026-04-26: Docker部署问题修复

**问题**: Docker环境中仍显示占位页面

**根本原因**: Vite构建输出目录配置错误，部署了旧文件

**修复**:
- ✅ 修正`vite.config.js`：`outDir: 'dist'`
- ✅ 清理并重新构建
- ✅ 重新部署到Docker

**文档**: `BUGFIX-DOCKER-DEPLOYMENT.md`

### 2026-04-26: Docker容器统一管理

**问题**: eduflow-frontend脱离docker-compose成为独立容器

**根本原因**: 使用`docker run`手动创建容器

**修复**:
- ✅ 删除独立容器
- ✅ 通过`docker compose up -d frontend`重新创建
- ✅ 所有容器统一在absadmin项目下管理

**经验**: 已记录到记忆库，严禁使用`docker run`

---

## 🎯 下一步计划

### 本周完成
1. ✅ DeepSeek API 连接修复
2. ✅ Vue 3 项目初始化
3. ✅ 创建 Layout 布局
4. ⏳ 完成登录页面
5. ⏳ 完成仪表盘页面
6. ⏳ 创建用户管理页面
7. ⏳ 创建学生管理页面

### 下周完成
1. 完善所有功能模块页面
2. 对接后端 API
3. 实现数据可视化图表
4. 添加权限控制
5. 优化移动端适配
6. 性能优化和测试

---

## 📚 参考资源

- **Vue 3 官方文档**: https://cn.vuejs.org/
- **Element Plus 文档**: https://element-plus.org/zh-CN/
- **Vite 官方文档**: https://cn.vitejs.dev/
- **Vue Router 文档**: https://router.vuejs.org/zh/
- **Pinia 文档**: https://pinia.vuejs.org/zh/
- **Axios 文档**: https://axios-http.com/zh/docs/intro

---

## ✨ 总结

**已完成**:
- ✅ DeepSeek API 配置问题已修复并验证
- ✅ Docker 容器正常运行，所有服务健康
- ✅ Vue 3 + Element Plus 前端项目已初始化
- ✅ 核心布局组件 (Layout.vue) 已创建
- ✅ 路由配置和路由守卫已完成
- ✅ 项目结构完整，依赖配置正确

**下一步**:
1. 运行 `npm install` 安装依赖
2. 运行 `npm run dev` 启动开发服务器
3. 创建登录页面和仪表盘页面
4. 对接后端 API
5. 逐步完善各功能模块

**访问地址**:
- 后端 API: http://localhost:8020
- 前端开发: http://localhost:3000 (启动后)

---

**文档版本**: v2.0  
**最后更新**: 2026-04-26  
**状态**: Phase 1&2已完成 ✅ | 系统功能完成度73% | 进入Phase 3开发
