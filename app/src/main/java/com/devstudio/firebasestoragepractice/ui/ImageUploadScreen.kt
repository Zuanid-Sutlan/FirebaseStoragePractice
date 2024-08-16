package com.devstudio.firebasestoragepractice.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.devstudio.firebasestoragepractice.data.DatabaseProvider
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageUploadScreen() {

    var screenState by remember { mutableStateOf(true) }


    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var uploadedImageUrls by remember { mutableStateOf<List<String>>(emptyList()) }
    val scope = rememberCoroutineScope()

    // Launcher for selecting multiple images from the gallery
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
//        selectedImageUris = uris
            uploadedImageUrls = emptyList()
            scope.launch {
                val imageDownloadLinks = DatabaseProvider.repository().uploadImagesToFirebase(uris)
                uploadedImageUrls = imageDownloadLinks
            }

        }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = if (screenState) "Upload Image" else "Test Images") },
                actions = {
                    Switch(
                        checked = screenState,
                        onCheckedChange = {
                            screenState = it
                        }
                    )
                }
            )
        },
        floatingActionButton = {
            if (screenState) {
                FloatingActionButton(
                    onClick = { galleryLauncher.launch("image/*") }
                ) {
                    Text(text = "Upload New Images")
                }
            }
        }
    ) { paddingValues ->
        if (screenState) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                // images vertical grid
                LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                    items(uploadedImageUrls){
                        ImageLoadFromUrl(url = it)
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                // test image view
                var imageUrl by remember { mutableStateOf("") }
                Column {
                    TextField(value = imageUrl, onValueChange = { imageUrl = it })

                }

            }
        }
    }
}