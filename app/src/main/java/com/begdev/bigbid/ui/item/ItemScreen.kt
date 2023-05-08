package com.begdev.bigbid.ui.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.begdev.bigbid.R
import com.begdev.bigbid.data.api.ApiConstants
import com.begdev.bigbid.data.api.model.Item
import com.begdev.bigbid.ui.theme.BigBidTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreen(
    navController: NavController,
    viewModel: ItemViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    val handleEvent = viewModel::handleEvent
    Surface(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        BigBidTheme {
            Scaffold(
                bottomBar = {
                    BottomBidPanel(
                        item = uiState.item.value,
                        bid = uiState.userBid,
                        onBidChanged = { viewModel.handleEvent(ItemEvent.UserBidChanged(it.toFloat())) },
                        onPlaceBid = { handleEvent(ItemEvent.PlaceBid) }
                    )
                }
            ) { contentPadding ->
                ItemScreenContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding),
                    item = uiState.item.value,
                    isLiked =  uiState.item.value.isLiked,
                    onLikePressed = { viewModel.handleEvent(ItemEvent.ItemLikePressed) },
//                    onUnlike = { viewModel.handleEvent(ItemEvent.ItemUnliked) }
                )
            }
        }
    }
}

@Composable
fun ItemScreenContent(
    modifier: Modifier = Modifier.padding(3.dp),
    item: Item = Item(1, "Title name", "Description description", currentBid = 42.52f),
    isLiked: Boolean,
    onLikePressed: () -> Unit,
//    onUnlike: () -> Unit,
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
                text = "NO. ${item.id}",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 10.sp
            )

            IconToggleButton(checked = isLiked,
                onCheckedChange = {
                    onLikePressed()
                }) {
                if (isLiked) {
                    Icon(
                        painter = painterResource(R.drawable.ic_liked),
                        contentDescription = "Localized description"
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_unliked),
                        contentDescription = "Localized description"
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${item.title}",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 30.sp
                )
                Text(
                    text = "$${item.currentBid}",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 30.sp
                )
            }

            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.padding(top = 10.dp)
            )

            Text(
                text = "${item.description}",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Category",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp
                )
                Text(
                    text = "${item.category}",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "SellerID",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp
                )
                Text(
                    text = "${item.ownerId}",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Ends in",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp
                )
                Text(
                    text = "${item.auctionEndTime}",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp
                )
            }


//            Row {
//                Text(
//                    modifier = Modifier.padding(5.dp),
//                    text = "CURRENT BID",
//                    color = MaterialTheme.colorScheme.primary,
//                    style = TextStyle(
//                        fontSize = 10.sp,
//                        color = MaterialTheme.colorScheme.secondary
//                    )
//                )
//                Text(
//                    text = "${item.currentBid}",
//                    color = MaterialTheme.colorScheme.primary,
//                    fontSize = 10.sp
//                )
//            }

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
                    Text(text = "Bidder1")
                    Text(text = "10 h ago")
                    Text(text = "3$")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(text = "Bidder1")
                    Text(text = "10 h ago")
                    Text(text = "3$")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(text = "Bidder1")
                    Text(text = "10 h ago")
                    Text(text = "3$")
                }
            }

//todo: LazyColumn for bid history

//            LazyColumn{
//                Row(modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceAround){
//                    Text(text = "Bidder1")
//                    Text(text = "10 h ago")
//                    Text(text = "3$")
//                }
//            }


        }
    }
}

@Composable
fun BottomBidPanel(
    item: Item,
    bid: Float?,
    onBidChanged: (bid: String) -> Unit,
    onPlaceBid: () -> Unit,

    ) {
    Surface {
        Row {
            BidInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                item = item,
                bid = bid ?: item.currentBid,
                onBidChanged = onBidChanged
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f), onClick = onPlaceBid
            ) {
                Text(text = "PLACE BID")
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BidInput(
    modifier: Modifier = Modifier.fillMaxWidth(),
    bid: Float? = 0f,
    item: Item,
    onBidChanged: (bid: String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = bid.toString(),
        onValueChange = { bid ->
            onBidChanged(bid)
        },
        singleLine = true,
        placeholder = { Text("More then" + item.currentBid.toString()) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal
        )

    )
}