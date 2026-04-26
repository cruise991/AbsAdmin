# ============================================================
# EduFlow Platform Dockerfile - 多阶段构建优化
# ============================================================

# -------------------- 阶段1: 构建阶段 --------------------
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /build

# 配置阿里云 Maven 镜像（加速下载）
COPY settings.xml /usr/share/maven/ref/

# 复制源代码和 pom.xml
COPY pom.xml .
COPY src ./src

# 直接构建（Maven 会在构建时下载需要的依赖）
RUN mvn clean package -DskipTests -B -s /usr/share/maven/ref/settings.xml

# -------------------- 阶段2: 运行阶段 --------------------
FROM eclipse-temurin:17-jdk-jammy

# 设置元数据
LABEL maintainer="EduFlow Platform Team"
LABEL description="EduFlow Platform Spring Boot Application"
LABEL version="0.0.1-SNAPSHOT"

# 设置工作目录
WORKDIR /app

# 安装必要的系统依赖
RUN apt-get update && apt-get install -y \
    fontconfig \
    fonts-dejavu-core \
    curl \
    && rm -rf /var/lib/apt/lists/* \
    && fc-cache -fv

# 复制字体文件（用于验证码和 PDF 生成）
COPY src/main/resources/font/*.ttf /usr/share/fonts/truetype/
RUN fc-cache -fv

# 从构建阶段复制 JAR 包
COPY --from=builder /build/target/EduFlow-Platform.jar app.jar

# 创建非 root 用户（安全最佳实践）
RUN groupadd -r eduflow && useradd -r -g eduflow -m eduflow

# 创建必要的目录并设置权限
RUN mkdir -p /app/uploads /app/logs /app/drivers \
    && chown -R eduflow:eduflow /app

# 切换到非 root 用户
USER eduflow

# 暴露端口
EXPOSE 8020

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8020/login/istokenvalid || exit 1

# 环境变量（可通过 docker-compose 覆盖）
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC" \
    SPRING_PROFILES_ACTIVE="docker" \
    UPLOAD_PATH="/app/uploads"

# JVM 参数优化
ENV JAVA_TOOL_OPTIONS="-XX:+UnlockExperimentalVMOptions \
    -XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:InitialRAMPercentage=50.0"

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS $JAVA_TOOL_OPTIONS -jar app.jar"]
