package com.eati.pexels.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.eati.pexels.domain.Photo

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
fun PhotoCard(photo: Photo) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "",
            style = MaterialTheme.typography.h3,
            modifier = Modifier.paddingFromBaseline(
                top = 24.dp, bottom = 8.dp
            )
        )
        AsyncImage(
            model = photo.sourceURL,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(128.dp)
        )

        ImageInformation(photo)

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

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            LazyColumn {
                items(results) { item ->
                    PhotoCard(item)
                }
            }
        }

        Text(text = results.toString())
}