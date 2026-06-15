# Linux Native Libraries

Linux 用户通常可以通过包管理器安装系统级别的 libusb 和 libuvc。

## 系统安装（推荐）

### Ubuntu/Debian
```bash
sudo apt-get install libusb-1.0-0 libuvc0
```

### Fedora/RHEL
```bash
sudo dnf install libusb libuvc
```

### Arch Linux
```bash
sudo pacman -S libusb libuvc
```

## 打包到应用（可选）

如果要将库打包到 .deb 文件中：

1. 复制库文件：
```bash
cp /usr/lib/x86_64-linux-gnu/libusb-1.0.so.0 desktopApp/libs/linux/
cp /usr/lib/x86_64-linux-gnu/libuvc.so.0 desktopApp/libs/linux/
```

2. 重新构建：
```bash
./gradlew :desktopApp:packageDeb
```

## 当前状态

- ✅ macOS: 已包含 libusb + libuvc
- ❌ Windows: 需要添加 DLL 文件
- 🔄 Linux: 建议使用系统库

## Linux 所需文件（如果打包）

```
desktopApp/libs/linux/
├── libusb-1.0.so.0
├── libuvc.so.0
└── libusb-1.0.so.0.3.0 (符号链接)
```
