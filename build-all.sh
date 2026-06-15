#!/bin/bash

echo "🔨 Building UVC for all platforms..."

# macOS
echo "📦 Building macOS DMG..."
./gradlew :desktopApp:packageDmg

# Windows (需要在 Windows 上运行)
# echo "📦 Building Windows MSI..."
# ./gradlew :desktopApp:packageMsi

# Linux (需要在 Linux 上运行)
# echo "📦 Building Linux DEB..."
# ./gradlew :desktopApp:packageDeb

echo "✅ Build complete!"
echo "📂 Output: desktopApp/build/compose/binaries/main/"
ls -lh desktopApp/build/compose/binaries/main/dmg/
