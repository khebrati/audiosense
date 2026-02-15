import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.application)
}
android {
    namespace = "ir.khebrati.audiosense"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
        targetSdk = 35

        applicationId = "ir.khebrati.audiosense.androidApp"
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}
//kotlin {
//    compilerOptions { jvmTarget.set(JvmTarget.JVM_21) }
//}
dependencies {
    implementation(project(":composeApp"))
    implementation(libs.androidx.activityCompose)
//    implementation(compose.uiTooling)
    implementation(libs.androidx.activityCompose)
    implementation(libs.kotlinx.coroutines.android)
    androidTestImplementation(libs.androidx.uitest.junit4)
    androidTestImplementation(libs.androidx.runner)
    debugImplementation(libs.androidx.uitest.testManifest)
    testImplementation(libs.junit)
//    with(libs.room.compiler) {
//        add("kspAndroid", this)
//    }
}


