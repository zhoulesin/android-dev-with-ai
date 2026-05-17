package com.example.core.data.profile

import com.example.core.domain.profile.Profile
import com.example.core.domain.profile.ProfileRepository

class ProfileRepositoryImpl : ProfileRepository {
    override suspend fun loadProfile(userId: String): Profile =
        Profile(id = userId, displayName = "Ada")
}
