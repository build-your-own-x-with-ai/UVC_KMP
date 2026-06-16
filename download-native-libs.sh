#!/bin/bash

# Download native libraries for Windows and Linux

echo "📦 Downloading native libraries for cross-platform builds..."

# Get project root directory
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
LIBS_DIR="$SCRIPT_DIR/desktopApp/libs"
WINDOWS_DIR="$LIBS_DIR/windows"
LINUX_DIR="$LIBS_DIR/linux"

mkdir -p "$WINDOWS_DIR"
mkdir -p "$LINUX_DIR"

# Download libusb for Windows
echo "Downloading libusb for Windows..."
LIBUSB_VERSION="1.0.27"
LIBUSB_URL="https://github.com/libusb/libusb/releases/download/v${LIBUSB_VERSION}/libusb-${LIBUSB_VERSION}.7z"

TEMP_DIR=$(mktemp -d)
cd "$TEMP_DIR"
curl -L -o libusb.7z "$LIBUSB_URL"

# Extract (requires 7z: brew install p7zip)
if command -v 7z &> /dev/null; then
    7z x libusb.7z
    cp "libusb-${LIBUSB_VERSION}/VS2019/MS64/dll/libusb-1.0.dll" "$WINDOWS_DIR/"
    echo "✅ Windows libusb-1.0.dll downloaded"
else
    echo "⚠️  7z not found. Install with: brew install p7zip"
    exit 1
fi

cd "$SCRIPT_DIR"
rm -rf "$TEMP_DIR"

# For Linux, we can use Docker to get the libraries
echo ""
echo "For Linux libraries, you have two options:"
echo "1. Use system libraries (recommended):"
echo "   sudo apt install libusb-1.0-0 libuvc0"
echo ""
echo "2. Extract from Docker:"
echo "   docker run --rm ubuntu:22.04 bash -c 'apt-get update && apt-get install -y libusb-1.0-0 libuvc0 && tar czf /tmp/libs.tar.gz /usr/lib/x86_64-linux-gnu/libusb-1.0.so* /usr/lib/x86_64-linux-gnu/libuvc.so*' | tar xzf -"
echo ""

# Note about libuvc for Windows
echo "⚠️  Note: libuvc.dll for Windows requires manual build or vcpkg:"
echo "   vcpkg install libuvc:x64-windows"
echo "   Then copy from: vcpkg/installed/x64-windows/bin/uvc.dll"
echo ""

echo "Current status:"
ls -lh "$WINDOWS_DIR/"
ls -lh "$LINUX_DIR/"
