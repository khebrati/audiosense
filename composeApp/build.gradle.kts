import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
import java.util.Properties

val IS_DEVELOPMENT = true

// Load local.properties for secrets
val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(localPropertiesFile.inputStream())
    }
}

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.sqldelight)
}

kotlin {
    androidTarget()
    js { browser() }
    compilerOptions {
        optIn.add("kotlin.time.ExperimentalTime")
        optIn.add("androidx.compose.material3.ExperimentalMaterial3Api")
        optIn.add("androidx.compose.material3.ExperimentalMaterial3ExpressiveApi")
        optIn.add("androidx.compose.material3.ExperimentalMaterial3ExpressiveApi")
        optIn.add("androidx.compose.ui.ExperimentalComposeUiApi")
    }
    jvmToolchain(21)

    applyDefaultHierarchyTemplate()
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.components.resources)
            implementation(libs.material3)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)
            implementation(libs.kermit)
            implementation(libs.kermit.koin)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime)
            implementation(libs.androidx.navigation.compose)
//            implementation(libs.androidx.graphics.shapes)
            implementation(libs.kotlinx.datetime)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.viewmodel.navigation)
            implementation(libs.kotlinx.serialization.json)
            //todo back handler
//            implementation(libs.back.handler)
            implementation(libs.coil)
            implementation(libs.coil.svg)
            implementation(libs.ktor.core)
            implementation(libs.ktor.cio)
            // SqlDelight
            implementation("app.cash.sqldelight:coroutines-extensions:${libs.versions.sqldelight.get()}")
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
        }

        androidMain.dependencies {
            implementation(compose.uiTooling)
            implementation(libs.androidx.activityCompose)
            implementation(libs.kotlinx.coroutines.android)
            // SqlDelight Android driver
            implementation("app.cash.sqldelight:android-driver:${libs.versions.sqldelight.get()}")
        }


        androidInstrumentedTest.dependencies {
            implementation(libs.androidx.runner)
        }
        sourceSets.jsMain.dependencies {
            implementation("app.cash.sqldelight:web-worker-driver:2.2.1")
            implementation(devNpm("copy-webpack-plugin", "9.1.0"))
            implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.2.1"))
            implementation(npm("sql.js", "1.8.0"))
        }

    }
}

android {
    namespace = "ir.khebrati.audiosense"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
        targetSdk = 35

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

//https://developer.android.com/develop/ui/compose/testing#setup
dependencies {
    androidTestImplementation(libs.androidx.uitest.junit4)
    debugImplementation(libs.androidx.uitest.testManifest)
    testImplementation(libs.junit)
}


compose.resources{
    generateResClass = always
}

buildConfig {
    buildConfigField("IS_DEVELOPMENT",IS_DEVELOPMENT)
    buildConfigField("API_USERNAME", localProperties.getProperty("API_USERNAME", ""))
    buildConfigField("API_PASSWORD", localProperties.getProperty("API_PASSWORD", ""))
}

sqldelight {
    databases {
        create("AudiosenseDb") {
            packageName.set("ir.khebrati.audiosense.db")
            generateAsync.set(true)
        }
    }
}
