package core

import androidx.compose.runtime.Composable

interface Navigation {
    val back: Route get() = Route("back")
}

/**
 * Route information.
 *
 * @property name
 * @property fullPath
 * @property scene
 * @constructor Create [Route]
 */
data class Route(
    val name: String,
    val path: String = name,
    val scene: @Composable () -> Unit = {},
){
    companion object {
        /**
         * Pass data into route.
         *
         * @param T
         * @param data
         */
        fun <T> Route.addToPath(data: T) = copy(path = "$name/$data")
    }
}
