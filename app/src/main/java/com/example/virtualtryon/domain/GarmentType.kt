package com.example.virtualtryon.domain

import com.example.virtualtryon.R

enum class GarmentType(val displayName: String, val imageRes: Int) {
    BLAZER("Premium Blazer", R.drawable.tshirt),
    TSHIRT("Sporty T-Shirt", R.drawable.tshirt),
    WEDDING_DRESS("Wedding Dress", R.drawable.wedding_dress)
}