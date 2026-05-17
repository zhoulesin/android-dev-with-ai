package com.example.feature.login

sealed interface LoginAction {
    data object Submit : LoginAction
    data object ShowCaptcha : LoginAction
}

data class LoginUiState(
    val email: String = "",
    val loading: Boolean = false,
    val showCaptcha: Boolean = false,
    val error: String? = null,
)
