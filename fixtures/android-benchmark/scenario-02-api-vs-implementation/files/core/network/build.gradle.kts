plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

dependencies {
    // ❌ 考题：UserApi 作为 public API 暴露了 Retrofit 类型，但 retrofit 未 api 暴露
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
}
