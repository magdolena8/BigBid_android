package com.begdev.bigbid.ui.bids

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.begdev.bigbid.data.api.model.Bid
import com.begdev.bigbid.refresh_utils.PullRefreshIndicator
import com.begdev.bigbid.refresh_utils.pullRefresh
import com.begdev.bigbid.refresh_utils.rememberPullRefreshState
import com.begdev.bigbid.ui.theme.BigBidTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun BidsScreen(
    viewModel: BidsViewModel = hiltViewModel()

) {
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val state = rememberPullRefreshState(isRefreshing, viewModel::refreshData)

//    val refreshScope = rememberCoroutineScope()
//    var refreshing by remember { mutableStateOf(false) }
//    fun refresh() = refreshScope.launch {
//        refreshing = true
//        delay(1500)
////        itemCount += 5
//        refreshing = false
//    }


    val bidsState = viewModel.bidsState
    Surface(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Box(Modifier.pullRefresh(state).pullRefresh(state)) {


            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                if (bidsState.isEmpty()) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(align = Alignment.Center)
                        )
                    }
                }
                items(bidsState) { bid: Bid ->
                    BidCard(bid = bid, onItemClick = { /*TODO*/ })
//                ItemImageCardOwner(item = item, onItemClick = { /*TODO*/})
                }
            }
            PullRefreshIndicator(isRefreshing, state, Modifier.align(Alignment.TopCenter))

        }
    }

}

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
                .clickable { onItemClick(bid) },
            shape = RoundedCornerShape(10.dp),
        ) {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = "${bid.itemId}",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 30.sp
                )

                Text(
                    text = "${bid.timeBid}",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp
                )
                Text(
                    text = "${bid.price}",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp
                )
            }
        }
    }
}