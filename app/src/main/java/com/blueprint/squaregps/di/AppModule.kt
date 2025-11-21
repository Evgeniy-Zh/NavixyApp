package com.blueprint.squaregps.di

import com.blueprint.squaregps.MainViewModel
import com.blueprint.squaregps.core.auth.SessionHashMutableProvider
import com.blueprint.squaregps.core.auth.SessionHashProvider
import com.blueprint.squaregps.feature.auth.data.impl.SessionHashProviderImpl
import com.blueprint.squaregps.core.network.createHttpClient
import com.blueprint.squaregps.navigation.AppNavigator
import com.blueprint.squaregps.navigation.AppNavigatorImpl
import com.blueprint.squaregps.navigation.NavigationEventHandler
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.binds
import org.koin.dsl.module

val appModule = module {
    single { createHttpClient(sessionHashProvider = get()) }
    single { AppNavigatorImpl() } binds arrayOf(AppNavigator::class, NavigationEventHandler::class)
    single { SessionHashProviderImpl(context = get()) } binds arrayOf(SessionHashProvider::class, SessionHashMutableProvider::class)
    viewModelOf(::MainViewModel)
}