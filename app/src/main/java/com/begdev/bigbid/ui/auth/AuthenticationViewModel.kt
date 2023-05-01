package com.begdev.bigbid.ui.auth

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.begdev.bigbid.data.api.model.LoginCredentials
import com.begdev.bigbid.data.api.model.LoginType
import com.begdev.bigbid.data.api.model.RegisterCredentials
import com.begdev.bigbid.data.repository.UsersRepo
import com.begdev.bigbid.nav_utils.AppNavigator
import com.begdev.bigbid.nav_utils.Destination
import com.begdev.bigbid.utils.md5
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
    private val usersRepo: UsersRepo

) : ViewModel() {
    val uiState = MutableStateFlow(AuthenticationState())

    fun handleEvent(authenticationEvent: AuthenticationEvent) {
        when (authenticationEvent) {
            is AuthenticationEvent.ToggleAuthenticationMode -> {
                toggleAuthenticationMode()
            }
            is AuthenticationEvent.EmailChanged -> {
                updateEmail(authenticationEvent.emailAddress)
            }
            is AuthenticationEvent.UsernameChanged -> {
                updateUsername(authenticationEvent.username)
            }
            is AuthenticationEvent.PasswordChanged -> {
                updatePassword(authenticationEvent.password)
            }
            is AuthenticationEvent.Authenticate -> {
                authenticate()
            }
        }
    }


    private fun toggleAuthenticationMode() {
        val authenticationMode = uiState.value.authenticationMode
        val newAuthenticationMode = if (
            authenticationMode == AuthenticationMode.SIGN_IN
        ) {
            AuthenticationMode.SIGN_UP
        } else {
            AuthenticationMode.SIGN_IN
        }
        uiState.value = uiState.value.copy(
            authenticationMode = newAuthenticationMode
        )
    }

    private fun updateEmail(email: String) {
        uiState.value = uiState.value.copy(
            email = email
        )
    }

    private fun updateUsername(username: String) {
        uiState.value = uiState.value.copy(
            username = username
        )
    }

    private fun updatePassword(password: String) {
        uiState.value = uiState.value.copy(
            password = password
        )
    }

    private fun authenticate() {
        uiState.value = uiState.value.copy(
            isLoading = true
        )
        if (uiState.value.authenticationMode == AuthenticationMode.SIGN_UP) {
            viewModelScope.launch(Dispatchers.IO) {
                registerPerson(
                    uiState.value.email,
                    uiState.value.username,
                    md5(uiState.value.password)
                )

            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                loginPerson(uiState.value.email, md5(uiState.value.password))

//            withContext(Dispatchers.Main) {
//                uiState.value = uiState.value.copy(
//                    isLoading = false,
//                    error = "Something went wrong!"
//                )
//            }
            }
        }
    }


    suspend fun loginPerson(login: String, passwordHash: String) {
        val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";
        val USERNAME_REGEX = "^[a-zA-Z0-9]([._](?![._])|[a-zA-Z0-9]){6,18}[a-zA-Z0-9]\$";
        val credentials = LoginCredentials(login = login, passwordHash = passwordHash)

        if (EMAIL_REGEX.toRegex().matches(login)) {
            credentials.loginType = LoginType.email
        } else if (USERNAME_REGEX.toRegex().matches(login)) {
            credentials.loginType = LoginType.username
        }

//        val person = usersRepo.loginUser(credentials)
        val response = usersRepo.loginUser(credentials)
        if (response.isSuccessful) {
            navigateToHomeScreen()
        } else {

        }
        Log.d(TAG, "loginPerson: ${response.body()}")
    }

    suspend fun registerPerson(email: String, username: String, passwordHash: String) {
        val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";
        val USERNAME_REGEX = "^[a-zA-Z0-9]([._](?![._])|[a-zA-Z0-9]){6,18}[a-zA-Z0-9]\$";
        val credentials =
            RegisterCredentials(email = email, username = username, passwordHash = passwordHash)
        if (EMAIL_REGEX.toRegex().matches(email) && USERNAME_REGEX.toRegex().matches(username)) {
            val response = usersRepo.registerUser(credentials)
            if (response.isSuccessful) {
//                Log.d(TAG, "registerPerson: " )
                navigateToHomeScreen()
            } else {
                Log.d(TAG, "registerPerson: ERRORRRRR")
            }
        }
    }

    fun navigateToHomeScreen() {
        Log.d(TAG, "onNavigateToRegisterBtnClicked: before navigation")
        appNavigator.tryNavigateTo(Destination.MainScreen())
//        appNavigator.tryNavigateTo(Destination.RegisterScreen())
        Log.d(TAG, "onNavigateToRegisterBtnClicked: after navigation click ??????")
    }

}