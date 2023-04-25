package com.begdev.bigbid.ui.item

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.begdev.bigbid.ui.home.HomeScreenContent
import com.begdev.bigbid.ui.home.HomeViewModel
import com.begdev.bigbid.ui.theme.BigBidTheme

@Composable
fun HomeScreen(
    viewModel: ItemViewModel = hiltViewModel()
) {
    val itemState = viewModel.itemsState
    Surface(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        BigBidTheme {
            HomeScreenContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp),
                itemsState = itemState,
            )
        }
    }
}
