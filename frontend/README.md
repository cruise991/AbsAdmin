# EduFlow 前端项目

基于 Vue 3 + Element Plus 的现代化教育管理平台前端。

## 技术栈

- **Vue 3.4.21** - 渐进式 JavaScript 框架
- **Vite 5.2.8** - 下一代前端构建工具
- **Element Plus 2.6.3** - Vue 3 组件库
- **Vue Router 4.3.0** - 官方路由管理器
- **Pinia 2.1.7** - Vue 状态管理库
- **Axios 1.6.8** - HTTP 客户端
- **ECharts 5.5.0** - 数据可视化图表库

## 快速开始

### 1. 安装依赖

```bash
cd /Users/workspace/order/AbsAdmin/frontend
npm install
```

### 2. 启动开发服务器

```bash
npm run dev
```

访问 http://localhost:3000

### 3. 构建生产版本

```bash
npm run build
```

构建产物将输出到 `../src/main/resources/static/admin` 目录，由 Spring Boot 提供服务。

## 项目结构

```
frontend/
├── src/
│   ├── api/              # API 接口
│   │   ├── request.js    # Axios 实例配置
│   │   └── auth.js       # 认证相关接口
│   ├── assets/           # 静态资源
│   ├── components/       # 公共组件
│   ├── router/           # 路由配置
│   │   └── index.js
│   ├── store/            # Pinia 状态管理
│   ├── utils/            # 工具函数
│   ├── views/            # 页面组件
│   │   ├── Login.vue     # 登录页面
│   │   ├── Layout.vue    # 主布局
│   │   ├── Chat.vue      # AI 助手
│   │   ├── dashboard/    # 仪表盘
│   │   ├── system/       # 系统管理
│   │   ├── student/      # 学生管理
│   │   └── ...
│   ├── App.vue           # 根组件
│   ├── main.js           # 应用入口
│   └── style.css         # 全局样式
├── index.html            # HTML 模板
├── vite.config.js        # Vite 配置
└── package.json          # 项目配置
```

## 已完成的页面

### ✅ 完整实现（已对接后端 API）

**登录页面** (`/login`)
- 用户名密码登录
- 表单验证
- Token 存储
- 对接后端 `/login/userlogin` 接口

**仪表盘** (`/dashboard`)
- 统计数据卡片（学生、教师、班级、考试）
- 成绩趋势图表（ECharts）
- 近期活动时间线
- AI 助手快捷入口

**用户管理** (`/system/users`)
- 用户列表展示
- 搜索和筛选
- 新增/编辑/删除用户
- 分页功能

**角色管理** (`/system/roles`)
- 角色列表展示
- 新增/编辑/删除角色
- 分页功能

**学生管理** (`/student/list`)
- 学生列表展示
- 搜索和筛选
- 新增/编辑/删除学生
- 分页功能

**班级管理** (`/class/list`) 🆕
- 班级列表展示
- 搜索和筛选（班级名称、年级）
- 新增/编辑/删除班级
- 分页功能
- 状态管理（启用/停用）
- 已对接后端 `/class/*` API

**AI 助手** (`/chat`) 🆕
- 真实接入 DeepSeek API
- 流式对话界面
- 消息历史记录
- 清空对话功能
- 打字动画效果
- 对接后端 `/api/ai/create` 接口

### ⏳ 占位页面（待开发）

**考试管理** (`/exam/list`)
- 基础框架已搭建
- 待对接后端 `/tsinfo/*` API

**成绩管理** (`/score/list`)
- 基础框架已搭建
- 待实现成绩录入、查询、分析功能

**组织架构** (`/system/organization`)
- 基础框架已搭建
- 待实现树形结构展示

**菜单权限** (`/system/menu`)
- 基础框架已搭建
- 待实现树形菜单配置

**系统配置** (`/system/config`)
- 基础框架已搭建
- 待实现系统参数配置

**文章管理** (`/content/article`)
- 基础框架已搭建
- 待集成富文本编辑器

**文件管理** (`/content/file`)
- 基础框架已搭建
- 待实现文件上传下载

**流程定义** (`/workflow/definition`)
- 基础框架已搭建
- 待实现可视化流程设计器

**流程实例** (`/workflow/instance`)
- 基础框架已搭建
- 待实现流程审批功能

## 开发说明

### API 代理配置

开发模式下，所有 `/api/*` 请求会被代理到后端服务器 `http://localhost:8020/*`

配置位置：`vite.config.js`

```javascript
server: {
  port: 3000,
  proxy: {
    '/api': {
      target: 'http://localhost:8020',
      changeOrigin: true,
      rewrite: (path) => path.replace(/^\/api/, '')
    }
  }
}
```

### Token 认证

所有 API 请求会自动在 Header 中添加 Token：

```javascript
Authorization: Bearer <token>
```

Token 存储在 `localStorage` 中，登录时保存，退出时清除。

### 路由守卫

未登录用户访问受保护的路由时，会自动重定向到登录页面。

## 默认账号

- 用户名：`admin`
- 密码：`123456`

## 下一步工作

⏳ 需要对接真实的后端 API 接口
⏳ 需要实现更多功能模块（教师管理、成绩管理、考试管理等）
⏳ 需要完善权限控制
⏳ 需要添加更多的数据可视化图表

## 注意事项

1. **确保后端服务运行**：前端需要后端 API 支持，请确保 Spring Boot 应用在 8020 端口运行
2. **DeepSeek API Key**：已在 `.env` 文件中配置，Docker 容器中已正确加载
3. **跨域问题**：开发模式通过 Vite 代理解决，生产模式由 Spring Boot 统一处理

## 常见问题

### Q: npm install 失败？
A: 尝试清除缓存后重试：
```bash
npm cache clean --force
npm install
```

### Q: 页面空白？
A: 检查浏览器控制台是否有错误，确保后端服务正常运行

### Q: 登录失败？
A: 检查后端登录接口是否正常，查看浏览器 Network 面板的请求响应

## 联系方式

如有问题，请联系开发团队。
