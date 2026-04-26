# GitHub仓库更名操作指南

## 📋 快速操作步骤

### 第1步：在GitHub上重命名仓库

1. 访问仓库设置页面：
   ```
   https://github.com/cruise991/AbsAdmin/settings
   ```

2. 找到 "Repository name" 输入框

3. 将名称从 `AbsAdmin` 修改为 `EduFlow-Platform`

4. 点击 "Rename" 按钮确认

5. ✅ GitHub会自动：
   - 创建从旧URL到新URL的重定向
   - 更新所有内部链接
   - 保留所有Stars、Issues和PRs

---

### 第2步：验证重定向

访问旧URL确认重定向是否生效：
```
https://github.com/cruise991/AbsAdmin
```

应该自动跳转到：
```
https://github.com/cruise991/EduFlow-Platform
```

---

### 第3步：更新本地Git远程地址

```bash
# 查看当前远程地址
git remote -v

# 更新远程地址为新名称
git remote set-url origin https://github.com/cruise991/EduFlow-Platform.git

# 验证更新
git remote -v
```

---

### 第4步：推送测试

```bash
# 进行一次简单的提交测试
echo "# Test" >> test.txt
git add test.txt
git commit -m "test: verify new repository name"
git push origin main

# 删除测试文件
git rm test.txt
git commit -m "chore: remove test file"
git push origin main
```

---

## ⚠️ 重要提示

### GitHub自动处理的事项
✅ URL重定向（旧链接仍然有效）  
✅ Clone地址更新  
✅ SSH和HTTPS访问  
✅ GitHub Pages（如果有）  
✅ Wiki（如果有）  

### 需要手动处理的事项
❌ 本地Git配置  
❌ CI/CD配置文件中的仓库引用  
❌ 第三方服务集成（如Travis CI、CircleCI等）  
❌ 文档中的硬编码链接  

---

## 🔧 后续优化建议

### 1. 更新README中的仓库链接

在README.md中确保所有链接使用新名称：

```markdown
<!-- 之前 -->
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

<!-- 之后 - 保持不变，因为相对路径不受影响 -->
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
```

### 2. 添加更名公告

在README顶部添加：

```markdown
> **📢 Note**: This repository has been renamed from `AbsAdmin` to `EduFlow-Platform`. 
> All old links will continue to work through GitHub's automatic redirection.
```

### 3. 更新pom.xml（可选）

```xml
<artifactId>eduflow-platform</artifactId>
<name>EduFlow Platform</name>
<description>A modern intelligent education management platform</description>
```

---

## 📊 更名后的检查清单

- [ ] GitHub仓库名称已更新
- [ ] 旧URL重定向正常工作
- [ ] 本地Git远程地址已更新
- [ ] 能够正常push/pull代码
- [ ] README中的链接已检查
- [ ] CI/CD配置已更新（如有）
- [ ] 团队成员已通知
- [ ] 文档已更新

---

## 🆘 常见问题

### Q1: 更名会影响现有的Stars和Forks吗？
**A**: 不会。所有Stars、Forks、Issues和Pull Requests都会保留。

### Q2: 其他人clone的旧仓库会失效吗？
**A**: 不会。GitHub会自动重定向，但建议他们更新远程地址。

### Q3: 可以再次改回原名吗？
**A**: 可以，但不建议频繁更名，会影响SEO和用户信任。

### Q4: 需要重新生成SSH Key吗？
**A**: 不需要。SSH Key与仓库名称无关。

### Q5: GitHub Pages会受影响吗？
**A**: 如果使用了自定义域名，不受影响。如果使用github.io域名，URL会自动更新。

---

## 📞 需要帮助？

如果在更名过程中遇到问题：

1. 查阅GitHub官方文档：
   https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/managing-repository-settings/renaming-a-repository

2. 联系GitHub支持：
   https://support.github.com/contact

3. 查看本项目Issues：
   https://github.com/cruise991/EduFlow-Platform/issues

---

**最后更新**: 2026-04-24
