#!/bin/zsh

# 1. 停止所有 Gradle 进程
./gradlew --stop

# 2. 删除所有 root 拥有的文件
sudo rm -rf shared/build
sudo rm -rf desktopApp/build
sudo rm -rf build
sudo rm -rf .gradle
sudo rm -rf .gradle_root_backup
sudo rm -rf build_root_backup_top

# 3. 修复所有权限
sudo chown -R i:staff /Users/i/AndroidStudioProjects/UVC

# 4. 清理 Gradle 缓存
./gradlew clean

# 5. 用 sudo 构建并运行
sudo ./gradlew :desktopApp:hotRun
