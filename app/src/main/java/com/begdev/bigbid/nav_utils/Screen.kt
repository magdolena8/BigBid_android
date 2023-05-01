package com.begdev.bigbid.nav_utils

import androidx.annotation.StringRes
import com.begdev.bigbid.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Market : Screen("market", R.string.market)
//    object Profile : Screen("profile", R.string.profile)
    object Profile : Screen("item/10", R.string.profile)
}