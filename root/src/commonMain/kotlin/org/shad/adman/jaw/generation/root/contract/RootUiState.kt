package org.shad.adman.jaw.generation.root.contract

enum class CameraPreviewMode{Preview,Detection,DoNotShow}
data class RootUiState(val cameraPreviewMode: CameraPreviewMode = CameraPreviewMode.DoNotShow)