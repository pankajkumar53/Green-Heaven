package com.pandroid.greenhaven.data.repo

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object UploadImage : KoinComponent {
    private val firebaseStorage: FirebaseStorage by inject()

    suspend fun uploadImage(imageUri: Uri, title: String, path: String): Result<String> {
        return try {
            val fileName = "$title.jpg"
            val imagePath = "${path}$fileName"
            val imageRef = firebaseStorage.reference.child(imagePath)
            val uploadTask = imageRef.putFile(imageUri)

            // Wait for the upload to complete and retrieve the download URL
            val downloadUrl = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                imageRef.downloadUrl
            }.await()

            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
