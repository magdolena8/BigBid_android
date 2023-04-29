package com.begdev.bigbid.ui.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.begdev.bigbid.ui.theme.BigBidTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
//viewModel: ItemViewModel = hiltViewModel()
) {
//    val uiState by viewModel.uiState.collectAsState()
//    val handleEvent = viewModel::handleEvent
    Surface(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        BigBidTheme {
            Text(text = "Profile")
            Text(text = "Profile")
            Text(text = "Profile")
            Text(text = "Profile")
            Text(text = "Profile")
        }

    }
}