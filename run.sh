#!/bin/zsh

echo "🔨 Building UVC application..."
./gradlew :desktopApp:packageDistributionForCurrentOS

# Find the built app
APP_PATH=$(find desktopApp/build/compose/binaries/main -name "*.app" | head -1)

if [ -z "$APP_PATH" ]; then
    echo "❌ App not found, trying DMG..."
    DMG_PATH=$(find desktopApp/build/compose/binaries/main -name "*.dmg" | head -1)
    if [ -n "$DMG_PATH" ]; then
        echo "✅ Built DMG: $DMG_PATH"
        echo "📦 Mount and run manually with sudo"
        open "$DMG_PATH"
    else
        echo "❌ Build failed - no app or DMG found"
        exit 1
    fi
else
    echo "✅ Built app: $APP_PATH"
    echo "🚀 Running with sudo (required for USB access)..."
    # Run with sudo for USB access
    sudo "$APP_PATH/Contents/MacOS/UVC"
fi
