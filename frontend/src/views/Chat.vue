<template>
  <div class="chat-container">
    <el-card class="chat-card">
      <template #header>
        <div class="card-header">
          <span>🤖 AI 智能助手</span>
          <el-button @click="clearChat" size="small">
            <el-icon><Delete /></el-icon>
            清空对话
          </el-button>
        </div>
      </template>
      
      <!-- 消息列表 -->
      <div class="messages-container" ref="messagesRef">
        <div
          v-for="(message, index) in messages"
          :key="index"
          :class="['message', message.role]"
        >
          <div class="message-avatar">
            <el-avatar v-if="message.role === 'user'" :size="40">
              <el-icon><User /></el-icon>
            </el-avatar>
            <el-avatar v-else :size="40" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%)">
              <el-icon><ChatDotRound /></el-icon>
            </el-avatar>
          </div>
          <div class="message-content">
            <div class="message-text">{{ message.content }}</div>
            <div class="message-time">{{ message.time }}</div>
          </div>
        </div>
        
        <!-- 加载中提示 -->
        <div v-if="loading" class="message ai">
          <div class="message-avatar">
            <el-avatar :size="40" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%)">
              <el-icon><ChatDotRound /></el-icon>
            </el-avatar>
          </div>
          <div class="message-content">
            <div class="message-text typing">
              <span></span>
              <span></span>
              <span></span>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 输入框 -->
      <div class="input-area">
        <el-input
          v-model="inputMessage"
          type="textarea"
          :rows="3"
          placeholder="请输入您的问题..."
          @keydown.enter.ctrl="sendMessage"
        />
        <div class="input-actions">
          <span class="tip">Ctrl + Enter 发送</span>
          <el-button
            type="primary"
            @click="sendMessage"
            :loading="loading"
            :disabled="!inputMessage.trim()"
          >
            <el-icon><Promotion /></el-icon>
            发送
          </el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const messages = ref([])
const inputMessage = ref('')
const loading = ref(false)
const messagesRef = ref(null)

// 滚动到底部
const scrollToBottom = async () => {
  await nextTick()
  if (messagesRef.value) {
    messagesRef.value.scrollTop = messagesRef.value.scrollHeight
  }
}

// 发送消息
const sendMessage = async () => {
  if (!inputMessage.value.trim() || loading.value) return
  
  const userMessage = {
    role: 'user',
    content: inputMessage.value,
    time: formatTime(new Date())
  }
  
  messages.value.push(userMessage)
  const question = inputMessage.value
  inputMessage.value = ''
  loading.value = true
  
  await scrollToBottom()
  
  try {
    // 调用后端 AI 接口
    const response = await axios.post('/api/ai/create', {
      type: 'text',
      topic: question,
      style: 'professional',
      length: 'medium',
      keywords: '',
      model: 'deepseek'
    }, {
      headers: {
        'Content-Type': 'application/json'
      }
    })
    
    // 后端返回格式：{ code: "0000", data: "AI生成的文本", message: "..." }
    if (response.data && response.data.code === '0000') {
      const aiMessage = {
        role: 'ai',
        content: response.data.data || 'AI 未返回内容',
        time: formatTime(new Date())
      }
      messages.value.push(aiMessage)
    } else {
      ElMessage.error(response.data?.message || response.data?.msg || 'AI 回复失败')
    }
  } catch (error) {
    console.error('AI 对话错误:', error)
    ElMessage.error(error.response?.data?.msg || 'AI 服务暂时不可用，请稍后重试')
  } finally {
    loading.value = false
    await scrollToBottom()
  }
}

// 清空对话
const clearChat = () => {
  messages.value = []
  ElMessage.success('对话已清空')
}

// 格式化时间
const formatTime = (date) => {
  const hours = date.getHours().toString().padStart(2, '0')
  const minutes = date.getMinutes().toString().padStart(2, '0')
  return `${hours}:${minutes}`
}
</script>

<style scoped>
.chat-container {
  padding: 20px;
  height: calc(100vh - 100px);
}

.chat-card {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}

.messages-container {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background: #f5f7fa;
}

.message {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
  animation: fadeIn 0.3s ease-in;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.message.user {
  flex-direction: row-reverse;
}

.message.user .message-content {
  align-items: flex-end;
}

.message.ai .message-content {
  align-items: flex-start;
}

.message-avatar {
  flex-shrink: 0;
}

.message-content {
  display: flex;
  flex-direction: column;
  max-width: 70%;
}

.message-text {
  padding: 12px 16px;
  border-radius: 8px;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  line-height: 1.6;
  white-space: pre-wrap;
  word-wrap: break-word;
}

.message.user .message-text {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.message-time {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
  padding: 0 5px;
}

.typing span {
  display: inline-block;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #909399;
  margin: 0 2px;
  animation: typing 1.4s infinite;
}

.typing span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 60%, 100% {
    transform: translateY(0);
  }
  30% {
    transform: translateY(-10px);
  }
}

.input-area {
  border-top: 1px solid #e4e7ed;
  padding: 20px;
  background: white;
}

.input-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.tip {
  font-size: 12px;
  color: #909399;
}
</style>
