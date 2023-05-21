package com.begdev.bigbid.ui.favourite

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.begdev.bigbid.nav_utils.Screen
import com.begdev.bigbid.ui.bids.BidsScreen
import com.begdev.bigbid.ui.liked.LikedScreen
import com.begdev.bigbid.ui.owner.OwnerScreen
import com.begdev.bigbid.ui.theme.BigBidTheme

@Composable
fun FavouriteScreen(
    viewModel: FavouriteViewModel = hiltViewModel()
) {
    TabScreen()
}

@Composable
fun TabScreen() {
    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf(
        Screen.Owner,
        Screen.Liked,
        Screen.Bets
    )
    Surface(        Modifier
        .wrapContentHeight()
        .fillMaxWidth()){
//    BigBidTheme{
            Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed {index, screen ->
                Tab(text = { Text(stringResource(screen.resourceId!!)) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    icon = {Icon(screen.icon!!, contentDescription = null)}
                )
            }
        }
        when (tabIndex) {
            0 -> OwnerScreen()
            1 -> LikedScreen()
            2 -> BidsScreen()
        }
    }}
}