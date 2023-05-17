package com.begdev.bigbid.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.begdev.bigbid.R
import com.begdev.bigbid.data.api.ApiConstants


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
) {
//    val uiState by viewModel.uiState.collectAsState()
//    val handleEvent = viewModel::handleEvent
    val uiState by viewModel.uiState.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()

    Surface(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {


        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = uiState.person?.username!!,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    actions = {
                        if (isOnline) {
                            IconButton(onClick = { /* doSomething() */ }) {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                )
            },
            content = { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    val imagerPainter = rememberImagePainter(
                        data = ApiConstants.AVATAR_END_POINT + uiState.person?.avatar,
                        builder = {
                            transformations(
                                RoundedCornersTransformation(30f),
                            )
                        })
                    val offlineImagePainter = painterResource(id = R.drawable.offline_avatar)
                    Row {
                        Image(
                            painter = if (isOnline) imagerPainter else offlineImagePainter,
                            contentDescription = null,
                            modifier = Modifier
                                .weight(1f)
                                .height(200.dp)
                                .padding(0.dp, 0.dp, 0.dp, 3.dp),
                            contentScale = ContentScale.FillBounds,
                        )
                        Column(modifier = Modifier.weight(2f)) {
                            Row() {
                                Text(modifier = Modifier.weight(2f), text = "Email")
                                Text(modifier = Modifier.weight(2f), text = uiState.person?.email!!)
                            }
                            Row() {
                                Text(modifier = Modifier.weight(2f), text = "Privilege")
                                Text(
                                    modifier = Modifier.weight(2f),
                                    text = uiState.person?.accountType!!
                                )
                            }
                        }
                    }
                    Divider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                    Text(text = "Bid history")
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
//                            Text(text = "bid 1")
                            Text(text = "item1")
                            Text(text = "10 h ago")
                            Text(text = "3$")
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
//                            Text(text = "bid 2")
                            Text(text = "item2")
                            Text(text = "11 h ago")
                            Text(text = "5$")
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
//                            Text(text = "bid 1")
                            Text(text = "item3")
                            Text(text = "13 h ago")
                            Text(text = "33$")
                        }
                    }
                }
            }
        )
    }
}