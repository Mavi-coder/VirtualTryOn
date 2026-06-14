package com.example.virtualtryon.ml

import android.content.Context
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.core.Delegate
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker.PoseLandmarkerOptions
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult

class PoseDetector(
    context: Context
) {

    private val poseLandmarker: PoseLandmarker

    init {

        val baseOptions = BaseOptions.builder()
            .setModelAssetPath("pose_landmarker_lite.task")
            .setDelegate(Delegate.CPU)
            .build()

        val options = PoseLandmarkerOptions.builder()
            .setBaseOptions(baseOptions)
            .setRunningMode(RunningMode.IMAGE)
            .setMinPoseDetectionConfidence(0.3f) // Lowered for easier detection
            .setMinPosePresenceConfidence(0.3f)
            .setMinTrackingConfidence(0.3f)
            .setNumPoses(1)
            .build()

        poseLandmarker =
            PoseLandmarker.createFromOptions(
                context,
                options
            )
    }

    fun detect(mpImage: MPImage): PoseLandmarkerResult? {

        return try {
            poseLandmarker.detect(mpImage)
        } catch (e: Exception) {
            null
        }
    }
}