package com.pandroid.greenhaven.domain.usecase

import android.net.Uri
import com.pandroid.greenhaven.data.resource.Result
import com.pandroid.greenhaven.data.resource.State
import com.pandroid.greenhaven.domain.model.UserModel
import com.pandroid.greenhaven.domain.repo.AuthRepo
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepo: AuthRepo
) : AuthRepo {

    override suspend fun signUpUser(userModel: UserModel, password: String, img: Uri): State<Unit> {
        return authRepo.signUpUser(userModel, password, img)
    }

    override suspend fun updateUser(userModel: UserModel): State<Unit> {
        return authRepo.updateUser(userModel)
    }

    override suspend fun loginUser(email: String, password: String): State<Unit> {
        return authRepo.loginUser(email, password)
    }

    override suspend fun forgotPassword(email: String): State<Unit> {
        return authRepo.forgotPassword(email)
    }

    override suspend fun logOut(): State<Unit> {
        return authRepo.logOut()
    }

    override suspend fun getUserDetails(): Result<UserModel> =
        authRepo.getUserDetails()

    override suspend fun signInWithGoogle(idToken: String): State<Unit> {
        return authRepo.signInWithGoogle(idToken)
    }

}
