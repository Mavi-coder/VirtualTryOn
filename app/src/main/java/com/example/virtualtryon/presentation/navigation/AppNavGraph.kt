package com.example.virtualtryon.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.virtualtryon.presentation.screens.AvatarScreen
import com.example.virtualtryon.presentation.screens.CameraScreen
import com.example.virtualtryon.presentation.screens.HomeScreen
import com.example.virtualtryon.viewmodel.AvatarViewModel


@Composable
fun AppNavGraph(){

    val navController = rememberNavController()
    val avatarViewModel: AvatarViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.routed
    ){
        composable(Screen.Home.routed) {
            HomeScreen(
                onAvatarClick = {
                    navController.navigate(Screen.Avatar.routed)
                },
                onCameraClick = {
                    navController.navigate(Screen.Camera.routed)
                }
            )
        }
        composable(Screen.Avatar.routed) {
            AvatarScreen(
                viewModel = avatarViewModel,
                onStartTryOn = {
                    navController.navigate(Screen.Camera.routed) {
                        popUpTo(Screen.Home.routed)
                    }
                }
            )
        }
        composable(Screen.Camera.routed) {
            CameraScreen(avatarViewModel = avatarViewModel)
        }
    }
}