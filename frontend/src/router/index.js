import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/views/Layout.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/Index.vue'),
        meta: { title: '首页', icon: 'Odometer' }
      },
      // 教育教学
      {
        path: '/student/list',
        name: 'StudentManage',
        component: () => import('@/views/student/List.vue'),
        meta: { title: '学生管理', icon: 'Avatar' }
      },
      {
        path: '/class/list',
        name: 'ClassManage',
        component: () => import('@/views/class/List.vue'),
        meta: { title: '班级管理', icon: 'School' }
      },
      {
        path: '/exam/list',
        name: 'ExamManage',
        component: () => import('@/views/exam/List.vue'),
        meta: { title: '考试管理', icon: 'Document' }
      },
      {
        path: '/score/list',
        name: 'ScoreManage',
        component: () => import('@/views/score/List.vue'),
        meta: { title: '成绩管理', icon: 'TrendCharts' }
      },
      // 系统管理
      {
        path: '/system/users',
        name: 'UserManage',
        component: () => import('@/views/system/Users.vue'),
        meta: { title: '用户管理', icon: 'User' }
      },
      {
        path: '/system/roles',
        name: 'RoleManage',
        component: () => import('@/views/system/Roles.vue'),
        meta: { title: '角色管理', icon: 'UserFilled' }
      },
      {
        path: '/system/organization',
        name: 'OrganizationManage',
        component: () => import('@/views/system/Organization.vue'),
        meta: { title: '组织架构', icon: 'OfficeBuilding' }
      },
      {
        path: '/system/menu',
        name: 'MenuManage',
        component: () => import('@/views/system/Menu.vue'),
        meta: { title: '菜单权限', icon: 'Menu' }
      },
      {
        path: '/system/config',
        name: 'SystemConfig',
        component: () => import('@/views/system/Config.vue'),
        meta: { title: '系统配置', icon: 'Setting' }
      },
      // 内容管理
      {
        path: '/content/article',
        name: 'ArticleManage',
        component: () => import('@/views/content/Article.vue'),
        meta: { title: '文章管理', icon: 'Document' }
      },
      {
        path: '/content/file',
        name: 'FileManage',
        component: () => import('@/views/content/File.vue'),
        meta: { title: '文件管理', icon: 'Folder' }
      },
      // 工作流引擎
      {
        path: '/workflow/definition',
        name: 'WorkflowDefinition',
        component: () => import('@/views/workflow/Definition.vue'),
        meta: { title: '流程定义', icon: 'Connection' }
      },
      {
        path: '/workflow/instance',
        name: 'WorkflowInstance',
        component: () => import('@/views/workflow/Instance.vue'),
        meta: { title: '流程实例', icon: 'List' }
      },
      // AI助手
      {
        path: '/chat',
        name: 'AIChat',
        component: () => import('@/views/Chat.vue'),
        meta: { title: 'AI助手', icon: 'ChatDotRound' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  if (to.path === '/login') {
    next()
  } else {
    if (token) {
      next()
    } else {
      next('/login')
    }
  }
})

export default router
