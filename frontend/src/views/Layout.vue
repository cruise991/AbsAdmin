<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside width="220px" class="sidebar">
      <div class="logo">
        <h2>🎓 EduFlow</h2>
        <p>智慧教育管理平台</p>
      </div>
      
      <el-menu
        :default-active="activeMenu"
        router
        class="sidebar-menu"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/dashboard">
          <el-icon><Odometer /></el-icon>
          <span>首页</span>
        </el-menu-item>
        
        <el-sub-menu index="education">
          <template #title>
            <el-icon><School /></el-icon>
            <span>教育教学</span>
          </template>
          <el-menu-item index="/student/list">学生管理</el-menu-item>
          <el-menu-item index="/class/list">班级管理</el-menu-item>
          <el-menu-item index="/exam/list">考试管理</el-menu-item>
          <el-menu-item index="/score/list">成绩管理</el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="system">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/system/users">用户管理</el-menu-item>
          <el-menu-item index="/system/roles">角色管理</el-menu-item>
          <el-menu-item index="/system/organization">组织架构</el-menu-item>
          <el-menu-item index="/system/menu">菜单权限</el-menu-item>
          <el-menu-item index="/system/config">系统配置</el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="content">
          <template #title>
            <el-icon><Document /></el-icon>
            <span>内容管理</span>
          </template>
          <el-menu-item index="/content/article">文章管理</el-menu-item>
          <el-menu-item index="/content/file">文件管理</el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="workflow">
          <template #title>
            <el-icon><Connection /></el-icon>
            <span>工作流引擎</span>
          </template>
          <el-menu-item index="/workflow/definition">流程定义</el-menu-item>
          <el-menu-item index="/workflow/instance">流程实例</el-menu-item>
        </el-sub-menu>
        
        <el-menu-item index="/chat">
          <el-icon><ChatDotRound /></el-icon>
          <span>AI助手</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    
    <!-- 主内容区 -->
    <el-container>
      <!-- 顶部导航 -->
      <el-header class="header">
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-avatar :size="32" :src="userInfo.avatar">{{ userInfo.username?.charAt(0) }}</el-avatar>
              <span class="username">{{ userInfo.username }}</span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="settings">系统设置</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      
      <!-- 内容区域 -->
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  Odometer, Setting, User, UserFilled, Avatar, ChatDotRound,
  School, Document, Connection
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

const userInfo = ref({
  username: localStorage.getItem('username') || '管理员',
  avatar: ''
})

const activeMenu = computed(() => route.path)
const currentTitle = computed(() => route.meta.title || '')

const handleCommand = (command) => {
  if (command === 'logout') {
    localStorage.removeItem('token')
    localStorage.removeItem('username')
    ElMessage.success('退出成功')
    router.push('/login')
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.sidebar {
  background-color: #304156;
  overflow-y: auto;
}

.logo {
  height: 60px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: #263445;
  color: #fff;
}

.logo h2 {
  margin: 0;
  font-size: 20px;
}

.logo p {
  margin: 5px 0 0;
  font-size: 12px;
  color: #bfcbd9;
}

.sidebar-menu {
  border-right: none;
}

.header {
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  gap: 8px;
}

.username {
  font-size: 14px;
}

.main-content {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>
