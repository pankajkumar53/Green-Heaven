package com.pandroid.greenhaven.presentation.component

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.FileProvider
import com.pandroid.greenhaven.R
import com.pandroid.greenhaven.presentation.utils.Dimens.LargeWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallTextSize
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargeSize
import com.pandroid.greenhaven.ui.theme.Manrope
import com.pandroid.greenhaven.ui.theme.PlantGreen
import com.pandroid.greenhaven.ui.theme.White
import java.io.File

@Composable
fun RoundImageWithIcon(
    selectedImageUri: Uri?, onImageSelected: (Uri?) -> Unit
) {
    val context = LocalContext.current
    var showImageSourceDialog by remember { mutableStateOf(false) }
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    // File picker launcher
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onImageSelected(uri)
    }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success && tempImageUri != null) {
            onImageSelected(tempImageUri)
        }
    }

    // Load bitmap from URI if selected, otherwise null
    val bitmap: ImageBitmap? = selectedImageUri?.let { uri ->
        loadBitmapFromUri(context, uri)
    }

    Box(
        modifier = Modifier
            .size(MediumLargeSize)
            .background(Color.LightGray, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                contentDescription = "SelectedImage",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(painter = painterResource(id = R.drawable.ic_person),
                contentDescription = "DefaultImage",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .clickable {
                        showImageSourceDialog = true
                    })
        }
    }

    // Image Source Selection Dialog
    if (showImageSourceDialog) {
        AlertDialog(
            containerColor = White,
            onDismissRequest = { showImageSourceDialog = false },
            title = {
                Text(
                    text = "Choose Image Source",
                    fontFamily = Manrope,
                    fontWeight = FontWeight.SemiBold
                )
            },
            text = {
                Text(
                    text = "Select Camera or File",
                    fontFamily = Manrope,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = SmallTextSize
                )
            },
            confirmButton = {
                Button(colors = ButtonDefaults.buttonColors(containerColor = PlantGreen),
                    modifier = Modifier
                        .padding(end = SmallPadding)
                        .width(LargeWidth),
                    onClick = {
                        showImageSourceDialog = false
                        val photoFile = File(context.cacheDir, "captured_image.jpg")
                        val uri = FileProvider.getUriForFile(
                            context, "${context.packageName}.provider", photoFile
                        )
                        tempImageUri = uri
                        cameraLauncher.launch(uri)
                    }) {
                    Text(text = "Camera")
                }
            },
            dismissButton = {
                Button(colors = ButtonDefaults.buttonColors(containerColor = PlantGreen),
                    modifier = Modifier
                        .padding(end = SmallPadding)
                        .width(LargeWidth),
                    onClick = {
                        showImageSourceDialog = false
                        filePickerLauncher.launch("image/*")
                    }) {
                    Text(text = "File")
                }
            })
    }

}

@Composable
fun loadBitmapFromUri(context: android.content.Context, uri: Uri): ImageBitmap? {
    return context.contentResolver.openInputStream(uri)?.use { inputStream ->
        BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
    }
}