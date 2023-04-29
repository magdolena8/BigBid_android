package com.begdev.bigbid.nav_utils

import androidx.annotation.StringRes
import com.begdev.bigbid.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Home : Screen("home", R.string.home)
    object Profile : Screen("profile", R.string.profile)
}