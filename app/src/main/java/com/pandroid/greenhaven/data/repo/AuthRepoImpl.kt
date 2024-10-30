package com.pandroid.greenhaven.data.repo

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.pandroid.greenhaven.data.repo.UploadImage.uploadImage
import com.pandroid.greenhaven.data.resource.State
import com.pandroid.greenhaven.data.resource.Result
import com.pandroid.greenhaven.domain.model.UserModel
import com.pandroid.greenhaven.domain.repo.AuthRepo
import com.pandroid.greenhaven.utils.NodeRef
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) : AuthRepo {

    // Sign Up User
    override suspend fun signUpUser(
        userModel: UserModel,
        password: String,
        img: Uri
    ): State<Unit> {
        return try {
            // Create the user with email and password
            val authResult =
                firebaseAuth.createUserWithEmailAndPassword(userModel.email, password).await()

            // Get user ID from auth result
            val userId = authResult.user?.uid ?: throw Exception("User ID not found")

            val image = uploadImage(
                imageUri = img,
                title = "${userId}_${userModel.name}",
                path = NodeRef.USER_IMG_REF
            )
            image.onSuccess { imageUrl ->
                // Update the plantImage field in the PlantModel
                userModel.img = imageUrl
            }.onFailure { error ->
                throw Exception("user image upload failed: ${error.message}")
            }

            // Create a new UserModel object with the obtained userId
            val updatedUserModel = userModel.copy(userId = userId)

            // Reference to the database node
            val usersReference = firebaseDatabase.getReference(NodeRef.USER_REF)

            // Save user data to the database
            usersReference.child(userId).setValue(updatedUserModel).await()

            // Return success state
            State.Success(Unit)
        } catch (e: Exception) {
            // Return error state
            State.Error(e.toString())
        }
    }

    // Update User
    override suspend fun updateUser(userModel: UserModel): State<Unit> {
        return try {
            // Get the currently logged-in user
            val firebaseUser = firebaseAuth.currentUser ?: throw Exception("User not logged in")

            // Get user ID from FirebaseAuth
            val userId = firebaseUser.uid

            // Reference to the database node where the user data is stored
            val usersReference = firebaseDatabase.getReference(NodeRef.USER_REF).child(userId)

            // Update the user data in the database
            usersReference.setValue(userModel).await()

            // Return success state
            State.Success(Unit)
        } catch (e: Exception) {
            // Return error state
            State.Error(e.toString())
        }
    }

    // Login User
    override suspend fun loginUser(email: String, password: String): State<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            State.Success(Unit)
        } catch (e: Exception) {
            State.Error(e.toString())
        }
    }

    // Get User Data
    override suspend fun getUserDetails(): Result<UserModel> {
        return try {
            // Get the currently logged-in user
            val firebaseUser = firebaseAuth.currentUser ?: throw Exception("User not logged in")

            // Get user ID from FirebaseAuth
            val userId = firebaseUser.uid

            // Reference to the database node where the user data is stored
            val usersReference = firebaseDatabase.getReference(NodeRef.USER_REF).child(userId)

            // Fetch the user data from the database
            val snapshot = usersReference.get().await()

            // Parse the data into a UserModel object
            val userModel = snapshot.getValue(UserModel::class.java)

            // Return the user data wrapped in success state
            if (userModel != null) {
                Result.Success(userModel)
            } else {
                Result.Failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    // Forgot Password
    override suspend fun forgotPassword(email: String): State<Unit> {
        return try {
            // Send password reset email
            firebaseAuth.sendPasswordResetEmail(email).await()
            State.Success(Unit)
        } catch (e: Exception) {
            State.Error(e.toString())
        }
    }

    // Log Out
    override suspend fun logOut(): State<Unit> {
        return try {
            firebaseAuth.signOut()
            State.Success(Unit)
        } catch (e: Exception) {
            State.Error(e.toString())
        }
    }

    // One tap Sign In
    override suspend fun signInWithGoogle(idToken: String): State<Unit> {
        return try {
            // Sign in with Google credential
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = firebaseAuth.signInWithCredential(credential).await()

            // Get the currently signed-in user
            val firebaseUser = authResult.user
            firebaseUser?.let { user ->
                // Get user details
                val userId = user.uid
                val name = user.displayName ?: "Unknown"
                val email = user.email ?: "No email"
                val phone = user.phoneNumber ?: "No phone number"
                val profileImageUrl = user.photoUrl?.toString() ?: ""

                // Create a UserModel object to store in Firebase
                val userModel = UserModel(
                    userId = userId,
                    name = name,
                    email = email,
                    phoneNumber = phone,
                    img = profileImageUrl
                )

                // Store the user data in Realtime Database under "users" node
                val userRef =
                    FirebaseDatabase.getInstance().getReference(NodeRef.USER_REF).child(userId)
                userRef.setValue(userModel).await()

                // Return success state
                State.Success(Unit)
            } ?: run {
                // In case user is null
                State.Error("User not found")
            }
        } catch (e: Exception) {
            // Return error state in case of failure
            State.Error(e.message ?: "Google Sign-In failed")
        }
    }


}




