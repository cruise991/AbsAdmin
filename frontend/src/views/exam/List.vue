<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>考试管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增考试
          </el-button>
        </div>
      </template>
      
      <el-empty description="考试管理功能开发中..." />
    </el-card>
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus'

const handleAdd = () => {
  ElMessage.info('功能开发中，敬请期待')
}
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
</style>
