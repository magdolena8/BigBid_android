package com.begdev.bigbid.auth

import androidx.annotation.StringRes
import com.begdev.bigbid.R

data class AuthenticationState(
    val authenticationMode: AuthenticationMode = AuthenticationMode.SIGN_IN,
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val passwordRequirements: List<PasswordRequirement> = emptyList()

    ){
//    fun isFormValid(): Boolean {
//        return password?.isNotEmpty() == true &&
//                email?.isNotEmpty() == true &&
//                (authenticationMode == AuthenticationMode.SIGN_IN
//                        || passwordRequirements.containsAll(
//                    passwordRequirements.values().toList()))
//    }
}

enum class AuthenticationMode {
    SIGN_UP, SIGN_IN
}

enum class PasswordRequirement(
    @StringRes val label: Int
) {
    CAPITAL_LETTER(R.string.requirement_capital),
    NUMBER(R.string.requirement_digit),
    EIGHT_CHARACTERS(R.string.requirement_characters)
}