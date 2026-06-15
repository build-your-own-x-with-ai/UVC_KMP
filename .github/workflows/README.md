# GitHub Actions 说明

本项目使用 GitHub Actions 自动构建所有平台的可执行文件。

## Workflows

### 1. Build Multi-Platform (`build.yml`)
**触发条件：**
- Push 到 main 分支
- Pull Request
- 手动触发

**构建内容：**
- ✅ macOS DMG
- ✅ Windows MSI
- ✅ Linux DEB
- ✅ Android APK

**Artifacts 保留：** 90 天

### 2. Release (`release.yml`)
**触发条件：**
- 推送 Git tag (如 `v1.0.0`)

**自动发布：**
- 创建 GitHub Release
- 上传所有平台的安装包
- 自动生成 Release Notes

## 使用方法

### 触发构建
```bash
# 推送到 main 分支会自动构建
git push origin main

# 或手动触发（在 GitHub Actions 页面点击 "Run workflow"）
```

### 创建 Release
```bash
# 1. 打标签
git tag v1.0.0
git push origin v1.0.0

# 2. GitHub Actions 会自动：
#    - 构建所有平台
#    - 创建 Release
#    - 上传所有文件
```

### 下载构建产物

**方式 1：GitHub Actions Artifacts**
- 进入 Actions 页面
- 选择对应的 workflow run
- 下载 Artifacts

**方式 2：GitHub Releases**
- 进入 Releases 页面
- 下载对应平台的安装包

## 注意事项

1. **macOS 签名**（可选）
   - 需要 Apple Developer 账号
   - 在 GitHub Secrets 中添加 `APPLE_CERTIFICATE` 和 `APPLE_PASSWORD`

2. **Windows 签名**（可选）
   - 需要代码签名证书
   - 在 GitHub Secrets 中添加证书信息

3. **Android 签名**（可选）
   - 需要 keystore 文件
   - 在 GitHub Secrets 中添加 `ANDROID_KEYSTORE` 和 `KEYSTORE_PASSWORD`

## 构建时间估计

- macOS: ~15 分钟
- Windows: ~20 分钟
- Linux: ~10 分钟
- Android: ~5 分钟

**总计：约 50 分钟（并行构建）**
