package com.begdev.bigbid.nav_utils

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import com.begdev.bigbid.R

sealed class Screen(val route: String, @StringRes val resourceId: Int? = null, val icon: ImageVector?=null) {
//    object Home : Screen("home", R.string.home)
    object Profile : Screen("profile", R.string.profile, icon = Icons.Filled.Face)
    object Auth : Screen("auth", R.string.login)
    object Market : Screen("market_graph", R.string.market, icon = Icons.Filled.List)
    object Item : Screen("item", R.string.item)
    object Main : Screen("main", R.string.main)
    object Root : Screen("root")
    object Catalog : Screen("catalog")
    object Favourite : Screen("favourite", R.string.favourite, icon = Icons.Filled.Favorite)
    object Owner : Screen("owner", R.string.owner, icon = Icons.Filled.AccountBox)
    object Liked : Screen("liked_screen", R.string.liked, icon = Icons.Filled.FavoriteBorder)
    object Bets : Screen("bets", R.string.bets, icon = Icons.Filled.Done)
    object AddItem : Screen("add_item", R.string.add_item, icon = Icons.Filled.Add)
    object LikedItems : Screen("liked_items", R.string.add_item, icon = Icons.Filled.Add)
}