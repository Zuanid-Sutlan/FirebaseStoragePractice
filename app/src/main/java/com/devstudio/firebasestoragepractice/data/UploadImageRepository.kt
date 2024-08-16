package com.devstudio.firebasestoragepractice.data

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class UploadImageRepository {
    private val storage = FirebaseStorage.getInstance()

    private val storageRef = storage.reference

    suspend fun uploadImagesToFirebase(uris: List<Uri>): List<String> {
        val uploadedUrls = mutableListOf<String>()

        uris.forEach { uri ->
            val fileReference = storageRef.child("images/${System.currentTimeMillis()}_${uri.lastPathSegment}")
            val uploadTask = fileReference.putFile(uri).await()

            if (uploadTask.task.isSuccessful) {
                val downloadUrl = fileReference.downloadUrl.await().toString()
                uploadedUrls.add(downloadUrl)
            } else {
                throw uploadTask.task.exception ?: Exception("Failed to upload image: $uri")
            }
        }

        return uploadedUrls
    }

}