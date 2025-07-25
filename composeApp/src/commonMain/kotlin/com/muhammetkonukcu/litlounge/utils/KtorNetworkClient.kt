package com.muhammetkonukcu.litlounge.utils

import io.ktor.client.HttpClient
import io.ktor.client.request.head
import io.ktor.client.statement.HttpResponse

class KtorNetworkClient(private val httpClient: HttpClient) : NetworkClient {
    override suspend fun head(url: String): HttpResponse {
        return httpClient.head(url)
    }

    override fun close() {
        httpClient.close()
    }
}