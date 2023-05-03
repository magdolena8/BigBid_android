package com.begdev.bigbid.nav_utils

import androidx.annotation.StringRes
import com.begdev.bigbid.R

sealed class Screen(val route: String, @StringRes val resourceId: Int? = null) {
//    object Home : Screen("home", R.string.home)
    object Profile : Screen("profile", R.string.profile)
    object Auth : Screen("auth", R.string.login)
    object Market : Screen("market_graph", R.string.market)
    object Item : Screen("item", R.string.item)
    object Main : Screen("main", R.string.main)
    object Root : Screen("root")
    object Catalog : Screen("catalog")
}