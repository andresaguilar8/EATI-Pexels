package com.eati.pexels.presentation


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


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

   Text(text = "Android App",
        textAlign = TextAlign.Center,
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier.padding(start = 80.dp, bottom = 20.dp, top = 20.dp))


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.1f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
        ) {
            TextField(
                value = searchText,
                placeholder = {
                    Text(text = "Search")
                },
                onValueChange = onValueChange,
                modifier = Modifier
                    .size(260.dp, 120.dp)
                    .padding(start = 8.dp)
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
                    .padding(end = 8.dp)
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

@Composable
fun PhotoCard(photo: Photo, modifier: Modifier = Modifier) {
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
            modifier = Modifier
                .size(600.dp),
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Photographer: "+photo.photographer, Modifier.padding(8.dp)) },
            text = {
                Column (
                    Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally)
                {
                    Box(modifier = Modifier) {
                        AsyncImage(
                            model = photo.sourceURL,
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxSize(1f)
                                .padding(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
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
fun ImageInformation(photo: Photo) {
    Text(
        modifier = Modifier
            .padding(top = 4.dp, bottom = 8.dp)
            .fillMaxWidth(0.5f),
        textAlign = TextAlign.Center,
        text = "Photograph: ${photo.photographer}"
    )
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
                PhotoCard(item)
            }
        }
}