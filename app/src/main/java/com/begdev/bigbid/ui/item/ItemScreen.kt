package com.begdev.bigbid.ui.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.begdev.bigbid.data.api.ApiConstants
import com.begdev.bigbid.data.api.model.Item
import com.begdev.bigbid.ui.theme.BigBidTheme

@Composable
fun ItemScreen(
    viewModel: ItemViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()


    Surface(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        BigBidTheme {
            ItemScreenContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp),
                uiState.item
            )
        }
    }
}

@Composable
@Preview (showSystemUi = true, showBackground = true)
fun ItemScreenContent(
    modifier: Modifier = Modifier.padding(3.dp),
    item: Item = Item(1, "Title name", "Description description")
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
            var checked by remember { mutableStateOf(false) }

            IconToggleButton(checked = checked, onCheckedChange = {checked = it}) {
                if (checked) {
                    Icon(Icons.Filled.Lock, contentDescription = "Localized description")
                } else {
                    Icon(Icons.Outlined.Lock, contentDescription = "Localized description")
                }
            }
            Text(
                text = "${item.title}",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 30.sp
            )
            Text(
                text = "${item.description}",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp
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
