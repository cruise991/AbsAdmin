# ============================================================
# EduFlow Frontend Dockerfile - 单阶段构建（使用本地构建产物）
# ============================================================

FROM nginx:alpine

# 设置元数据
LABEL maintainer="EduFlow Platform Team"
LABEL description="EduFlow Platform Frontend"
LABEL version="1.0.0"

# 从本地复制已构建的产物（需要先执行 npm run build）
COPY dist /usr/share/nginx/html

# 复制自定义 Nginx 配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 设置权限
RUN chown -R nginx:nginx /usr/share/nginx/html && \
    chmod -R 755 /usr/share/nginx/html

# 暴露端口
EXPOSE 80

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://127.0.0.1:80/ || exit 1

# 启动 Nginx
CMD ["nginx", "-g", "daemon off;"]
