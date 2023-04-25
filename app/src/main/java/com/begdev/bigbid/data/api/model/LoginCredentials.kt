package com.begdev.bigbid.data.api.model

data class LoginCredentials (
    var login: String? = null,
    var loginType: LoginType? = null,
    val passwordHash: String? = null
)

data class RegisterCredentials (
    var email: String? = null,
//    var loginType: LoginType? = null,
    var username: String? = null,
    val passwordHash: String? = null
)

enum class LoginType{
    email, username
}