package com.begdev.bigbid.data.api

object ApiConstants {
//    const val BASE_URL = "http://192.168.100.5:3000/api/"         //e24q
    const val BASE_URL = "http://192.168.137.1:3000/api/"       //hotspot
    const val ITEMS_END_POINT = "items/"
    const val CATALOG_END_POINT = "items/catalog"
    const val LIKED_END_POINT = "items/liked"
    const val OWNER_ITEMS_END_POINT = "items/user"
    const val PHOTOS_END_POINT = BASE_URL + "image/photo/"
    const val BID_PHOTO_END_POINT = BASE_URL + "bids/photo/"
    const val AVATAR_END_POINT = BASE_URL + "image/avatar/"
    const val LOGIN_END_POINT = BASE_URL + "auth/login"
    const val REGISTER_END_POINT = BASE_URL + "auth/register"
    const val BIDS_END_POINT = BASE_URL + "bids/"
    const val BIDS_USER_END_POINT = BASE_URL + "bids/user"
    const val BIDS_ITEM_END_POINT = BASE_URL + "bids/item"
    const val BIDS_WINNER_END_POINT = BASE_URL + "bids/winner"
}