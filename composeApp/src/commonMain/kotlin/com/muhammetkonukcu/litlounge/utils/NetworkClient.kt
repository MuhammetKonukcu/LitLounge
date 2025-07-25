package com.muhammetkonukcu.litlounge.utils

import io.ktor.client.statement.HttpResponse

interface NetworkClient {
    suspend fun head(url: String): HttpResponse
    fun close()
}