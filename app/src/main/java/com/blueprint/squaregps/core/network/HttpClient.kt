package com.blueprint.squaregps.core.network

import android.util.Log
import com.blueprint.squaregps.core.exception.UnauthorizedException
import com.blueprint.squaregps.core.auth.SessionHashMutableProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


fun createHttpClient(sessionHashProvider: SessionHashMutableProvider): HttpClient {
    return HttpClient(OkHttp) {
        expectSuccess = true

        install(ContentNegotiation) {
            json(
                Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                },
            )
        }

        install(HttpTimeout) {
            val timeout = 30000L
            socketTimeoutMillis = timeout
            requestTimeoutMillis = timeout
            connectTimeoutMillis = timeout
        }

        defaultRequest {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            url("https://api.eu.navixy.com/v2/")

        }


        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("HTTP_LOGGER", message)
                }
            }
            level = LogLevel.ALL
        }

        HttpResponseValidator {
            handleResponseExceptionWithRequest  { exception, request ->
                val clientException = exception as? ClientRequestException ?: return@handleResponseExceptionWithRequest
                val response = clientException.response
                if(response.status.value in 400..499) {
                    sessionHashProvider.clearSessionHash()
                    throw UnauthorizedException(message = response.bodyAsText())
                }
            }
        }

    }
}
