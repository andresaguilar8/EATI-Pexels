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
import kotlinx.coroutines.launch


@Composable
fun PhotosScreen(viewModel: PhotosViewModel) {
    val result by viewModel.photosFlow.collectAsState()
    var textoDeBusqueda by remember { mutableStateOf("") }
    var botonApretado by remember { mutableStateOf(false) }

    Column() {
        BarraDeBusqueda(botonApretado, { botonApretado = true}, textoDeBusqueda, onValueChange = { textoDeBusqueda = it },)
        if (botonApretado)
            Photos(textoDeBusqueda, result, viewModel::updateResults)
    }



}

fun changueButton(estado: Boolean) {
}

@Composable
fun BarraDeBusqueda(botonApretado: Boolean, funcionBoton: () -> Unit, textoDeBusqueda: String , onValueChange: (String) -> Unit) {

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
                value = textoDeBusqueda,
                placeholder = {
                    Text(text = "Search")
                },
                onValueChange = onValueChange,
            )

            Button(
                onClick = {
                    funcionBoton()
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
                    text = "Search",
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
fun Photos(textoDeBusqueda: String, results: List<Photo>, updateResults: (String) -> Unit) {

        LaunchedEffect(Unit) {
            updateResults(textoDeBusqueda)
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            LazyColumn(
            ) {
                items(results) { item ->
                    PhotoCard(item)
                }
            }
        }

        Text(text = results.toString())


}