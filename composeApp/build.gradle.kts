plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    // Другой namespace/applicationId, чем у :app — специально, чтобы старое
    // (стабильное) и новое (CMP) приложения можно было держать на телефоне
    // одновременно и сравнивать. Перед релизом просто поменяешь на
    // "com.leafcellteam.mafia" и уберёшь модуль :app.
    namespace = "com.leafcellteam.mafia.compose"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.leafcellteam.mafia.compose"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "0.1-cmp"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
    lint {
        abortOnError = false
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}

// Compose resources живут в :shared (commonMain), но Compose-плагин (org.jetbrains.compose)
// в этом модуле не применяется, поэтому shared НЕ упаковывает их в android assets APK
// автоматически. Чтобы stringResource()/painterResource() на Android не падали на
// "No instrumentation registered", вручную раскладываем уже подготовленные shared-ресурсы
// в assets по пути, который ожидает сгенерированный Res-код:
//   assets/composeResources/com.leafcellteam.mafia.resources/...

val sharedPreparedResources =
    layout.projectDirectory.dir("../shared/build/generated/compose/resourceGenerator/preparedResources/commonMain/composeResources")

// Папка, куда будет собрана структура assets (с пакетом в пути).
// Используем статичный File (не Provider), чтобы AGP позволил добавить её в srcDir.
val composeAssetsDir = layout.buildDirectory.dir("generated/composeResources/composeResources").get().asFile

// Задача: раскладывает подготовленные ресурсы shared в assets с нужным путём пакета.
val prepareComposeResourcesAssets = tasks.register<Sync>("prepareComposeResourcesAssets") {
    from(sharedPreparedResources)
    into(file("$composeAssetsDir/com.leafcellteam.mafia.resources"))
    // Ресурсы генерируются в :shared — объявляем явную зависимость.
    dependsOn(
        ":shared:prepareComposeResourcesTaskForCommonMain",
        ":shared:convertXmlValueResourcesForCommonMain",
        ":shared:copyNonXmlValueResourcesForCommonMain",
    )
}

// Добавляем папку с assets в main source set — статичный путь, разрешённый AGP.
// Родитель build/generated/composeResources содержит вложенную папку
// composeResources/..., поэтому в assets уйдёт путь composeResources/...
val composeAssetsRoot = layout.buildDirectory.dir("generated/composeResources").get().asFile
android {
    sourceSets["main"].assets.srcDir(composeAssetsRoot)
}

// Гарантируем, что assets подготовятся до старта сборки.
tasks.named("preBuild") {
    dependsOn(prepareComposeResourcesAssets)
}

dependencies {
    implementation(project(":shared"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    debugImplementation(libs.androidx.ui.tooling)
}