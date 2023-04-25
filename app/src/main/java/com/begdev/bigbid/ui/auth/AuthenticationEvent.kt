package com.begdev.bigbid.ui.auth

sealed class AuthenticationEvent{
    object ToggleAuthenticationMode: AuthenticationEvent()

    class EmailChanged(val emailAddress: String):
        AuthenticationEvent()

    class UsernameChanged(val username: String):
        AuthenticationEvent()

    class PasswordChanged(val password: String):
        AuthenticationEvent()

    object Authenticate: AuthenticationEvent()

}

