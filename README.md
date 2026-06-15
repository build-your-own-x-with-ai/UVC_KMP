# UVC Camera - Kotlin Multiplatform Application

> 跨平台 UVC 摄像头应用，支持 Desktop (macOS/Windows/Linux) 和 Android

[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.0-blue.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Compose-1.7.1-blue.svg)](https://www.jetbrains.com/lp/compose-multiplatform)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

## ✨ 功能特性

### Desktop 版本
- ✅ **实时视频预览** - H.264/MJPEG/YUV 格式支持
- ✅ **截图功能** - 📷 保存当前帧为 PNG
- ✅ **视频录制** - ⏺️ 录制 H.264 视频流
- ✅ **相机控制** - ⚙️ 亮度/对比度/饱和度/白平衡/曝光/增益/锐度/伽马
- ✅ **多分辨率** - 支持 1280x480, 1920x1080, 1280x720, 640x480
- ✅ **格式切换** - H.264, MJPEG, YUV

### Android 版本
- ✅ **USB Host 支持** - 通过 USB OTG 连接相机
- ✅ **硬件解码** - MediaCodec H.264 加速
- ✅ **相同的 UI** - 与 Desktop 100% 一致
- ✅ **MediaStore 集成** - 保存到相册

## 🏗️ 架构

```
UVC/
├── shared/                    # 共享模块（70% 代码复用）
│   ├── commonMain/           # 跨平台代码
│   │   ├── domain/          # 业务逻辑
│   │   └── presentation/    # Compose UI
│   ├── jvmMain/             # Desktop 实现
│   └── androidMain/         # Android 实现
├── desktopApp/              # Desktop 应用
└── androidApp/              # Android 应用
```

## 🚀 快速开始

### Desktop (macOS)

**运行：**
```bash
sudo ./gradlew :desktopApp:hotRun --auto
```

**打包：**
```bash
./gradlew :desktopApp:packageDmg
# 输出: desktopApp/build/compose/binaries/main/dmg/UVC-1.0.0.dmg
```

### Android

**构建 APK：**
```bash
./gradlew :androidApp:assembleDebug
# 输出: androidApp/build/outputs/apk/debug/androidApp-debug.apk
```

**安装：**
```bash
adb install androidApp/build/outputs/apk/debug/androidApp-debug.apk
```

**需求：**
- Android 7.0+ (API 24+)
- USB OTG 支持
- UVC 摄像头

## 📦 下载

### macOS
- [UVC-1.0.0.dmg](desktopApp/build/compose/binaries/main/dmg/)

### Windows
- UVC-1.0.0.msi (待构建)

### Linux
- UVC-1.0.0.deb (待构建)

### Android
- [androidApp-debug.apk](androidApp/build/outputs/apk/debug/)

## 🛠️ 技术栈

| 技术 | 用途 |
|------|------|
| Kotlin Multiplatform | 跨平台代码共享 |
| Compose Multiplatform | 跨平台 UI |
| libuvc + libusb | Desktop USB 视频 |
| JNA | Java Native Access |
| FFmpeg | Desktop H.264 解码 |
| Android USB Host API | Android USB 访问 |
| MediaCodec | Android 硬件解码 |

## 📝 开发

### 环境要求

**Desktop:**
- JDK 17+
- Gradle 8.5+
- macOS: Xcode Command Line Tools
- Windows: Visual Studio 2019+
- Linux: GCC 9+

**Android:**
- Android Studio Hedgehog+
- Android SDK 34
- NDK (可选)

### 构建

```bash
# 清理
./gradlew clean

# Desktop
./gradlew :desktopApp:run

# Android
./gradlew :androidApp:assembleDebug

# 全平台打包
./build-all.sh
```

## 🤝 贡献

欢迎 PR！请确保：
1. 代码符合 Kotlin 风格指南
2. 添加必要的测试
3. 更新相关文档

## 📄 许可证

Apache License 2.0 - 详见 [LICENSE](LICENSE)

## 🙏 致谢

- [libuvc](https://github.com/libuvc/libuvc) - UVC 协议实现
- [libusb](https://github.com/libusb/libusb) - USB 设备访问
- [JavaCV](https://github.com/bytedeco/javacv) - FFmpeg 绑定
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform) - 跨平台 UI 框架

## 📧 联系

- GitHub: [@iosdevlog](https://github.com/iosdevlog)
- Email: iosdevlog@iosdevlog.com

---

**Built with ❤️ using Kotlin Multiplatform**
