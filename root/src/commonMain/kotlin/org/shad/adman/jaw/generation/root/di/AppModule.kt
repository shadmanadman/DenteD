package org.shad.adman.jaw.generation.root.di

import camera.di.cameraModule
import di.mainModule
import jaw.di.jawModule

fun appModules() = listOf(mainModule, jawModule, cameraModule)