# Windows Native Libraries

由于 Windows 上 libuvc 和 libusb 的构建比较复杂，这里提供两个解决方案：

## 方案 1：使用预编译库（推荐）

### 下载 libusb for Windows
1. 访问：https://github.com/libusb/libusb/releases
2. 下载最新版本（如 libusb-1.0.27.7z）
3. 解压并复制文件：
   ```
   MS64/dll/libusb-1.0.dll → desktopApp/libs/windows/
   ```

### 下载 libuvc for Windows
1. 访问：https://github.com/libuvc/libuvc/releases
2. 或使用 vcpkg 安装：
   ```cmd
   vcpkg install libuvc:x64-windows
   ```
3. 复制 DLL 文件到 `desktopApp/libs/windows/`

## 方案 2：禁用 Windows 版本（临时）

如果暂时不需要 Windows 支持，可以在代码中添加平台检查：

```kotlin
// LibUSBManager.kt
init {
    if (System.getProperty("os.name").contains("Windows")) {
        throw UnsupportedOperationException("Windows support coming soon")
    }
    // ... rest of init
}
```

## 当前状态

- ✅ macOS: 已包含 libusb + libuvc
- ❌ Windows: 需要添加 DLL 文件
- ❌ Linux: 需要添加 .so 文件

## Windows 所需文件

```
desktopApp/libs/windows/
├── libusb-1.0.dll
├── libuvc.dll
└── msvcr120.dll (可能需要)
```

## 构建说明

Windows 用户需要在本地安装：
1. Visual Studio 2019+
2. vcpkg (用于依赖管理)
3. CMake

然后运行：
```cmd
vcpkg install libusb:x64-windows
vcpkg install libuvc:x64-windows
```
