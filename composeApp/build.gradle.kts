import com.android.aaptcompiler.parseUiModeType
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.buildConfig)
    id("com.google.gms.google-services") version "4.4.0" apply false
    id("app.cash.sqldelight") version "2.0.2"

}

kotlin {
    androidTarget {
//        @OptIn(ExperimentalKotlinGradlePluginApi::class)
//        compilerOptions {
//            jvmTarget.set(JvmTarget.JVM_11)
//        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            //   export("io.github.mirzemehdi:kmpnotifier:1.4.0")
            baseName = "ComposeApp"
            isStatic = true
            linkerOpts("-lsqlite3", "_sqlite3")
            project.extensions.findByType(KotlinMultiplatformExtension::class.java)?.apply {
                targets
                    .filterIsInstance<KotlinNativeTarget>()
                    .flatMap { it.binaries }
                    .forEach { compilationUnit ->
                        compilationUnit.linkerOpts(
                            "-lsqlite3",
                            "_sqlite3"
                        )
                    }
            }
        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.kermit)
            implementation(libs.kotlinx.coroutines.core)
//            implementation(libs.ktor.client.core)
//            implementation(libs.ktor.client.content.negotiation)
//            implementation(libs.ktor.client.serialization)
//            implementation(libs.ktor.client.logging)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.navigation.composee)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.coil)
            implementation(libs.coil.network.ktor)
            implementation(libs.multiplatformSettings)
            implementation(libs.kotlinx.datetime)
            implementation(libs.composeIcons.featherIcons)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.ktor.client.cio)
            implementation(libs.bundles.ktor)

            implementation(libs.lifecycle.viewmodel.compose)
            //           api(libs.kmpnotifier)
            implementation(libs.kermit)
//            implementation(libs.library.base)

            implementation(libs.coroutines.extensions)
            implementation(libs.stately.common) // Needed by SQLDelight
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
            implementation(libs.kotlinx.coroutines.test)

        }

        androidMain.dependencies {
            implementation(compose.uiTooling)
            implementation(libs.androidx.activityCompose)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)

            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.android.driver)

            // AndroidX
            implementation(libs.androidx.startup.runtime)
            api(libs.kmpnotifier)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.jnativehook)
            implementation(libs.sqlite.driver)

            api(libs.kmpnotifier)
        }

        nativeMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.native.driver)
        }

    }
}
sqldelight {
    databases {
        create("Database") {
            packageName.set("com.example")
        }
    }
}
android {
    namespace = "org.softsuave.bustlespot"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
        targetSdk = 35

        applicationId = "org.softsuave.bustlespot.androidApp"
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        jvmToolchain(21)
    }
}

dependencies {
//    implementation(libs.androidx.startup.runtime)
//    implementation(libs.androidx.room.common)
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "org.softsuave.bustlespot.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Bustlespot"
            packageVersion = "1.0.0"
            vendor = "Soft Suave Technologies"
            modules("java.sql")

            windows {
//                targetFormats(TargetFormat.Exe) Exe if needed
                menu = true
                menuGroup = "KMMDev"
                upgradeUuid = "123e4567-e89b-12d3-a456-426614174010" // Unique ID for the installer
                iconFile.set(project.file("src/commonMain/composeResources/files/app_icon_windows.ico"))
                perUserInstall = false
                shortcut = true
            }

            // macOS configuration
            macOS {
                bundleID = "org.softsuave.bustlespot.app"
                iconFile.set(project.file("src/commonMain/composeResources/files/app_icon_macos.icns"))
            }
            // Linux configuration
            linux {
                iconFile.set(project.file("src/commonMain/composeResources/files/app_icon_linux.png"))
            }
        }
    }
}
