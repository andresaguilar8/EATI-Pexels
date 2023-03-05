package com.eati.pexels.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.eati.pexels.domain.Photo
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties

@Composable
fun PhotosScreen(viewModel: PhotosViewModel) {
    val result by viewModel.photosFlow.collectAsState()
    var searchText by remember { mutableStateOf("") }
    var pressedButton by remember { mutableStateOf(false) }

    Column {
        SearchBar({ pressedButton = !pressedButton}, searchText, { searchText = ""}, onValueChange = { searchText = it })
        if (pressedButton){
            Photos(searchText, result, viewModel::updateResults)
        }
    }
}

@Composable
fun SearchBar(buttonActivation: () -> Unit, searchText: String, resetText: () -> Unit, onValueChange: (String) -> Unit) {
    var buttonText by remember { mutableStateOf("Search") }

    Text(text = "EATI Pexels",
        textAlign = TextAlign.Start,
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier.padding(start = 10.dp, bottom = 10.dp, top = 10.dp))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.1f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
        ) {
            TextField(
                value = searchText,
                placeholder = {
                    Text(text = "What do you want to see?")
                },
                onValueChange = onValueChange,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.surface
                ),
                modifier = Modifier
                    .heightIn(min = 56.dp)
                    .padding(start = 10.dp)
                    .padding(end = 10.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
            )

            Button(
                onClick = {
                    buttonActivation()
                    if (buttonText == "Reset"){
                        buttonText = "Search"
                        resetText()
                    }
                    else
                        buttonText = "Reset"
                },
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(end = 10.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
            ) {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Text(
                    buttonText,
                    textAlign = TextAlign.Start,
                )
            }
        }
    }
}

fun Modifier.customAlertDialog(
    photo: Photo
): Modifier = this.then(
    Modifier
        .heightIn(max = photo.height.dp)
        .widthIn(max = photo.width.dp)
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PhotoCard(photo: Photo, photoHeight: Int, photoWidth: Int, modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) }

    AsyncImage(
        model = photo.sourceURL,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(128.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { showDialog = true }
    )

    if (showDialog) {
        AlertDialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            modifier = Modifier.customAlertDialog(photo),
            onDismissRequest = { showDialog = false },
            title = { Text(
                text = ("Photographer: "+photo.photographer),
                fontSize = 25.sp,
                style = MaterialTheme.typography.h3,
                modifier = Modifier
                    .padding(8.dp)) },
            text = {
                Column (
                    Modifier
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally)
                {
                    Box(
                        modifier = Modifier
                    ) {
                        Column {
                            AsyncImage(
                                model = photo.sourceURL,
                                contentDescription = null,
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier
                                    .fillMaxSize(1f)
                                    .padding(bottom = 20.dp, top = 10.dp)
                            )
                            Row {
                                Icon(
                                    Icons.Filled.Favorite,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(start = 10.dp)
                                )
                                Icon(
                                    Icons.Filled.Person,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(start = 10.dp)
                                )
                                Icon(
                                    Icons.Filled.Place,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(start = 10.dp)
                                )
                                Icon(
                                    Icons.Filled.Share,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(start = 10.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text(text = "Close")
                }
            }
        )
    }
}

@Composable
fun Photos(searchText: String, results: List<Photo>, updateResults: (String) -> Unit) {

        LaunchedEffect(Unit) {
            updateResults(searchText)
        }

        LazyVerticalGrid (
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ){
            items(results) { item ->
                PhotoCard(item, item.height, item.width)
            }
        }
}