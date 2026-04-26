#!/bin/bash

# 文档分类整理脚本
# 将 docs/ 目录中的文档移动到对应的分类子目录

set -e

echo "=========================================="
echo "  EduFlow Platform 文档分类整理"
echo "=========================================="
echo ""

DOCS_DIR="docs"

# 检查 docs 目录是否存在
if [ ! -d "$DOCS_DIR" ]; then
    echo "❌ 错误：docs 目录不存在"
    exit 1
fi

# 确保分类目录存在
mkdir -p $DOCS_DIR/architecture
mkdir -p $DOCS_DIR/api
mkdir -p $DOCS_DIR/development
mkdir -p $DOCS_DIR/deployment
mkdir -p $DOCS_DIR/changelog

echo "📋 开始分类整理文档..."
echo ""

# ==========================================
# 架构设计文档
# ==========================================
echo "📐 分类：架构设计 (architecture/)"

ARCHITECTURE_FILES=(
    "ISSUE-ANALYSIS-AND-SOLUTION.md"
    "方案A修复完成报告.md"
)

for file in "${ARCHITECTURE_FILES[@]}"; do
    if [ -f "$DOCS_DIR/$file" ]; then
        mv "$DOCS_DIR/$file" "$DOCS_DIR/architecture/"
        echo "   ✅ $file"
    fi
done
echo ""

# ==========================================
# API 文档
# ==========================================
echo "🔌 分类：API 文档 (api/)"

API_FILES=(
    "FRONTEND-IMPLEMENTATION-GUIDE.md"
    "FRONTEND-IMPLEMENTATION-SUMMARY.md"
)

for file in "${API_FILES[@]}"; do
    if [ -f "$DOCS_DIR/$file" ]; then
        mv "$DOCS_DIR/$file" "$DOCS_DIR/api/"
        echo "   ✅ $file"
    fi
done
echo ""

# ==========================================
# 开发文档
# ==========================================
echo "💻 分类：开发文档 (development/)"

DEVELOPMENT_FILES=(
    "DEVELOPMENT-PROGRESS-2026-04-26.md"
    "TASK-COMPLETION-REPORT.md"
    "PHASE1-COMPLETION-REPORT.md"
    "PHASE2-COMPLETION-REPORT.md"
    "PHASE3-IMPLEMENTATION-REPORT.md"
    "PHASE3-DEVELOPMENT-PLAN.md"
)

for file in "${DEVELOPMENT_FILES[@]}"; do
    if [ -f "$DOCS_DIR/$file" ]; then
        mv "$DOCS_DIR/$file" "$DOCS_DIR/development/"
        echo "   ✅ $file"
    fi
done
echo ""

# ==========================================
# 部署文档
# ==========================================
echo "🚀 分类：部署文档 (deployment/)"

DEPLOYMENT_FILES=(
    "DOCKER-DEPLOYMENT.md"
    "DEPLOYMENT-SUCCESS.md"
    "DEPLOYMENT-STATUS.md"
    "DEPLOYMENT-SUMMARY.md"
    "PHASE3-DEPLOYMENT-COMPLETE.md"
    "README-Docker.md"
)

for file in "${DEPLOYMENT_FILES[@]}"; do
    if [ -f "$DOCS_DIR/$file" ]; then
        mv "$DOCS_DIR/$file" "$DOCS_DIR/deployment/"
        echo "   ✅ $file"
    fi
done
echo ""

# ==========================================
# 变更日志和 Bug 修复
# ==========================================
echo "📝 分类：变更日志 (changelog/)"

CHANGELOG_FILES=(
    "v3.0-RELEASE-NOTES.md"
    "CONTENT-MODULE-FIX-REPORT.md"
    "RESPONSE-PARSING-FIX-REPORT.md"
    "COMPREHENSIVE-RESPONSE-FIX-REPORT.md"
    "AI-FIX-COMPLETE.md"
    "BUGFIX-API-REQUEST-FAILURE.md"
    "BUGFIX-COMPLETE.md"
    "BUGFIX-DOCKER-DEPLOYMENT.md"
    "LOGIN-FIX-DETAILS.md"
    "LOGIN-FIX-GUIDE.md"
)

