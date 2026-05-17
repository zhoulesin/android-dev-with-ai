plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

dependencies {
    implementation(project(":core:domain"))
    // 考题：不得 implementation(project(":core:data"))
}
