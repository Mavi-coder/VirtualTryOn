package com.example.virtualtryon.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.virtualtryon.domain.GarmentType
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import kotlin.math.atan2

@Composable
fun GarmentOverlay(
    garmentType: GarmentType,
    poseLandmarkerResult: PoseLandmarkerResult?,
    imageWidth: Int,
    imageHeight: Int,
    userSize: String = "M"
) {
    val imageRes = garmentType.imageRes
    
    val sizeMultiplier = when (userSize) {
        "S" -> 2.6f
        "M" -> 2.9f
        "L" -> 3.2f
        "XL" -> 3.5f
        else -> 2.9f
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val scope = this
        val landmarks = poseLandmarkerResult?.landmarks()?.firstOrNull()
        val isVisible = landmarks != null && landmarks.size > 24
        
        // Target values for animation
        var targetX by remember { mutableFloatStateOf(0f) }
        var targetY by remember { mutableFloatStateOf(0f) }
        var targetWidth by remember { mutableFloatStateOf(0f) }
        var targetRotation by remember { mutableFloatStateOf(0f) }
        var targetHeightScale by remember { mutableFloatStateOf(1f) }

        if (landmarks != null && landmarks.size > 24) {
            val leftShoulder = landmarks[11]
            val rightShoulder = landmarks[12]
            val leftHip = landmarks[23]
            val rightHip = landmarks[24]
            
            val leftX = leftShoulder.x() * scope.constraints.maxWidth
            val leftY = leftShoulder.y() * scope.constraints.maxHeight
            val rightX = rightShoulder.x() * scope.constraints.maxWidth
            val rightY = rightShoulder.y() * scope.constraints.maxHeight
            
            val lHipX = leftHip.x() * scope.constraints.maxWidth
            val lHipY = leftHip.y() * constraints.maxHeight
            val rHipX = rightHip.x() * constraints.maxWidth
            val rHipY = rightHip.y() * constraints.maxHeight

            // Midpoints
            val shoulderMidX = (leftX + rightX) / 2
            val shoulderMidY = (leftY + rightY) / 2
            val hipMidX = (lHipX + rHipX) / 2
            val hipMidY = (lHipY + rHipY) / 2

            // Body dimensions
            val dxS = (rightX - leftX)
            val dyS = (rightY - leftY)
            val shoulderWidth = kotlin.math.sqrt((dxS * dxS + dyS * dyS).toDouble()).toFloat()
            
            val dxT = (hipMidX - shoulderMidX)
            val dyT = (hipMidY - shoulderMidY)
            val torsoHeight = kotlin.math.sqrt((dxT * dxT + dyT * dyT).toDouble()).toFloat()

            targetX = shoulderMidX
            targetY = shoulderMidY
            
            // Width is based on shoulder distance
            targetWidth = shoulderWidth * sizeMultiplier
            
            // Height aspect ratio adjustment for "fitted" feel
            targetHeightScale = (torsoHeight / (shoulderWidth * 1.3f)).coerceIn(0.8f, 1.4f)
            
            // Calculate rotation based on the shoulder line
            // We use the absolute distance to prevent the 180-degree flip in mirrored mode
            val tiltAngle = Math.toDegrees(atan2(dyS.toDouble(), kotlin.math.abs(dxS).toDouble())).toFloat()
            targetRotation = if (dxS < 0) -tiltAngle else tiltAngle
        }

        // Animated values for buttery smooth movement
        val animX by animateFloatAsState(targetValue = targetX, animationSpec = spring(stiffness = Spring.StiffnessLow), label = "x")
        val animY by animateFloatAsState(targetValue = targetY, animationSpec = spring(stiffness = Spring.StiffnessLow), label = "y")
        val animWidth by animateFloatAsState(targetValue = targetWidth, animationSpec = spring(stiffness = Spring.StiffnessLow), label = "width")
        val animRotation by animateFloatAsState(targetValue = targetRotation, animationSpec = spring(stiffness = Spring.StiffnessMediumLow), label = "rotation")
        val animAlpha by animateFloatAsState(targetValue = if (isVisible) 1f else 0f, animationSpec = tween(800), label = "alpha")
        val animHeightScale by animateFloatAsState(targetValue = targetHeightScale, animationSpec = spring(stiffness = Spring.StiffnessLow), label = "heightScale")

        if (isVisible) {
            val density = LocalContext.current.resources.displayMetrics.density
            val garmentWidthDp = (animWidth / density).dp

            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(garmentWidthDp)
                    .graphicsLayer {
                        translationX = animX - animWidth / 2
                        translationY = animY - animWidth / 3.8f 
                        rotationZ = animRotation
                        alpha = animAlpha
                        
                        scaleX = animHeightScale
                        scaleY = animHeightScale
                        
                        // Perspective depth
                        val depthEffect = (animWidth / scope.constraints.maxWidth).coerceIn(0.8f, 1.2f)
                        scaleX *= depthEffect
                        scaleY *= depthEffect
                    }
            )
        }
    }
}