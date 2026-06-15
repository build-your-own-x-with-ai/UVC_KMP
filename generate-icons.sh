#!/bin/bash

# UVC Camera Icon Generator
# Generates icons for all platforms (macOS, Windows, Linux, Android)

echo "🎨 Generating UVC Camera icons..."

# Base icon size
BASE_SIZE=1024

# Check if ImageMagick is installed
if ! command -v convert &> /dev/null; then
    echo "❌ ImageMagick not found. Installing..."
    echo "   macOS: brew install imagemagick"
    echo "   Ubuntu: sudo apt install imagemagick"
    echo "   Windows: choco install imagemagick"
    exit 1
fi

# Create output directories
mkdir -p desktopApp/icons
mkdir -p androidApp/src/main/res/mipmap-mdpi
mkdir -p androidApp/src/main/res/mipmap-hdpi
mkdir -p androidApp/src/main/res/mipmap-xhdpi
mkdir -p androidApp/src/main/res/mipmap-xxhdpi
mkdir -p androidApp/src/main/res/mipmap-xxxhdpi

# Create base 1024x1024 icon
echo "Creating base icon..."
convert -size ${BASE_SIZE}x${BASE_SIZE} xc:none \
  -fill "#4A90E2" \
  -draw "roundrectangle 50,50 974,974 120,120" \
  -fill white \
  -font Helvetica-Bold -pointsize 200 \
  -gravity center -annotate +0-80 "UVC" \
  -fill white -stroke white -strokewidth 8 \
  -draw "circle 512,680 512,560" \
  -fill none \
  -draw "circle 512,680 512,600" \
  /tmp/icon_base.png

echo "✅ Base icon created"

# macOS .icns (requires iconutil on macOS)
if [[ "$OSTYPE" == "darwin"* ]]; then
    echo "Creating macOS .icns..."
    mkdir -p /tmp/UVC.iconset

    sips -z 16 16     /tmp/icon_base.png --out /tmp/UVC.iconset/icon_16x16.png
    sips -z 32 32     /tmp/icon_base.png --out /tmp/UVC.iconset/icon_16x16@2x.png
    sips -z 32 32     /tmp/icon_base.png --out /tmp/UVC.iconset/icon_32x32.png
    sips -z 64 64     /tmp/icon_base.png --out /tmp/UVC.iconset/icon_32x32@2x.png
    sips -z 128 128   /tmp/icon_base.png --out /tmp/UVC.iconset/icon_128x128.png
    sips -z 256 256   /tmp/icon_base.png --out /tmp/UVC.iconset/icon_128x128@2x.png
    sips -z 256 256   /tmp/icon_base.png --out /tmp/UVC.iconset/icon_256x256.png
    sips -z 512 512   /tmp/icon_base.png --out /tmp/UVC.iconset/icon_256x256@2x.png
    sips -z 512 512   /tmp/icon_base.png --out /tmp/UVC.iconset/icon_512x512.png
    sips -z 1024 1024 /tmp/icon_base.png --out /tmp/UVC.iconset/icon_512x512@2x.png

    iconutil -c icns /tmp/UVC.iconset -o desktopApp/icons/icon.icns
    echo "✅ macOS .icns created"
fi

# Windows .ico
echo "Creating Windows .ico..."
convert /tmp/icon_base.png -define icon:auto-resize=256,128,64,48,32,16 desktopApp/icons/icon.ico
echo "✅ Windows .ico created"

# Linux .png
echo "Creating Linux .png..."
convert /tmp/icon_base.png -resize 512x512 desktopApp/icons/icon.png
echo "✅ Linux .png created"

# Android mipmap icons
echo "Creating Android icons..."
convert /tmp/icon_base.png -resize 48x48   androidApp/src/main/res/mipmap-mdpi/ic_launcher.png
convert /tmp/icon_base.png -resize 72x72   androidApp/src/main/res/mipmap-hdpi/ic_launcher.png
convert /tmp/icon_base.png -resize 96x96   androidApp/src/main/res/mipmap-xhdpi/ic_launcher.png
convert /tmp/icon_base.png -resize 144x144 androidApp/src/main/res/mipmap-xxhdpi/ic_launcher.png
convert /tmp/icon_base.png -resize 192x192 androidApp/src/main/res/mipmap-xxxhdpi/ic_launcher.png

# Round icons for Android
convert /tmp/icon_base.png -resize 48x48   \( +clone -threshold -1 -negate -fill white -draw "circle 24,24 24,0" \) -alpha off -compose copy_opacity -composite androidApp/src/main/res/mipmap-mdpi/ic_launcher_round.png
convert /tmp/icon_base.png -resize 72x72   \( +clone -threshold -1 -negate -fill white -draw "circle 36,36 36,0" \) -alpha off -compose copy_opacity -composite androidApp/src/main/res/mipmap-hdpi/ic_launcher_round.png
convert /tmp/icon_base.png -resize 96x96   \( +clone -threshold -1 -negate -fill white -draw "circle 48,48 48,0" \) -alpha off -compose copy_opacity -composite androidApp/src/main/res/mipmap-xhdpi/ic_launcher_round.png
convert /tmp/icon_base.png -resize 144x144 \( +clone -threshold -1 -negate -fill white -draw "circle 72,72 72,0" \) -alpha off -compose copy_opacity -composite androidApp/src/main/res/mipmap-xxhdpi/ic_launcher_round.png
convert /tmp/icon_base.png -resize 192x192 \( +clone -threshold -1 -negate -fill white -draw "circle 96,96 96,0" \) -alpha off -compose copy_opacity -composite androidApp/src/main/res/mipmap-xxxhdpi/ic_launcher_round.png

echo "✅ Android icons created"

echo ""
echo "🎉 All icons generated successfully!"
echo ""
echo "Generated files:"
echo "  📁 desktopApp/icons/"
echo "     - icon.icns (macOS)"
echo "     - icon.ico (Windows)"
echo "     - icon.png (Linux)"
echo "  📁 androidApp/src/main/res/mipmap-*/"
echo "     - ic_launcher.png"
echo "     - ic_launcher_round.png"
echo ""
echo "Next steps:"
echo "  1. Run: ./generate-icons.sh"
echo "  2. Commit the icons"
echo "  3. Rebuild the apps"
