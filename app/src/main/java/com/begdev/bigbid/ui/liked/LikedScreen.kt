package com.begdev.bigbid.ui.liked

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.begdev.bigbid.data.api.ApiConstants
import com.begdev.bigbid.data.api.model.Item
import com.begdev.bigbid.refresh_utils.PullRefreshIndicator
import com.begdev.bigbid.refresh_utils.pullRefresh
import com.begdev.bigbid.refresh_utils.rememberPullRefreshState
import com.begdev.bigbid.ui.theme.BigBidTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun LikedScreen(
    viewModel: LikedViewModel = hiltViewModel()
) {
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val state = rememberPullRefreshState(isRefreshing, viewModel::refreshData)

    val itemsState = viewModel.itemsState
    Surface(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Box(
            Modifier
                .pullRefresh(state)
                .pullRefresh(state)) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
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
                    ItemImageCardLiked(item = item, onItemClick = { /*TODO*/ })
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
fun ItemImageCardLiked(
    item: Item = Item(
        12,
        "Exotic Book",
        photo = "https://assets1.ignimgs.com/thumbs/userUploaded/2022/6/14/tmntshreddersrevengeblogroll-01-1655243147003.jpg",
        currentBid = 12.1F
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

                IconToggleButton(checked = checked, onCheckedChange = { checked = it }) {
                    if (checked) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Localized description")
                    } else {
                        Icon(Icons.Outlined.Favorite, contentDescription = "Localized description")
                    }
                }

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
                    text = "${item.currentBid}",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp
                )
            }
        }
    }
}