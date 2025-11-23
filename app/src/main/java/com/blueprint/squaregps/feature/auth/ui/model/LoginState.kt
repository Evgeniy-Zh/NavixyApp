package com.blueprint.squaregps.feature.auth.ui.model

data class LoginState(
    val fields: Fields = Fields(),
    val loading: Boolean = false,
    val errorMessage: String? = null
){
    data class Fields(
        val username: String = "",
        val password: String = ""
    )
}