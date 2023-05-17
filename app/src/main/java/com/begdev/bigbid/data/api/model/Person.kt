package com.begdev.bigbid.data.api.model

import com.google.gson.annotations.SerializedName


data class Person(
    @field:SerializedName("id")
    var id: Int? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("pswd")
    val pswd: String? = null,

    @field:SerializedName("link")
    val avatar: String? = null,

    @field:SerializedName("imageId")
    val avatarId: String? = null,

    @field:SerializedName("account_type")
    val accountType: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    )