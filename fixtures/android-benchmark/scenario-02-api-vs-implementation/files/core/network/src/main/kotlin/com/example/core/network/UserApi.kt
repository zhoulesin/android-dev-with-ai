package com.example.core.network

import retrofit2.http.GET

/** Public API：调用方会间接依赖 Retrofit 注解与类型 */
interface UserApi {
    @GET("users/me")
    suspend fun getMe(): UserDto
}

data class UserDto(val id: String, val name: String)
