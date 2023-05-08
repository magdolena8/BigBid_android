package com.begdev.bigbid.ui.owner

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import com.begdev.bigbid.ui.add_item.AddItemScreen
import com.begdev.bigbid.ui.theme.BigBidTheme

@Composable
fun OwnerScreen(
    viewModel: OwnerViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    LaunchedEffect(viewModel) {
        viewModel.navigationEvent.collect { destination ->
            navController.navigate(destination)
        }
    }

    NavHost(navController = navController, startDestination = Screen.Owner.route) {
        composable(route = Screen.Owner.route) {
            OwnerItemsScreen(viewModel, navController)
        }
        composable(route = Screen.AddItem.route) {
            AddItemScreen(viewModel, navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerItemsScreen(
    viewModel: OwnerViewModel,
    navController: NavController
) {
    val itemsState = viewModel.itemsState

    Surface(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Scaffold(
            floatingActionButton = {
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

            },
        ) {

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
                    ItemImageCardOwner(item = item, onItemClick = { /*TODO*/ })
                }
            }
        }
    }
}


@Composable
@ExperimentalMaterial3Api
fun ItemImageCardOwner(
    item: Item = Item(
        12,
        "Exotic Book",
        photo = "https://assets1.ignimgs.com/thumbs/userUploaded/2022/6/14/tmntshreddersrevengeblogroll-01-1655243147003.jpg",
        currentBid = 12.1F
    ), onItemClick: (Item) -> Unit
) {
    val imagerPainter =
        rememberImagePainter(data = ApiConstants.PHOTOS_END_POINT + item.photo, builder = {
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

                Text(
                    text = "${item.title}",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 30.sp
                )
                Text(
                    text = "CURRENT BID",
                    color = MaterialTheme.colorScheme.primary,
                    style = TextStyle(
                        fontSize = 10.sp, color = MaterialTheme.colorScheme.secondary
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