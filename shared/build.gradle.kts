// Debug imports removed — internal AGP APIs no longer available in AGP 9.x

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.lint)
    kotlin("plugin.serialization") version "2.1.10"
    id("com.google.devtools.ksp")
}

//plugins {   TODO
//    alias(libs.plugins.android.application)
//    alias(libs.plugins.kotlin.android)
//    alias(libs.plugins.kotlin.compose)
//}
val signalrkoreVersion = "0.5.4"
kotlin {
    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    android {
        namespace = "com.leafcellteam.mafia"
        compileSdk = 37
        minSdk = 24

        // Обязательно для передачи composeResources (strings/drawable) из
        // библиотечного модуля в потребляющий APK. Без этого assets НЕ попадают
        // в composeApp, и stringResource() падает при чтении ресурсов.
        androidResources.enable = true

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
        val xcfName = "shared"

    iosArm64 {
        binaries.framework {
            baseName = xcfName
            isStatic = true
        }
    }

    @OptIn(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCacheApi::class)
    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
            isStatic = true
            disableNativeCache(org.jetbrains.kotlin.gradle.plugin.mpp.DisableCacheInKotlinVersion.`2_4_0`, "Workaround for JetBrains Navigation Compose compiler crash on iOS Simulator")
        }
    }

    // Делаем package класса Res детерминированным, чтобы не гадать, куда
    // compose resources сгенерируют Res.drawable.* / Res.string.*
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    // Source set declarœations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.signalrkore)
                implementation(libs.kotlin.stdlib)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation("org.jetbrains.compose.material:material-icons-core:1.7.3")
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.datastore.preferences)
                implementation(libs.navigation.compose.multiplatform)

                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.client.logging)
                implementation(libs.napier)
                implementation(libs.kotlinx.datetime)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.ktor.client.okhttp)
                implementation("com.google.android.play:app-update:2.1.0")
                implementation("com.google.android.play:app-update-ktx:2.1.0")

                implementation("androidx.datastore:datastore-preferences:1.1.1")
                implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.4")
                implementation(libs.kotlinx.coroutines.android)
                implementation(libs.accompanist.flowlayout)
                implementation("io.reactivex.rxjava3:rxjava:3.1.11")
                implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
                implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
                implementation(libs.retrofit)
                implementation(libs.converter.gson)
                implementation(libs.androidx.core.ktx)
                implementation(libs.androidx.lifecycle.runtime.ktx)
                implementation(libs.androidx.lifecycle.runtime.compose)
                implementation(libs.androidx.lifecycle.viewmodel.compose)
                implementation(libs.androidx.activity.compose)
                implementation(libs.firebase.perf.ktx)
                implementation(libs.androidx.storage)
                implementation(libs.okhttp)
                implementation(libs.logging.interceptor)
                implementation(libs.androidx.media3.common.ktx)

                getByName("androidHostTest") {
                    implementation(libs.junit)
                }
                getByName("androidDeviceTest") {
                    implementation(libs.androidx.junit)
                    implementation(libs.androidx.espresso.core)
                    implementation(project.dependencies.platform(libs.androidx.compose.bom))
                    // TODO ^^^
                    implementation(libs.androidx.ui.tooling)
                    implementation(libs.androidx.ui.test.manifest)
                    implementation(libs.androidx.ui.test.junit4)
                }

                implementation(libs.androidx.ui.tooling)
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.core)
                implementation(libs.androidx.junit)
                implementation(libs.androidx.runner)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
    }

}

// Явно фиксируем пакет сгенерированного Res-класса (Res.drawable.*, Res.string.*),
// чтобы он не "плавал" в зависимости от group/name проекта и чтобы код,
// написанный руками (import com.leafcellteam.mafia.resources.Res), точно совпадал
// с тем, что реально сгенерирует Gradle-плагин.
compose.resources {
    packageOfResClass = "com.leafcellteam.mafia.resources"
    generateResClass = always
}