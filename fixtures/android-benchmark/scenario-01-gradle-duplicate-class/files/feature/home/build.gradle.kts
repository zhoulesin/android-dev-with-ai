plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

dependencies {
    implementation(project(":core:database"))
    // 传递依赖 com.example.legacy:analytics-sdk → room 2.4.0（与 core:database 2.6.1 冲突）
    implementation(libs.legacy.analytics)
}
