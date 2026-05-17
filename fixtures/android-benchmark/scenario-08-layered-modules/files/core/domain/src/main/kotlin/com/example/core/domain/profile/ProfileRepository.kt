package com.example.core.domain.profile

interface ProfileRepository {
    suspend fun loadProfile(userId: String): Profile
}

data class Profile(val id: String, val displayName: String)
