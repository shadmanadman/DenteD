package camera.view.builder

import camera.view.controller.CameraController
import camera.view.controller.TorchMode

/**
 * Builder interface for constructing a [controller.CameraController] with customizable configurations and plugins.
 */
interface CameraControllerBuilder {
    /**
     * Builds and returns a configured instance of [controller.CameraController].
     *
     * @throws InvalidConfigurationException If mandatory parameters are missing or configurations are incompatible.
     * @return A fully configured [controller.CameraController] instance.
     */
    fun build(): CameraController
    fun setTorchMode(torchMode: TorchMode): CameraControllerBuilder
}