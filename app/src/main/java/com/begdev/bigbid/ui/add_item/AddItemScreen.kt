package com.begdev.bigbid.ui.add_item

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.begdev.bigbid.R
import com.begdev.bigbid.ui.owner.OwnerEvent
import com.begdev.bigbid.ui.owner.OwnerViewModel
import com.begdev.bigbid.ui.theme.BigBidTheme
import com.google.accompanist.insets.navigationBarsWithImePadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(viewModel: OwnerViewModel, navController: NavController) {
    val uiState = viewModel.itemsState
    val newItemUiState = viewModel.newItemUiState.collectAsState().value


    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            viewModel.updateImageUri(uri)
        }


    Surface(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsWithImePadding()
    ) {
//        ProvideWindowInsets {
            BigBidTheme {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Create Lot") //TODO: make text size bigger


                    Button(onClick = { launcher.launch("image/*") }) {
                        Text(text = "Pick image")
                    }
                    newItemUiState.imageUri?.let { uri ->
                        val bitmap = if (Build.VERSION.SDK_INT < 28) {
                            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                        } else {
                            val source = ImageDecoder.createSource(context.contentResolver, uri)
                            ImageDecoder.decodeBitmap(source)
                        }
                        viewModel.updateBitmap(bitmap)
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.size(100.dp)
                        )
                    }


                    OutlinedTextField(value = newItemUiState.title ?: "",
                        onValueChange = { title ->
                            viewModel.handleEvent(OwnerEvent.TitleChanged(title))
                        },
                        singleLine = true,
                        placeholder = { Text(stringResource(R.string.placeholder_title)) })

                    OutlinedTextField(value = newItemUiState.category ?: "",
                        onValueChange = { category ->
                            viewModel.handleEvent(OwnerEvent.CategoryChanged(category))
                        },
                        singleLine = true,
                        placeholder = { Text(stringResource(R.string.placeholder_category)) })

                    OutlinedTextField(value = newItemUiState.description ?: "",
                        onValueChange = { description ->
                            viewModel.handleEvent(OwnerEvent.DescriptionChanged(description))
                        },
                        singleLine = true,
                        placeholder = { Text(stringResource(R.string.placeholder_description)) })

                    OutlinedTextField(
                        value = newItemUiState.startPrice.toString() ?: "",
                        onValueChange = { price ->
                            viewModel.handleEvent(OwnerEvent.PriceChanged(price.toFloat()))
                        },
                        singleLine = true,
                        placeholder = { Text(stringResource(R.string.placeholder_start_price)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        )
                    )

                    OutlinedTextField(
                        value = newItemUiState.aucDuration.toString() ?: "",
                        onValueChange = { duration ->
                            viewModel.handleEvent(OwnerEvent.DurationChanged(duration.toInt()))
                        },
                        singleLine = true,
                        placeholder = { Text(stringResource(R.string.placeholder_duration)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        )
                    )

                    Button(onClick = { viewModel.handleEvent(OwnerEvent.AddButtonClicked())}) {
                        Text(text = "CREATE")
                    }
                }
            }
//        }
    }
}