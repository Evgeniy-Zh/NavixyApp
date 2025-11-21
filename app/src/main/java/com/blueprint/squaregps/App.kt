package com.blueprint.squaregps

import android.app.Application
import com.blueprint.squaregps.di.appModule
import com.blueprint.squaregps.feature.auth.di.authModule
import com.blueprint.squaregps.feature.tracker.di.trackerModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initDI()
    }

    private fun initDI() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                appModule,
                authModule,
                trackerModule,
            )
        }
    }
}