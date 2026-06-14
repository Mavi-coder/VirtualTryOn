package com.example.virtualtryon.presentation.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.view.PreviewView
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.virtualtryon.camera.startCamera
import com.example.virtualtryon.domain.GarmentType
import com.example.virtualtryon.components.GarmentOverlay
import com.example.virtualtryon.viewmodel.AvatarViewModel
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult

@Composable
fun CameraScreen(
    avatarViewModel: AvatarViewModel = viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentAvatar by avatarViewModel.currentAvatar.collectAsState()

    var selectedGarment by remember { mutableStateOf(GarmentType.BLAZER) }
    var poseResults by remember { mutableStateOf<PoseLandmarkerResult?>(null) }
    var imageWidth by remember { mutableIntStateOf(0) }
    var imageHeight by remember { mutableIntStateOf(0) }
    var cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_FRONT_CAMERA) }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        if (hasCameraPermission) {
            key(cameraSelector) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        PreviewView(ctx).apply {
                            startCamera(
                                context = context,
                                lifecycleOwner = lifecycleOwner,
                                previewView = this,
                                cameraSelector = cameraSelector,
                                onResults = { results, width, height ->
                                    poseResults = results
                                    imageWidth = width
                                    imageHeight = height
                                }
                            )
                        }
                    }
                )
            }

            GarmentOverlay(
                garmentType = selectedGarment,
                poseLandmarkerResult = poseResults,
                imageWidth = imageWidth,
                imageHeight = imageHeight,
                userSize = currentAvatar?.size ?: "M"
            )

            if (poseResults != null && poseResults?.landmarks()?.isNotEmpty() == true) {
                ActiveScannerLine()
            }
        }

        // --- CLEANED UI ---
        
        // Top Header
        Box(modifier = Modifier.fillMaxWidth().padding(top = 44.dp)) {
            Text(
                text = "Virtual Style",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    letterSpacing = 2.sp
                )
            )
        }

        // Bottom Selection Panel
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                    )
                )
                .padding(bottom = 32.dp)
        ) {
            // Garment Selection Grid
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(GarmentType.values()) { type ->
                    GarmentCard(
                        type = type,
                        isSelected = selectedGarment == type,
                        onClick = { selectedGarment = type }
                    )
                }
            }

            // Controls
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { /* Handle Close */ },
                    modifier = Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(Icons.Default.Close, null, tint = Color.White)
                }
                
                // Camera Toggle
                FloatingActionButton(
                    onClick = { 
                        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) 
                            CameraSelector.DEFAULT_BACK_CAMERA else CameraSelector.DEFAULT_FRONT_CAMERA 
                    },
                    containerColor = Color(0xFFE91E63),
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Cameraswitch, null)
                }

                IconButton(
                    onClick = { /* Handle Confirm */ },
                    modifier = Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(Icons.Default.Check, null, tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun GarmentCard(type: GarmentType, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Surface(
            modifier = Modifier
                .size(72.dp)
                .border(
                    width = if (isSelected) 3.dp else 0.dp,
                    color = if (isSelected) Color(0xFFE91E63) else Color.Transparent,
                    shape = RoundedCornerShape(16.dp)
                ),
            shape = RoundedCornerShape(16.dp),
            color = Color.White.copy(alpha = 0.9f)
        ) {
            Image(
                painter = painterResource(type.imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize().padding(8.dp),
                contentScale = ContentScale.Fit
            )
        }
        Text(
            text = type.displayName,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 8.dp),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun ActiveScannerLine() {
    val infiniteTransition = rememberInfiniteTransition(label = "scannerLine")
    val yProgress by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "yProgress"
    )

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val scope = this
        val yOffset = scope.constraints.maxHeight * yProgress
        val density = LocalContext.current.resources.displayMetrics.density
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .offset(y = (yOffset / density).dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, Color(0xFFE91E63), Color.Transparent)
                    )
                )
                .alpha(0.6f)
        )
    }
}
