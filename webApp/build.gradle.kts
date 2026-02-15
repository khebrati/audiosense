plugins {
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    js {
        browser()
        binaries.executable()
    }


    sourceSets {
        commonMain.dependencies {
            implementation(project(":composeApp"))
        }
    }
}