for file in "${CHANGELOG_FILES[@]}"; do
    if [ -f "$DOCS_DIR/$file" ]; then
        mv "$DOCS_DIR/$file" "$DOCS_DIR/changelog/"
        echo "   ✅ $file"
    fi
done
echo ""

# ==========================================
# 其他文档（保留在根目录或单独分类）
# ==========================================
echo "📋 分类：其他文档"

OTHER_FILES=(
    "PROJECT-NAME-OPTIMIZATION.md"
    "RENAME-COMPLETION-REPORT.md"
    "GITHUB-RENAME-GUIDE.md"
    "GITHUB-UPLOAD-SUCCESS.md"
    "QUICK-TEST-GUIDE.md"
    "QUICK-REFERENCE.md"
    "PHASE3-FUNCTIONAL-TEST-REPORT.md"
    "DOCS-DIRECTORY-REMOVED.md"
)

for file in "${OTHER_FILES[@]}"; do
    if [ -f "$DOCS_DIR/$file" ]; then
        # 测试相关的放到 development
        if [[ "$file" == *"TEST"* ]]; then
            mv "$DOCS_DIR/$file" "$DOCS_DIR/development/"
            echo "   ✅ $file → development/"
        # GitHub 相关的放到 development
        elif [[ "$file" == *"GITHUB"* ]] || [[ "$file" == *"RENAME"* ]]; then
            mv "$DOCS_DIR/$file" "$DOCS_DIR/development/"
            echo "   ✅ $file → development/"
        else
            mv "$DOCS_DIR/$file" "$DOCS_DIR/development/"
            echo "   ✅ $file → development/"
        fi
    fi
done
echo ""

# ==========================================
# 统计结果
# ==========================================
echo "=========================================="
echo "  ✅ 文档分类整理完成！"
echo "=========================================="
echo ""

# 统计各分类文档数量
echo "📊 分类统计："
echo "   architecture/: $(ls -1 $DOCS_DIR/architecture/*.md 2>/dev/null | wc -l | tr -d ' ') 个文档"
echo "   api/: $(ls -1 $DOCS_DIR/api/*.md 2>/dev/null | wc -l | tr -d ' ') 个文档"
echo "   development/: $(ls -1 $DOCS_DIR/development/*.md 2>/dev/null | wc -l | tr -d ' ') 个文档"
echo "   deployment/: $(ls -1 $DOCS_DIR/deployment/*.md 2>/dev/null | wc -l | tr -d ' ') 个文档"
echo "   changelog/: $(ls -1 $DOCS_DIR/changelog/*.md 2>/dev/null | wc -l | tr -d ' ') 个文档"
echo ""

TOTAL_CATEGORIZED=$((
    $(ls -1 $DOCS_DIR/architecture/*.md 2>/dev/null | wc -l) +
    $(ls -1 $DOCS_DIR/api/*.md 2>/dev/null | wc -l) +
    $(ls -1 $DOCS_DIR/development/*.md 2>/dev/null | wc -l) +
    $(ls -1 $DOCS_DIR/deployment/*.md 2>/dev/null | wc -l) +
    $(ls -1 $DOCS_DIR/changelog/*.md 2>/dev/null | wc -l)
))

REMAINING=$(ls -1 $DOCS_DIR/*.md 2>/dev/null | wc -l | tr -d ' ')

echo "   已分类：$TOTAL_CATEGORIZED 个文档"
echo "   剩余：$REMAINING 个文档（docs/README.md 等）"
echo ""

if [ "$REMAINING" -gt 0 ]; then
    echo "⚠️  剩余未分类的文档："
    ls -1 $DOCS_DIR/*.md 2>/dev/null | xargs -n1 basename
    echo ""
fi

echo "📝 下一步："
echo "   1. 检查分类是否正确"
echo "   2. 更新 docs/README.md 中的链接"
echo "   3. 提交到 Git"
echo ""
echo "=========================================="
