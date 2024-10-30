package com.pandroid.greenhaven.domain.repo

import kotlinx.coroutines.flow.Flow

interface UserPrefRepo {
    suspend fun saveCredentials(email: String, password: String)
    suspend fun loadCredentials(): Pair<String, String>?

    suspend fun saveToken(idToken: String)
    suspend fun loadToken(): String?


    fun isFirstTime(): Flow<Boolean>
    suspend fun setFirstTime(isFirstTime: Boolean)

}
