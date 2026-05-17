package com.example.feature.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.Submit -> submit()
            // ❌ 考题：缺少 ShowCaptcha；下面 else 会吞掉新分支
            else -> Unit
        }
    }

    private fun submit() {
        _uiState.update { it.copy(loading = true) }
        // ...
    }
}
