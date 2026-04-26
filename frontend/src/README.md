# Frontend 目录结构说明

## 📁 目录组织

```
src/
├── api/              # API 接口封装
│   ├── auth.js       # 认证相关 API
│   ├── user.js       # 用户管理 API
│   └── ...
│
├── assets/           # 静态资源
│   ├── images/       # 图片资源
│   ├── styles/       # 全局样式
│   └── fonts/        # 字体文件
│
├── components/       # Vue 组件
│   ├── common/       # 通用组件（按钮、输入框等）
│   ├── layout/       # 布局组件（Header、Sidebar 等）
│   └── business/     # 业务组件（特定功能模块）
│
├── composables/      # Composition API（Vue3）
│   ├── useAuth.js    # 认证逻辑
│   ├── useTable.js   # 表格逻辑
│   └── ...
│
├── layouts/          # 页面布局
│   ├── DefaultLayout.vue
│   └── BlankLayout.vue
│
├── router/           # 路由配置
│   ├── index.js      # 路由主配置
│   └── routes.js     # 路由定义
│
├── stores/           # Pinia 状态管理
│   ├── user.js       # 用户状态
│   ├── app.js        # 应用状态
│   └── ...
│
├── utils/            # 工具函数
│   ├── request.js    # Axios 封装
│   ├── auth.js       # 认证工具
│   └── ...
│
├── views/            # 页面组件
│   ├── dashboard/    # 仪表盘
│   ├── system/       # 系统管理
│   ├── student/      # 学生管理
│   ├── content/      # 内容管理
│   └── ...
│
├── App.vue           # 根组件
└── main.js           # 应用入口
```

## 🎯 命名规范

### 组件命名
- **文件名**：PascalCase（如 `UserProfile.vue`）
- **组件名**：与文件名一致
- **目录名**：kebab-case（如 `user-profile/`）

### 文件组织
- **API 文件**：按模块划分（`user.js`, `article.js`）
- **Store 文件**：按状态域划分（`user.js`, `app.js`）
- **Composable**：以 `use` 开头（`useAuth.js`, `useTable.js`）

## 📝 最佳实践

### 1. Components 分类

**common/** - 通用组件
- 无业务逻辑
- 高度可复用
- 示例：`BaseButton.vue`, `BaseInput.vue`

**layout/** - 布局组件
- 页面结构相关
- 包含多个子组件
- 示例：`AppHeader.vue`, `Sidebar.vue`

**business/** - 业务组件
- 包含业务逻辑
- 特定场景使用
- 示例：`UserList.vue`, `ArticleEditor.vue`

### 2. Composables 使用

```javascript
// composables/useAuth.js
import { ref, computed } from 'vue'
import { useStore } from 'pinia'

export function useAuth() {
  const userStore = useStore('user')
  
  const login = async (credentials) => {
    // 登录逻辑
  }
  
  const logout = () => {
    // 登出逻辑
  }
  
  return {
    login,
    logout,
    user: computed(() => userStore.user)
  }
}
```

### 3. Stores 组织

```javascript
// stores/user.js
import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null,
    token: null
  }),
  
  actions: {
    setUser(userData) {
      this.user = userData
    }
  }
})
```

## 🔧 开发指南

### 添加新页面

1. 在 `views/` 下创建页面目录
2. 创建页面组件 `.vue` 文件
3. 在 `router/routes.js` 中添加路由
4. 如需状态管理，在 `stores/` 中创建 store

### 添加新组件

1. 确定组件类型（common/layout/business）
2. 在对应目录下创建组件文件
3. 遵循 PascalCase 命名
4. 导出组件供其他模块使用

### 添加新 API

1. 在 `api/` 下创建或修改模块文件
2. 使用统一的 request 封装
3. 导出 API 方法
4. 在组件中导入使用

---

**最后更新**：2026-04-26  
**维护者**：EduFlow Frontend Team
