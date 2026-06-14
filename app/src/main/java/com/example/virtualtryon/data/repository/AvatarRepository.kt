package com.example.virtualtryon.data.repository

import com.example.virtualtryon.data.model.Avatar
import com.google.firebase.firestore.FirebaseFirestore

class AvatarRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun saveAvatar(
        avatar: Avatar,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        firestore
            .collection("avatars")
            .document()
            .set(avatar)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}