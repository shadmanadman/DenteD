package org.shad.adman.jaw.generation

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform