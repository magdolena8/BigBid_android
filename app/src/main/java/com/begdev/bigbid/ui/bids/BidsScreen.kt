package com.begdev.bigbid.ui.bids

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.begdev.bigbid.data.api.ApiConstants
import com.begdev.bigbid.data.api.model.Bid
import com.begdev.bigbid.nav_utils.Screen
import com.begdev.bigbid.refresh_utils.PullRefreshIndicator
import com.begdev.bigbid.refresh_utils.pullRefresh
import com.begdev.bigbid.refresh_utils.rememberPullRefreshState
import com.begdev.bigbid.ui.item.ItemScreen
import com.begdev.bigbid.ui.theme.BigBidTheme

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun BidsScreen(
    viewModel: BidsViewModel = hiltViewModel(),
//    navController: NavController,
) {
    val navController = rememberNavController()
    LaunchedEffect(viewModel) {
        viewModel.navigationEvent.collect { destination ->
            navController.navigate(destination)
        }
    }

    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val refreshState = rememberPullRefreshState(isRefreshing, viewModel::refreshData)
    val isOnline by viewModel.isOnline.collectAsState()
    val bidsState = viewModel.bidsState
    val context = LocalContext.current

    Surface(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        BigBidTheme {
            NavHost(navController = navController, startDestination = Screen.Owner.route) {
                composable(route = Screen.Owner.route) {
                    Box(Modifier.pullRefresh(refreshState)) {
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            if (isRefreshing) {
                                item {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .wrapContentSize(align = Alignment.Center)
                                    )
                                }
                            }
                            if (bidsState.isEmpty()){
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

                            items(bidsState) { bid: Bid ->
                                BidCard(bid = bid, onItemClick = {
                                    if (isOnline) {
                                        viewModel.handleEvent(BidsEvent.BidClicked(bid))
                                    }
                                    else{
                                        Toast.makeText(context, "Unavailable in OFFLINE", Toast.LENGTH_SHORT).show()
                                    }
                                })
                            }
                        }
                        PullRefreshIndicator(
                            isRefreshing,
                            refreshState,
                            Modifier.align(Alignment.TopCenter)
                        )
                    }

                }
                composable(route = Screen.Item.route + "/{itemId}") {
                    ItemScreen()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@ExperimentalMaterial3Api
//@Preview(showBackground = true)
//@Preview
fun BidCard(
    bid: Bid = Bid(
        12,
        1,
        1
    ),
    onItemClick: (Bid) -> Unit
) {


    BigBidTheme(useDarkTheme = true) {
        Card(
            //todo: elevation for card in home screen and everywhere
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            modifier = Modifier
                .padding(3.dp)
                .fillMaxWidth()
                .clickable { onItemClick(bid) },
            shape = RoundedCornerShape(10.dp),
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                val imagerPainter = rememberImagePainter(
                    data = ApiConstants.BID_PHOTO_END_POINT + bid.itemId,
                    builder = {
                        transformations(
                            RoundedCornersTransformation(30f),
//                GrayscaleTransformation()
                        )
                    })
                Column {
                    Image(
                        painter = imagerPainter,
                        contentDescription = null,
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .padding(0.dp, 0.dp, 0.dp, 3.dp),
                        contentScale = ContentScale.FillBounds
                    )
                    Text(
                        text = "№${bid.itemId}",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 10.sp
                    )
                }
                Text(
                    text = "${bid.formatTimeBid()}",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp
                )
                Text(
                    text = "$${bid.price}",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 30.sp
                )
            }
        }
    }
}