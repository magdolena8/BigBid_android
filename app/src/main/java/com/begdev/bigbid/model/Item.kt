package com.begdev.bigbid.model

import com.google.gson.annotations.SerializedName


data class Item(

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("title")
    val released: String? = null,

    @field:SerializedName("description")
    val backgroundImage: String? = null,

//    @field:SerializedName("background_image_additional")
//    val backgroundImageAdditional: String? = null,
//
//    @field:SerializedName("name")
//    val name: String? = null,
//
//    @field:SerializedName("description")
//    val description: String? = null,


)