#!/bin/bash

# 项目结构优化脚本 - 阶段一（低风险）
# 执行前请确保已备份重要文件

set -e  # 遇到错误立即退出

echo "=========================================="
echo "  EduFlow Platform 项目结构优化"
echo "  阶段一：整理根目录和文档"
echo "=========================================="
echo ""

# 检查是否在正确的目录
if [ ! -f "pom.xml" ]; then
    echo "❌ 错误：请在项目根目录执行此脚本"
    exit 1
fi

echo "📋 当前目录结构检查..."
echo "   根目录文件数: $(ls -1 | wc -l)"
echo ""

# 创建备份
BACKUP_DIR="backup-structure-$(date +%Y%m%d-%H%M%S)"
echo "📦 创建备份目录: $BACKUP_DIR"
mkdir -p "$BACKUP_DIR"
cp -r docs "$BACKUP_DIR/" 2>/dev/null || true
echo "   ✅ 备份完成"
echo ""

# ==========================================
# 步骤 1: 整理 Docker 相关文件
# ==========================================
echo "🔧 步骤 1: 整理 Docker 相关文件..."

if [ ! -d "docker" ]; then
    mkdir -p docker/nginx/conf.d
    
    # 移动 Dockerfile
    if [ -f "Dockerfile" ]; then
        mv Dockerfile docker/Dockerfile.backend
        echo "   ✅ 移动 Dockerfile → docker/Dockerfile.backend"
    fi
    
    if [ -f "Dockerfile.alternative" ]; then
        mv Dockerfile.alternative docker/Dockerfile.backend.alternative
        echo "   ✅ 移动 Dockerfile.alternative → docker/Dockerfile.backend.alternative"
    fi
    
    # 移动 docker-compose.yml
    if [ -f "docker-compose.yml" ]; then
        mv docker-compose.yml docker/
        echo "   ✅ 移动 docker-compose.yml → docker/"
    fi
    
    # 移动 nginx 配置
    if [ -d "nginx" ]; then
        mv nginx/* docker/nginx/ 2>/dev/null || true
        rmdir nginx 2>/dev/null || true
        echo "   ✅ 移动 nginx/ → docker/nginx/"
    fi
    
    echo "   ✅ Docker 文件整理完成"
else
    echo "   ⚠️  docker/ 目录已存在，跳过"
fi
echo ""

# ==========================================
# 步骤 2: 整理脚本文件
# ==========================================
echo "🔧 步骤 2: 整理脚本文件..."

if [ ! -d "scripts/deploy" ]; then
    mkdir -p scripts/deploy scripts/utils
    
    # 移动部署脚本
    if [ -f "fix-and-deploy.sh" ]; then
        mv fix-and-deploy.sh scripts/deploy/deploy.sh
        chmod +x scripts/deploy/deploy.sh
        echo "   ✅ 移动 fix-and-deploy.sh → scripts/deploy/deploy.sh"
    fi
    
    # 移动前端启动脚本
    if [ -f "start-frontend.sh" ]; then
        mv start-frontend.sh scripts/utils/start-frontend.sh
        chmod +x scripts/utils/start-frontend.sh
        echo "   ✅ 移动 start-frontend.sh → scripts/utils/start-frontend.sh"
    fi
    
    echo "   ✅ 脚本文件整理完成"
else
    echo "   ⚠️  scripts/ 目录已存在，跳过"
fi
echo ""

# ==========================================
# 步骤 3: 整理配置文件
# ==========================================
echo "🔧 步骤 3: 整理配置文件..."

if [ ! -d "config" ]; then
    mkdir -p config
    
    # 移动 Maven 配置
    if [ -f "settings.xml" ]; then
        mv settings.xml config/maven-settings.xml
        echo "   ✅ 移动 settings.xml → config/maven-settings.xml"
    fi
    
    # 复制 .env.example（保留原文件用于兼容）
    if [ -f ".env.example" ]; then
        cp .env.example config/.env.example
        echo "   ✅ 复制 .env.example → config/.env.example"
    fi
    
    echo "   ✅ 配置文件整理完成"
else
    echo "   ⚠️  config/ 目录已存在，跳过"
fi
echo ""

# ==========================================
# 步骤 4: 合并 README 文件
# ==========================================
echo "🔧 步骤 4: 检查 README 文件..."

if [ -f "README-Docker.md" ]; then
    echo "   ⚠️  发现 README-Docker.md"
    echo "   📝 建议：手动将其内容整合到 README.md 的 Docker 部署章节"
    echo "   📝 然后删除 README-Docker.md"
    # 不自动删除，需要人工确认
else
    echo "   ✅ 无需处理"
fi
echo ""

# ==========================================
# 步骤 5: 清理过时资源（可选）
# ==========================================
echo "🔧 步骤 5: 检查过时资源..."

OLD_RESOURCES=0

# 检查旧的 Bootstrap/jQuery 资源
if [ -d "src/main/resources/www/bootstrap" ]; then
    echo "   ⚠️  发现旧版 Bootstrap 资源: src/main/resources/www/bootstrap/"
    OLD_RESOURCES=$((OLD_RESOURCES + 1))
fi

if [ -d "src/main/resources/www/js" ] && ls src/main/resources/www/js/jquery* 1> /dev/null 2>&1; then
    echo "   ⚠️  发现旧版 jQuery 资源"
    OLD_RESOURCES=$((OLD_RESOURCES + 1))
fi

# 检查多余的 ChromeDriver
DRIVER_COUNT=$(ls -1 src/main/resources/driver/chromedriver*.exe 2>/dev/null | wc -l)
if [ "$DRIVER_COUNT" -gt 3 ]; then
    echo "   ⚠️  发现 $DRIVER_COUNT 个 ChromeDriver 版本，建议只保留最新 2-3 个"
    OLD_RESOURCES=$((OLD_RESOURCES + 1))
fi

if [ "$OLD_RESOURCES" -eq 0 ]; then
    echo "   ✅ 未发现明显的过时资源"
else
    echo "   📝 建议：手动审查并删除过时资源"
fi
echo ""

# ==========================================
# 步骤 6: 文档分类准备
# ==========================================
echo "🔧 步骤 6: 准备文档分类目录..."

if [ -d "docs" ]; then
    mkdir -p docs/architecture
    mkdir -p docs/api
    mkdir -p docs/development
    mkdir -p docs/deployment
    mkdir -p docs/changelog
    
    echo "   ✅ 创建文档分类目录:"
    echo "      - docs/architecture/"
    echo "      - docs/api/"
    echo "      - docs/development/"
    echo "      - docs/deployment/"
    echo "      - docs/changelog/"
    echo ""
    echo "   📝 下一步：手动将现有文档移动到对应分类"
    echo "   📝 参考：docs/architecture/PROJECT-STRUCTURE-OPTIMIZATION.md"
else
    echo "   ⚠️  docs/ 目录不存在"
fi
echo ""

# ==========================================
# 步骤 7: 更新 .gitignore
# ==========================================
echo "🔧 步骤 7: 检查 .gitignore..."

if grep -q "backup-" .gitignore 2>/dev/null; then
    echo "   ✅ backup 目录已在 .gitignore 中"
else
    echo "   📝 建议：在 .gitignore 中添加以下内容："
    echo "      backup-*"
    echo "      target/"
    echo "      frontend/dist/"
    echo "      frontend/node_modules/"
    echo "      *.log"
fi
echo ""

# ==========================================
# 总结
# ==========================================
echo "=========================================="
echo "  ✅ 阶段一优化完成！"
echo "=========================================="
echo ""
echo "📊 优化统计："
echo "   - Docker 文件已整理到 docker/"
echo "   - 脚本文件已整理到 scripts/"
echo "   - 配置文件已整理到 config/"
echo "   - 文档分类目录已创建"
echo ""
echo "📝 后续操作建议："
echo "   1. 检查备份目录: $BACKUP_DIR"
echo "   2. 手动合并 README-Docker.md 到 README.md"
echo "   3. 审查并删除过时资源"
echo "   4. 将文档移动到对应分类目录"
echo "   5. 更新 .gitignore"
echo "   6. 测试项目构建和运行"
echo ""
echo "🧪 验证命令："
echo "   mvn clean package -DskipTests"
echo "   docker-compose -f docker/docker-compose.yml build"
echo ""
echo "⚠️  如果遇到问题，可以从备份恢复："
echo "   rm -rf docker/ scripts/ config/"
echo "   cp -r $BACKUP_DIR/docs ./docs"
echo ""
echo "=========================================="
