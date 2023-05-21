package com.begdev.bigbid.ui.owner

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.begdev.bigbid.ui.add_item.AddItemScreen
import com.begdev.bigbid.ui.item.ItemScreen
import com.begdev.bigbid.ui.theme.BigBidTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OwnerScreen(
    viewModel: OwnerViewModel = hiltViewModel(),
) {

    val navController = rememberNavController()
    LaunchedEffect(viewModel) {
        viewModel.navigationEvent.collect { destination ->
            navController.navigate(destination)
        }
    }
    Surface(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        BigBidTheme {
            NavHost(navController = navController, startDestination = Screen.Owner.route) {
                composable(route = Screen.Owner.route) {
                    OwnerItemsScreen(viewModel, navController)
                }
                composable(route = Screen.AddItem.route) {
                    AddItemScreen(viewModel, navController)
                }
                composable(route = Screen.Item.route + "/{itemId}") {
                    ItemScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun OwnerItemsScreen(
    viewModel: OwnerViewModel,
    navController: NavController
) {
    val isOnline by viewModel.isOnline.collectAsState()

    val itemsState = viewModel.itemsState
    val eventHandler: (event: OwnerEvent) -> Unit = viewModel::handleEvent

    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val state = rememberPullRefreshState(isRefreshing, viewModel::refreshData)


    Surface{
        BigBidTheme {


    Scaffold(
        floatingActionButton = {
            if (isOnline) {
                ExtendedFloatingActionButton(
                    text = {
                        Text(text = "Create lot", color = Color.White)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Navigate FAB",
                            tint = Color.White,
                        )
                    },
                    onClick = { viewModel.handleEvent(OwnerEvent.AddFABClicked()) },
                    containerColor = androidx.compose.material.MaterialTheme.colors.secondaryVariant,
                )
            }
        },
    ) {
            Box(Modifier.pullRefresh(state)) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(it)
                ) {
                    if (itemsState.isEmpty()) {
                        item {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .wrapContentSize(align = Alignment.Center)
                            )
                        }
                    }
                    items(itemsState) { item: Item ->
                        ItemImageCardOwner(item = item, onItemClick =
                        when (item.biddingCondition) {
                            "sale" -> {
                                { eventHandler(OwnerEvent.SaleItemClick(item)) }
                            }

                            else -> {
                                { eventHandler(OwnerEvent.SaleItemClick(item)) }
                            }
                        }
                        )
                    }
                }
                PullRefreshIndicator(isRefreshing, state, Modifier.align(Alignment.TopCenter))
            }
    }
}        }
}


@Composable
@ExperimentalMaterial3Api
fun ItemImageCardOwner(
    item: Item = Item(
        12,
        "Exotic Book",
        photo = "https://assets1.ignimgs.com/thumbs/userUploaded/2022/6/14/tmntshreddersrevengeblogroll-01-1655243147003.jpg",
        currentPrice = 12.1F
    ), onItemClick: () -> Unit?
) {
    val imagerPainter =
        rememberImagePainter(data = ApiConstants.PHOTOS_END_POINT + item.photo, builder = {
            transformations(
                RoundedCornersTransformation(30f),
//                GrayscaleTransformation()
            )
        })


//    BigBidTheme{
    Card(
        //todo: elevation for card in home screen and everywhere
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier
            .padding(3.dp)
            .clickable { onItemClick() },
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

            Text(
                text = "${item.title}",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 30.sp
            )
            if (item.biddingCondition != "sail") {
                Text(
                    text = "${item.biddingCondition}",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = 40.sp
                )
                if (item.biddingCondition == "sold") {
                    Row {
                        Text(
                            text = "Price",
                            color = MaterialTheme.colorScheme.primary,
                            style = TextStyle(
                                fontSize = 10.sp, color = MaterialTheme.colorScheme.secondary
                            )

                        )
                        Text(
                            text = "${item.currentPrice}",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 20.sp
                        )
                    }
                }
            } else {
                Text(
                    text = "CURRENT BID",
                    color = MaterialTheme.colorScheme.primary,
                    style = TextStyle(
                        fontSize = 10.sp, color = MaterialTheme.colorScheme.secondary
                    )

                )
                Text(
                    text = "${item.currentPrice}",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp
                )
            }
        }
//        }
    }
}