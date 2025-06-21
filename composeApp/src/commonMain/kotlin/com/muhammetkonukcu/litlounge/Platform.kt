package com.muhammetkonukcu.litlounge

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform