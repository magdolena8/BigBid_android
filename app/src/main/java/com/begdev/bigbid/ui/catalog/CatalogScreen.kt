package com.begdev.bigbid.ui.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.begdev.bigbid.data.api.ApiConstants
import com.begdev.bigbid.data.api.model.Item
import com.begdev.bigbid.refresh_utils.PullRefreshIndicator
import com.begdev.bigbid.refresh_utils.pullRefresh
import com.begdev.bigbid.refresh_utils.rememberPullRefreshState
import com.begdev.bigbid.ui.theme.BigBidTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    navController: NavController,
    viewModel: CatalogViewModel = hiltViewModel(),
) {
//    val isSearching by viewModel.isSearching.collectAsState()
//    val searchQuery by viewModel.searchQuery.collectAsState()
//    val isOnline by viewModel.isOnline.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.navigationEvent.collect { destination ->
            navController.navigate(destination)
        }
    }

    val itemState = viewModel.itemsState
    Surface(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        BigBidTheme {
            Scaffold(
                topBar = { TopBar(viewModel) }
            ) {
//                if (viewModel.isOnline.value == true) {
//                if (true) {
                HomeScreenContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                    itemsState = itemState,
                    handleEvent = viewModel::handleEvent,
                    viewModel = viewModel
                )
//                }
//                else{
                Text(text = "OFFLINE MODE")
            }
        }
//            HomeScreenContent(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(30.dp),
//                itemsState = itemState,
//                handleEvent = viewModel::handleEvent,
//            )

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(viewModel: CatalogViewModel) {
    val isSearching by viewModel.isSearching.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    TopAppBar(
        title = {
            if (isSearching) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.handleEvent(CatalogEvent.SearchQueryChanged(it)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        viewModel.handleEvent(CatalogEvent.SearchItems())
                    }),

                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text("Catalog")
            }
        },
        actions = {
            IconButton(onClick = { viewModel.handleEvent(CatalogEvent.ToggleSearchState()) }) {
                Icon(
                    imageVector = if (isSearching) Icons.Filled.Close else Icons.Filled.Search,
                    contentDescription = "Search"
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreenContent(
    modifier: Modifier,
    itemsState: List<Item>,
    handleEvent: (event: CatalogEvent) -> Unit,
    viewModel: CatalogViewModel
) {
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val state = rememberPullRefreshState(isRefreshing, viewModel::refreshData)

    Box(
        Modifier
            .pullRefresh(state)
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
                ItemImageCard(
                    item = item,
                    onItemClick = { handleEvent(CatalogEvent.ItemClicked(item)) })
            }
        }
        PullRefreshIndicator(isRefreshing, state, Modifier.align(Alignment.TopCenter))
    }
}

@Composable
@ExperimentalMaterial3Api
//@Preview(showBackground = true)
//@Preview
fun ItemImageCard(
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