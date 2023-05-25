package com.begdev.bigbid.ui.auth

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.begdev.bigbid.data.DBHandlerLocal
import com.begdev.bigbid.data.api.model.LoginCredentials
import com.begdev.bigbid.data.api.model.LoginType
import com.begdev.bigbid.data.api.model.RegisterCredentials
import com.begdev.bigbid.data.repository.UsersRepo
import com.begdev.bigbid.nav_utils.Screen
import com.begdev.bigbid.utils.ConnectivityChecker
import com.begdev.bigbid.utils.md5
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val usersRepo: UsersRepo,
    private val localDBHandler: DBHandlerLocal,
    private val connectivityChecker: ConnectivityChecker,
    context: Context
) : ViewModel() {
    val isOnline: StateFlow<Boolean> = connectivityChecker.isOnline

    private val _toastEvent = MutableSharedFlow<String>(replay = 0)
    val toastEvent: SharedFlow<String> = _toastEvent


    val uiState =
        MutableStateFlow(AuthenticationState(email = usersRepo.getSavedCredentials().login.toString()))
    val isLoggedIn = mutableStateOf(false)
    val loginError = mutableStateOf(false)

    private val _navigationEvent = MutableSharedFlow<String>()
    val navigationEvent: SharedFlow<String> = _navigationEvent

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
        } else if (uiState.value.authenticationMode == AuthenticationMode.SIGN_IN) {
            viewModelScope.launch(Dispatchers.IO) {
                loginPerson(uiState.value.email, md5(uiState.value.password))

                withContext(Dispatchers.Main) {
                    uiState.value = uiState.value.copy(
                        isLoading = false,
                        error = "Something went wrong!"
                    )
                }
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
        if (isOnline.value) {
            val response = usersRepo.loginUser(credentials)
            if (response.isSuccessful) {
                uiState.value = uiState.value.copy(
                    isSuccess = true
                )
                navigateToHomeScreen()
            } else {
                loginError.value = true
                viewModelScope.launch {
                    _toastEvent.emit("Bad Credentials")
                }
            }
            Log.d(TAG, "loginPerson: ${response.body()}")
        } else {
            try {
                val offlineLoginResult = localDBHandler.checkCredentialsOffline(login, passwordHash)
                if (offlineLoginResult != null) {
                    UsersRepo.currentUser = offlineLoginResult
                    navigateToHomeScreen()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                viewModelScope.launch {
                    _toastEvent.emit("Bad Credentials")
                }
            }
        }
    }

    suspend fun registerPerson(email: String, username: String, passwordHash: String) {
        val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";
        val USERNAME_REGEX = "^[a-zA-Z0-9]([._](?![._])|[a-zA-Z0-9]){6,18}[a-zA-Z0-9]\$";
        val credentials =
            RegisterCredentials(email = email, username = username, passwordHash = passwordHash)
        if (EMAIL_REGEX.toRegex().matches(email) && USERNAME_REGEX.toRegex().matches(username)) {
            val response = usersRepo.registerUser(credentials)
            if (response.isSuccessful) {
                uiState.value = uiState.value.copy(
                    isSuccess = true
                )
                navigateToHomeScreen()

            } else {
                viewModelScope.launch {
                    _toastEvent.emit("Register Error\nCheck credentials")
                }
                loginError.value = true
                Log.d(TAG, "registerPerson: ERRORRRRR")
            }
        }
        else{
            viewModelScope.launch {
                _toastEvent.emit("Bad Credentials")
            }
        }
    }

    private suspend fun navigateToHomeScreen() {
        _navigationEvent.emit(Screen.Main.route)
    }

    private fun saveUserToPrefs() {

    }

}