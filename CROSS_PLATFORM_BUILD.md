# 跨平台构建说明

## 平台支持状态

| 平台 | libusb | libuvc | 状态 | 说明 |
|------|--------|--------|------|------|
| **macOS** | ✅ 已包含 | ✅ 已包含 | 完全工作 | 无需额外设置 |
| **Windows** | ⚡ Actions | ⚡ Actions | 自动构建 | GitHub Actions 自动下载 |
| **Linux** | 📦 系统库 | 📦 系统库 | 推荐方式 | sudo apt install |
| **Android** | N/A | N/A | 完全工作 | 使用 USB Host API |

---

## Windows 库获取方式

### 方式 1：GitHub Actions（推荐）
```bash
1. 推送代码到 GitHub
2. 进入 Actions → "Download Native Libraries"
3. 点击 "Run workflow"
4. 等待 ~10 分钟
5. 下载 Windows artifacts
6. 解压到 desktopApp/libs/windows/
```

### 方式 2：本地 vcpkg
```cmd
# 安装 vcpkg
git clone https://github.com/Microsoft/vcpkg.git
cd vcpkg
bootstrap-vcpkg.bat

# 安装库
vcpkg install libusb:x64-windows
vcpkg install libuvc:x64-windows

# 复制 DLL
copy vcpkg\installed\x64-windows\bin\libusb-1.0.dll desktopApp\libs\windows\
copy vcpkg\installed\x64-windows\bin\uvc.dll desktopApp\libs\windows\
```

---

## Linux 库获取方式

### 方式 1：系统包（推荐）
```bash
# Ubuntu/Debian
sudo apt-get install libusb-1.0-0 libuvc0

# Fedora/RHEL
sudo dnf install libusb libuvc

# Arch Linux
sudo pacman -S libusb libuvc
```

### 方式 2：打包到 .deb
```bash
# 运行 GitHub Actions 工作流
# 下载 Linux artifacts
# 复制到 desktopApp/libs/linux/
```

---

## 构建测试

### 本地测试
```bash
# macOS (直接运行)
sudo ./gradlew :desktopApp:run

# Windows (需要 DLL)
./gradlew :desktopApp:run

# Linux (需要系统库)
sudo apt install libusb-1.0-0 libuvc0
./gradlew :desktopApp:run
```

### CI 测试
```bash
# 推送触发自动构建
git push origin main

# 所有平台并行构建
# 约 50 分钟完成
```

---

## 常见问题

### Q: Windows 找不到 libusb-1.0.dll
**A:** 使用 GitHub Actions 自动下载，或本地安装 vcpkg

### Q: Linux 找不到 libuvc.so
**A:** 运行 `sudo apt install libuvc0`

### Q: macOS 需要 sudo 运行
**A:** 这是正常的，USB 原始访问需要 root 权限

---

## 自动化构建

所有平台的库都可以通过 GitHub Actions 自动获取：

1. 推送代码到 GitHub
2. Actions 自动构建所有平台
3. 下载 Artifacts 或等待 PR
4. 本地测试

**无需手动下载任何库！**
