# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Kotlin Multiplatform UVC Camera application targeting Android and Desktop (JVM) with shared Compose Multiplatform UI. Integrates libuvc for USB Video Class 1.5 camera support with H.264, MJPEG, and YUV format handling.

## Build Commands

**Run applications:**
- Desktop: `./gradlew :desktopApp:run`
- Desktop (hot reload): `./gradlew :desktopApp:hotRun --auto`
- Android: `./gradlew :androidApp:assembleDebug`

**Run tests:**
- Desktop/JVM: `./gradlew :shared:jvmTest`
- Android: `./gradlew :shared:testAndroidHostTest`
- Run single test: `./gradlew :shared:jvmTest --tests "com.iosdevlog.uvc.SpecificTest"`

**Build artifacts:**
- Desktop distributable: `./gradlew :desktopApp:packageDistributionForCurrentOS`
- Output: `desktopApp/build/compose/binaries/main/dmg/com.iosdevlog.uvc-1.0.0.dmg`

## Architecture

**Module structure:**
- `:shared` - Multiplatform module containing common code and platform-specific implementations
- `:androidApp` - Android application (depends on :shared)
- `:desktopApp` - Desktop application (depends on :shared)

**Source sets in :shared:**
- `commonMain` - Shared Kotlin code for all platforms
  - `domain/model` - Data models (UVCDevice, VideoFrame, CameraControl, USBPacket)
  - `domain/repository` - Repository interfaces (UVCRepository with expect/actual)
  - `presentation/screens` - Compose UI screens (CameraListScreen, PreviewScreen)
  - `presentation/components` - Shared components (VideoPreview expect/actual composable)
  - `presentation/viewmodel` - ViewModels for state management
- `androidMain` - Android-specific implementations
  - Native JNI code in `cpp/` for libuvc integration
  - Platform-specific decoders (H264Decoder, MJPEGDecoder, YUVConverter)
  - USB packet logging
- `jvmMain` - Desktop/JVM-specific implementations
  - JNA-based libuvc bindings
  - FFmpeg-based video decoding
  - USB packet logging via libusb

**Key patterns:**
- Platform-specific implementations: Define `expect` declarations in `commonMain`, provide `actual` implementations in platform source sets
- Shared UI lives in `commonMain` using Compose Multiplatform
- Platform entry points are in respective app modules (MainActivity.kt for Android, main.kt for Desktop)
- Main package: `com.iosdevlog.uvc`

**Native Integration:**
- Android: Uses JNI with NDK for libuvc/libusb (C++ code in `shared/src/androidMain/cpp/`)
- Desktop: Uses JNA for native library bindings
- Direct integration with pupil-labs/libuvc (no third-party wrappers)

**Dependencies:**
- Uses Gradle version catalogs (`libs.*` references in build files)
- Compose Multiplatform for UI across platforms
- JNA for JVM native bindings
- Desktop main class: `com.iosdevlog.uvc.MainKt`

## Development Notes

**UVC Integration:**
- Direct libuvc integration without third-party Android libraries
- Supports H.264, MJPEG, YUV formats
- Multi-camera support
- Camera controls (exposure, focus, white balance)
- Wireshark-style USB packet logging

**Build Configuration:**
- Android requires NDK for native code compilation
- Desktop bundles native libraries per OS (macOS/Linux/Windows)
- JNA library path configured in desktop JVM args
