package com.begdev.bigbid.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.begdev.bigbid.data.api.model.Item
import com.begdev.bigbid.ui.theme.BigBidTheme


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
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


@Composable
fun HomeScreenContent(
    modifier: Modifier,
    itemsState: List<Item>
) {
    LazyColumn(modifier = modifier) {
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
            ItemImageCard(item = item)
        }
    }
}

//@Composable
//@Preview
//fun HomeScreenPreview() {
//    Surface() {
//
//val qwe = Item(12, "ivan")
//        LazyColumn {
//            ItemImageCard()
//            ItemImageCard(Item(12, "ivan"))
//            ItemImageCard(Item(12, "ivan"))
//            ItemImageCard(Item(12, "ivan"))
//        }
//    }
//}


@Composable
@Preview
fun ItemImageCard(item: Item = Item(12, "ivan")) {
//    val imagerPainter = rememberImagePainter(data = character.image)
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(16.dp)
    ) {
        Box {

//            Image(
//                painter = imagerPainter,
//                contentDescription = null,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(200.dp),
//                contentScale = ContentScale.FillBounds
//            )

            Surface(
//                color = MaterialTheme.colors.onSurface.copy(alpha = .3f),
                modifier = Modifier.align(Alignment.BottomCenter),
//                contentColor = MaterialTheme.colors.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Text(text = "Real name: ${item.id}")
                    Text(text = "Actor name: ${item.title}")
                }
            }
        }
    }
}