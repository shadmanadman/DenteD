package camera.di

import camera.viewmodel.AnalyzerViewModel
import camera.viewmodel.CameraViewModel
import camera.viewmodel.JawViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val cameraModule = module {
    viewModelOf(::JawViewModel)
    viewModelOf(::AnalyzerViewModel)
    viewModelOf(::CameraViewModel)
}