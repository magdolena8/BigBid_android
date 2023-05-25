package com.begdev.bigbid.ui.profile

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.begdev.bigbid.R
import com.begdev.bigbid.data.api.ApiConstants


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
) {
//    val uiState by viewModel.uiState.collectAsState()
//    val handleEvent = viewModel::handleEvent
    val uiState by viewModel.uiState.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()
    var bitmap: Bitmap? = null
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            viewModel.updateImageUri(uri)
        }
    Surface(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = uiState.person?.username!!,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    actions = {
                        if (isOnline && (uiState.profileMode == EditMode.NO_EDIT)) {
                            IconButton(onClick = { viewModel.handleEvent(ProfileEvent.ToggleEditMode) }) {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                )
            },
            content = { innerPadding ->
                Column {

                    Row(modifier = Modifier.padding(innerPadding)) {
                        val imagerPainter = rememberImagePainter(
                            data = ApiConstants.AVATAR_END_POINT + uiState.person!!.username,
                            builder = {
                                transformations(
                                    RoundedCornersTransformation(30f),
                                )
                            })
                        val offlineImagePainter = painterResource(id = R.drawable.offline_avatar)
                        Row(modifier = Modifier.padding(0.dp, 0.dp, 5.dp, 5.dp)) {
                            Column(Modifier.weight(1f).padding(8.dp)) {
                                if (uiState.bitmap == null) {
                                    Image(
                                        painter = imagerPainter,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .height(100.dp)
                                            .padding(0.dp, 0.dp, 0.dp, 3.dp),
                                        contentScale = ContentScale.FillBounds,
                                    )
                                } else {
                                    bitmap?.let {
                                        Image(
                                            bitmap = it.asImageBitmap(),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .height(100.dp)
                                                .padding(0.dp, 0.dp, 0.dp, 3.dp),
                                            contentScale = ContentScale.FillBounds,
                                        )
                                    }
                                }
                                if (uiState.profileMode == EditMode.EDIT) {
                                    Button(onClick = { launcher.launch("image/*") }) {
                                        Text("CHANGE AVATAR")
                                    }
                                }
                                uiState.imageUri?.let { uri ->
                                    bitmap = if (Build.VERSION.SDK_INT < 28) {
                                        MediaStore.Images.Media.getBitmap(
                                            context.contentResolver,
                                            uri
                                        )
                                    } else {
                                        val source =
                                            ImageDecoder.createSource(context.contentResolver, uri)
                                        ImageDecoder.decodeBitmap(source)
                                    }
                                    viewModel.updateBitmap(bitmap)

                                }
                            }
                            Column(modifier = Modifier.weight(2f)) {
                                Text(text = "Email")
                                Text(
                                    text = uiState.person?.email!!,
                                    fontSize = 20.sp
                                )
                                Text(text = "Privilege")
                                Text(
                                    text = uiState.person?.accountType!!,
                                    fontSize = 20.sp

                                )
                            }
                        }
                    }

                    Divider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                    if (uiState.profileMode == EditMode.EDIT) {
                        Button(modifier = Modifier.fillMaxWidth() , onClick = {viewModel.handleEvent(ProfileEvent.ApplyChanges) }) {
                            Text("APPLY CHANGES")
                        }
                    }

                }
            }
        )
    }
}