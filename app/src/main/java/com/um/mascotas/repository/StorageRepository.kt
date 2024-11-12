package com.um.mascotas.repository

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageRepository @Inject constructor(
    private val storage: FirebaseStorage
) {
    suspend fun uploadPetPhoto(uri: Uri?): String? {
        if (uri == null) return null

        val storageRef = storage.reference
        val photoRef =
            storageRef.child("pets/${System.currentTimeMillis()}_${uri.lastPathSegment}")

        photoRef.putFile(uri).await()

        val downloadUrl = photoRef.downloadUrl.await()

        return downloadUrl.toString()
    }
}