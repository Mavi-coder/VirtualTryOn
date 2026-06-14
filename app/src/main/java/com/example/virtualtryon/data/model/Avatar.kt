package com.example.virtualtryon.data.model

data class Avatar(
    val name: String = "",
    val gender: String = "",
    val height: String = "",
    val weight: String = "",
    val size: String = "",
    val createdAt: Long = System.currentTimeMillis()
)