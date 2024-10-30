package com.pandroid.greenhaven.domain.repo

import android.net.Uri
import com.pandroid.greenhaven.data.resource.Result
import com.pandroid.greenhaven.data.resource.State
import com.pandroid.greenhaven.domain.model.UserModel

interface AuthRepo {
    suspend fun signUpUser(userModel: UserModel, password: String, img: Uri): State<Unit>
    suspend fun updateUser(userModel: UserModel): State<Unit>
    suspend fun loginUser(email: String, password: String): State<Unit>
    suspend fun forgotPassword(email: String): State<Unit>
    suspend fun logOut(): State<Unit>
    suspend fun getUserDetails(): Result<UserModel>
    suspend fun signInWithGoogle(idToken: String): State<Unit>
}