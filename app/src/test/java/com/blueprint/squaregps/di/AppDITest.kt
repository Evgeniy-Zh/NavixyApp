package com.blueprint.squaregps.di

import android.content.Context
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import okhttp3.internal.immutableListOf
import org.junit.Test
import org.koin.test.verify.verify
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.test.verify.injectedParameters
import kotlin.test.assertEquals


@OptIn(KoinExperimentalAPI::class)
class AppDITest {


    @Test
    fun verifyAppDI() {
        val module = module {
            includes(appModule)
        }

        module.verify(
            injections = injectedParameters(),
            extraTypes = immutableListOf(
                Context::class,
                HttpClientEngine::class,
                HttpClientConfig::class,
            ),
        )

    }

}