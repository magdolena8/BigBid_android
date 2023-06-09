package com.begdev.bigbid.ui.liked

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.begdev.bigbid.data.api.ApiConstants
import com.begdev.bigbid.data.api.model.Item
import com.begdev.bigbid.nav_utils.Screen
import com.begdev.bigbid.refresh_utils.PullRefreshIndicator
import com.begdev.bigbid.refresh_utils.pullRefresh
import com.begdev.bigbid.refresh_utils.rememberPullRefreshState
import com.begdev.bigbid.ui.item.ItemScreen
import com.begdev.bigbid.ui.theme.BigBidTheme

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun LikedScreen(
    viewModel: LikedViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    LaunchedEffect(viewModel) {
        viewModel.navigationEvent.collect { destination ->
            navController.navigate(destination)
        }
    }

    NavHost(navController = navController, startDestination = Screen.LikedItems.route) {
        composable(route = Screen.LikedItems.route) {
            LikedItemsScreen(viewModel, navController)
        }
        composable(route = Screen.Item.route + "/{itemId}") {
            ItemScreen()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
@ExperimentalMaterial3Api
fun LikedItemsScreen(
    viewModel: LikedViewModel,
    navController: NavController
) {
    val eventHandler: (event: LikedEvent) -> Unit = viewModel::handleEvent
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val state = rememberPullRefreshState(isRefreshing, viewModel::refreshData)
    val itemsState = viewModel.itemsState

//    LaunchedEffect(viewModel) {
//        viewModel.navigationEvent.collect { destination ->
//            navController.navigate(destination)
//        }
//    }
    Surface(
        Modifier
            .wrapContentHeight()
            .fillMaxSize()
    ) {
        Box(
            Modifier
                .pullRefresh(state)
                .fillMaxSize()
                .heightIn(min = LocalConfiguration.current.screenHeightDp.dp)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                if (viewModel.isRefreshing.value) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(align = Alignment.Center)
                        )
                    }
                }
                if (itemsState.isEmpty()) {
                    item {
                        Text(
                            text = "Nothing here yet",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize()
                                .align(Alignment.Center)
                        )
                    }
                }
                items(itemsState) { item: Item ->
                    ItemImageCardLiked(
                        item = item,
                        onItemClick = { eventHandler(LikedEvent.LikedItemClick(item)) })
                }
            }
            PullRefreshIndicator(isRefreshing, state, Modifier.align(Alignment.TopCenter))
        }
    }
}


@Composable
@ExperimentalMaterial3Api
fun ItemImageCardLiked(
    item: Item = Item(
        12,
        "Exotic Book",
        photo = "https://assets1.ignimgs.com/thumbs/userUploaded/2022/6/14/tmntshreddersrevengeblogroll-01-1655243147003.jpg",
        currentPrice = 12.1F
    ),
    onItemClick: (Item) -> Unit
) {
    val imagerPainter = rememberImagePainter(
        data = ApiConstants.PHOTOS_END_POINT + item.photo,
        builder = {
            transformations(
                RoundedCornersTransformation(30f),
//                GrayscaleTransformation()
            )
        })


    BigBidTheme(useDarkTheme = true) {
        Card(
            //todo: elevation for card in home screen and everywhere
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            modifier = Modifier
                .padding(3.dp)
                .clickable { onItemClick(item) },
            shape = RoundedCornerShape(10.dp),
        ) {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Image(
                    painter = imagerPainter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(0.dp, 0.dp, 0.dp, 3.dp),
                    contentScale = ContentScale.FillBounds
                )
                var checked by remember { mutableStateOf(false) }

//                IconToggleButton(checked = checked, onCheckedChange = { checked = it }) {
//                    if (checked) {
//                        Icon(Icons.Filled.Favorite, contentDescription = "Localized description")
//                    } else {
//                        Icon(Icons.Outlined.Favorite, contentDescription = "Localized description")
//                    }
//                }

                Text(
                    text = "${item.title}",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 30.sp
                )

                Text(
                    text = "CURRENT BID",
                    color = MaterialTheme.colorScheme.primary,
                    style = TextStyle(
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                )
                Text(
                    text = "${item.currentPrice}",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp
                )

            }
        }
    }
}