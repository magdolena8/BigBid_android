package com.begdev.bigbid.ui.item

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.begdev.bigbid.R
import com.begdev.bigbid.data.api.ApiConstants
import com.begdev.bigbid.data.api.model.Bid
import com.begdev.bigbid.data.api.model.Item
import com.begdev.bigbid.data.repository.UsersRepo
import com.begdev.bigbid.refresh_utils.PullRefreshIndicator
import com.begdev.bigbid.refresh_utils.PullRefreshState
import com.begdev.bigbid.refresh_utils.pullRefresh
import com.begdev.bigbid.refresh_utils.rememberPullRefreshState
import com.begdev.bigbid.ui.theme.BigBidTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.imePadding
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ItemScreen(
    viewModel: ItemViewModel = hiltViewModel(),
) {
    val isOnline by viewModel.isOnline.collectAsState()
    val uiState = viewModel.uiState.collectAsState().value
    val bidsState = viewModel.bidsState
    val handleEvent = viewModel::handleEvent
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val refreshState = rememberPullRefreshState(isRefreshing, viewModel::refreshData)
    val winnerEmail = viewModel.winnerEmail.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(viewModel.toastEvent) {
        viewModel.toastEvent.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    ProvideWindowInsets {
        Surface(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            BigBidTheme {
                Scaffold(
                    bottomBar = {
                        if ((uiState.item.value.ownerId != UsersRepo.currentUser?.id) and isOnline and isOnline and (uiState.item.value.biddingCondition != "sold")) {
                            BottomBidPanel(
                                item = uiState.item.value,
                                bid = uiState.userBid,
                                onBidChanged = { viewModel.handleEvent(ItemEvent.UserBidChanged(it.toFloat())) },
                                onPlaceBid = { handleEvent(ItemEvent.PlaceBid) }
                            )
                        }
                    }
                ) { contentPadding ->
                    ItemScreenContent(
                        modifier = Modifier
                            .padding(contentPadding),
                        item = uiState.item.value,
                        bidsState = bidsState,
                        isLiked = uiState.item.value.isLiked,
                        onLikePressed = { viewModel.handleEvent(ItemEvent.ItemLikePressed) },
                        isOnline = isOnline,
                        isRefreshing = isRefreshing,
                        refreshState = refreshState,
                        winnerEmail = winnerEmail.value
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ItemScreenContent(
    modifier: Modifier = Modifier.padding(3.dp),
    item: Item,
    isLiked: Boolean,
    isOnline: Boolean,
    onLikePressed: () -> Unit,
    bidsState: List<Bid>,
    isRefreshing: Boolean,
    refreshState: PullRefreshState,
    winnerEmail: String? = ""

) {
    val context = LocalContext.current
    val imagerPainter = rememberImagePainter(
        data = ApiConstants.PHOTOS_END_POINT + item.photo,
        builder = {
            transformations(
                RoundedCornersTransformation(30f),
//                GrayscaleTransformation()
            )
        })
    BigBidTheme(useDarkTheme = true) {
        Box(Modifier.pullRefresh(refreshState)) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
//                    .verticalScroll(rememberScrollState())
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
                if ((item.ownerId != UsersRepo.currentUser?.id) and isOnline and (item.biddingCondition != "sold")) {
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
                    if (item.biddingCondition != "sale" && item.biddingCondition != null) {
                        Text(
                            text = "${item.biddingCondition?.uppercase(Locale.getDefault())}",
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            fontSize = 40.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Text(
                        text = "$${item.currentPrice}",
                        color = MaterialTheme.colorScheme.onPrimary,
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
                        text = item.formatEndTime(),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 20.sp
                    )
                }
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(top = 20.dp)
                )
                Text(text = "Bid history")
                Box {
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
                        if (bidsState.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .align(Alignment.Center)
                                ) {
                                    Text(
                                        text = "No bids yet",
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth()
                                    )
                                }
                            }
                        }
                        items(bidsState) { bid: Bid ->
                            val isFirst = bidsState.indexOf(bid) == 0;
                            Row(
                                modifier = if (isFirst) Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.onBackground)
                                    .fillMaxWidth() else Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                            ) {
                                val imagerPainterAvatar = rememberImagePainter(
                                    data = ApiConstants.AVATAR_END_POINT + bid.bidderUsername,
                                    builder = {
                                        transformations(
                                            RoundedCornersTransformation(30f),
                                        )
                                    })
                                Image(
                                    painter = imagerPainterAvatar,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .width(30.dp)
                                        .height(30.dp)
                                        .padding(0.dp, 0.dp, 0.dp, 3.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Text(
                                    text = bid.bidderUsername.toString(),
                                    color = if (isFirst) MaterialTheme.colorScheme.tertiary
                                    else MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = bid.formatTimeBid(),
                                    color = if (isFirst) MaterialTheme.colorScheme.tertiary
                                    else MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = bid.price.toString(),
                                    color = if (isFirst) MaterialTheme.colorScheme.tertiary
                                    else MaterialTheme.colorScheme.primary
                                )
                                if (isFirst && item.ownerId == UsersRepo.currentUser?.id && item.biddingCondition == "sold") {
//                                    Button(onClick = { onBuyerPressed() }) {
                                    Button(onClick = {
                                        if (isOnline) {
                                            context.sendMail(to = winnerEmail ?: "")
//                                            Toast.makeText(context, "Mail Service Unavailable", Toast.LENGTH_SHORT).show()

                                        }
                                        else{
                                            Toast.makeText(context, "Mail Service Unavailable", Toast.LENGTH_SHORT).show()
                                        }
                                    }) {
                                        Text(text = "Email")
                                    }
                                }
                            }
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.padding(8.dp)
                            ) {
                            }
                        }
                    }
                }
            }
            PullRefreshIndicator(
                isRefreshing,
                refreshState,
                Modifier.align(Alignment.TopCenter)
            )
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
                    .padding(horizontal = 12.dp)
                    .imePadding(),
                item = item,
                bid = bid ?: item.currentPrice,
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
        placeholder = { Text("More then" + item.currentPrice.toString()) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal
        )
    )
}

fun Context.sendMail(to: String) {
    try {
        val subject = "You won the BigBid Auction!"
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "vnd.android.cursor.item/email" // or "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        // TODO: Handle case where no email app is available
    } catch (t: Throwable) {
        // TODO: Handle potential other type of exceptions
    }
}

fun Context.dial(phone: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
        startActivity(intent)
    } catch (t: Throwable) {
        // TODO: Handle potential exceptions
    }
}