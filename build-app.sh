#!/bin/zsh

echo "🔨 Building signed UVC.app..."

# Build the app
./gradlew :desktopApp:packageDistributionForCurrentOS

# Find the built app
APP_PATH=$(find desktopApp/build/compose/binaries/main -name "*.app" | head -1)

if [ -n "$APP_PATH" ]; then
    echo "✅ Built: $APP_PATH"

    # Create a wrapper script that requests sudo
    cat > "$APP_PATH/Contents/MacOS/UVC-wrapper.sh" <<'EOF'
#!/bin/zsh
osascript -e 'do shell script "\"'$0'/../MacOS/UVC\"" with administrator privileges'
EOF

    chmod +x "$APP_PATH/Contents/MacOS/UVC-wrapper.sh"

    echo "🚀 Run: open '$APP_PATH'"
    echo "   Or: sudo '$APP_PATH/Contents/MacOS/UVC'"

    # Ask user
    echo ""
    read "choice?Launch now with sudo? (y/n): "
    if [[ "$choice" == "y" ]]; then
        sudo "$APP_PATH/Contents/MacOS/UVC"
    fi
else
    echo "❌ Build failed"
    exit 1
fi
