package com.blueprint.squaregps.feature.auth.di

import com.blueprint.squaregps.feature.auth.data.api.AuthApi
import com.blueprint.squaregps.feature.auth.data.impl.LoginDataSourceImpl
import com.blueprint.squaregps.feature.auth.domain.LoginDataSource
import com.blueprint.squaregps.feature.auth.domain.LoginRepository
import com.blueprint.squaregps.feature.auth.ui.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val authModule = module {

    single { AuthApi(client = get()) }

    //login
    single<LoginDataSource> { LoginDataSourceImpl(authApi = get()) }
    single { LoginRepository(loginDataSource = get(), sessionHashMutableProvider = get()) }
    viewModelOf(::LoginViewModel)

}