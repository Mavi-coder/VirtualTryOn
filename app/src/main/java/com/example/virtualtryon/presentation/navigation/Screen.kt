package com.example.virtualtryon.presentation.navigation

sealed class Screen (val routed: String){
    object Home : Screen("home")
    object Avatar : Screen("avatar")
    object Camera : Screen("camera")
    object TryOn : Screen("tryon")
}