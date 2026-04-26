# ============================================================
# EduFlow Frontend Dockerfile - 多阶段构建
# ============================================================

# -------------------- 阶段1: 构建阶段 --------------------
FROM node:18-alpine AS builder

WORKDIR /build

# 复制 package 文件
COPY package*.json ./

# 安装依赖（使用淘宝镜像加速）
RUN npm config set registry https://registry.npmmirror.com && \
    npm install

# 复制源代码
COPY . .

# 构建生产版本（输出到 dist 目录）
RUN npm run build -- --outDir dist

# -------------------- 阶段2: Nginx 运行阶段 --------------------
FROM nginx:alpine

# 设置元数据
LABEL maintainer="EduFlow Platform Team"
LABEL description="EduFlow Platform Frontend"
LABEL version="1.0.0"

# 复制自定义 Nginx 配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 从构建阶段复制构建产物
COPY --from=builder /build/dist /usr/share/nginx/html

# 创建非 root 用户（可选，Nginx 默认以 nginx 用户运行）
RUN chown -R nginx:nginx /usr/share/nginx/html && \
    chmod -R 755 /usr/share/nginx/html

# 暴露端口
EXPOSE 80

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://127.0.0.1:80/ || exit 1

# 启动 Nginx
CMD ["nginx", "-g", "daemon off;"]
