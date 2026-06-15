import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

dependencies {
    implementation(projects.shared)

    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutinesSwing)

    implementation(libs.compose.uiToolingPreview)
}

compose.desktop {
    application {
        mainClass = "com.iosdevlog.uvc.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "UVC"
            packageVersion = "1.0.0"
            description = "UVC Camera Application - Kotlin Multiplatform"
            vendor = "iosdevlog"

            macOS {
                iconFile.set(project.file("icons/icon.icns"))
                // Include macOS native libraries
                appResourcesRootDir.set(project.layout.projectDirectory.dir("libs/macos"))
            }
            windows {
                iconFile.set(project.file("icons/icon.ico"))
                // Include Windows native libraries
                appResourcesRootDir.set(project.layout.projectDirectory.dir("libs/windows"))
            }
            linux {
                iconFile.set(project.file("icons/icon.png"))
                // Include Linux native libraries
                appResourcesRootDir.set(project.layout.projectDirectory.dir("libs/linux"))
            }

            includeAllModules = true
        }

        // Note: JVM args removed - libraries should be loaded from bundled resources
    }
}