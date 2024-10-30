package com.pandroid.greenhaven.presentation.screens.createPlant

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.pandroid.greenhaven.R
import com.pandroid.greenhaven.data.resource.State
import com.pandroid.greenhaven.domain.model.PlantModel
import com.pandroid.greenhaven.presentation.component.CustomButton
import com.pandroid.greenhaven.presentation.component.CustomCircularIndicator
import com.pandroid.greenhaven.presentation.component.loadBitmapFromUri
import com.pandroid.greenhaven.presentation.screens.component.CustomDropdown
import com.pandroid.greenhaven.presentation.screens.component.MultiImagePicker
import com.pandroid.greenhaven.presentation.utils.Dimens.ExtraLargeHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.ExtraLargeWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.ExtraSmallWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.LargeWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargeHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargeSmallHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargeSmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumRoundedCorner
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallRoundedCorner
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumTextSize
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallMediumHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallMediumPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallMediumSize
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallSize
import com.pandroid.greenhaven.presentation.utils.Dimens.VerySmallMediumPadding
import com.pandroid.greenhaven.ui.theme.LightGray
import com.pandroid.greenhaven.ui.theme.PlantGreen
import com.pandroid.greenhaven.ui.theme.White
import com.pandroid.greenhaven.utils.NodeRef.CATEGORY
import java.io.File

@Composable
fun UpdateAndDeleteProduct(
    viewModel: PlantViewModel,
    plantModel: PlantModel,
    onDismiss: () -> Unit
) {
    // Pre-fill the form with the product's existing data
    var title by remember { mutableStateOf(plantModel.title) }
    var rating by remember { mutableStateOf(plantModel.rating) }
    var description by remember { mutableStateOf(plantModel.description) }
    var price by remember { mutableStateOf(plantModel.price) }
    var selectedCategory by remember { mutableStateOf(plantModel.category) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var images by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val scrollState = rememberScrollState()

    val plantState by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .border(width = MediumSmallWidth, color = LightGray, shape = RoundedCornerShape(MediumRoundedCorner))
            .verticalScroll(scrollState)
    ) {
        TopAppBar(
            onDismiss = { onDismiss() },
            text = "Update Product",
            productId = plantModel.plantId,
            viewModel
        )

        Spacer(modifier = Modifier.height(SmallHeight))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(ExtraLargeWidth),
            contentAlignment = Alignment.Center
        ) {
            ProductRoundImageWithIcon(
                defaultImage = plantModel.plantImage,
                selectedImageUri = imageUri,
                onImageSelected = { uri -> imageUri = uri }
            )

        }

        Spacer(modifier = Modifier.height(SmallMediumHeight))

        MultiImagePicker(
            default = plantModel.plantImages,
            images = images,
            onImagesPicked = { uris -> images = uris }
        )

        ProductForm(title = title,
            rating = rating,
            description = description,
            price = price,
            onTitleChange = { title = it },
            onRatingChange = { rating = it },
            onDescriptionChange = { description = it },
            onPriceChange = { price = it }
        )

        Spacer(modifier = Modifier.height(SmallHeight))

        CategoryDropdownWrapper(
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedValue -> selectedCategory = selectedValue }
        )

        Spacer(modifier = Modifier.height(SmallMediumHeight))

        CustomButton(
            onClick = {
                viewModel.updatePlant(
                    plantId = plantModel.plantId,
                    imageUri = imageUri,
                    title = title,
                    rating = rating,
                    description = description,
                    price = price,
                    category = selectedCategory,
                    images = images,
                )
            },
            modifier = Modifier.fillMaxWidth().padding(MediumSmallPadding).height(MediumHeight),
            text = "Update Plant"
        )


        Spacer(modifier = Modifier.height(MediumLargeHeight))


        when (plantState) {
            is State.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CustomCircularIndicator()
                }
            }

            is State.Success -> {
                if (viewModel.isPlantRecentlyUpdated()) {
                    title = ""
                    rating = ""
                    description = ""
                    price = ""
                    selectedCategory = ""
                    imageUri = null
                    images = emptyList()
                    viewModel.resetPlantUpdatedFlag()


                    Toast.makeText(
                        LocalContext.current,
                        "Plant updated successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    onDismiss()

                }

            }

            is State.Error -> {
                Text("Error: ${(plantState as State.Error).message}")
            }

            else -> {}
        }

    }


}


@Composable
fun CategoryDropdownWrapper(
    selectedCategory: String?,
    onCategorySelected: (category: String) -> Unit
) {

    CustomDropdown(
        items = CATEGORY,
        selectedItem = selectedCategory,
        onItemSelected = { newSelectedCategory -> onCategorySelected(newSelectedCategory) },
        itemToString = { it },
        label = "Select Category"
    )

}


@Composable
fun TopAppBar(onDismiss: () -> Unit, text: String, productId: String, viewModel: PlantViewModel) {
    Box(
        modifier = Modifier
            .height(MediumLargeSmallHeight)
            .fillMaxWidth()
            .clip(RoundedCornerShape(MediumRoundedCorner))
            .background(PlantGreen)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SmallMediumPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .padding(top = MediumLargeSmallPadding, start = VerySmallMediumPadding)
                    .size(SmallSize)
                    .clickable {
                        onDismiss()
                    },
                tint = White
            )


            Text(
                text = text,
                fontSize = MediumTextSize,
                color = White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = MediumLargeSmallPadding, top = MediumLargeSmallPadding)
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = "Delete Icon",
                modifier = Modifier
                    .padding(top = MediumLargeSmallPadding, end = VerySmallMediumPadding)
                    .border(width = ExtraSmallWidth, color = LightGray, shape = RoundedCornerShape(MediumSmallRoundedCorner))
                    .padding(VerySmallMediumPadding)
                    .size(SmallMediumSize)
                    .clickable {
                        viewModel.delete(productId)
                        onDismiss()
                    },
                tint = White
            )
        }
    }
}


@Composable
fun ProductRoundImageWithIcon(
    defaultImage: String? = null,
    selectedImageUri: Uri?,
    onImageSelected: (Uri?) -> Unit
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
            .fillMaxWidth()
            .padding(start = SmallMediumPadding, end = SmallMediumPadding)
            .height(ExtraLargeHeight)
            .background(White, shape = RoundedCornerShape(MediumSmallRoundedCorner)),
        contentAlignment = Alignment.Center
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                contentDescription = "SelectedImage",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(MediumSmallRoundedCorner)),
                contentScale = ContentScale.Crop
            )
        } else {

            if (defaultImage != null) {
                AsyncImage(
                    model = defaultImage,
                    contentDescription = "default",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(MediumSmallRoundedCorner))
                        .clickable {
                            showImageSourceDialog = true
                        },
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.icon_expanded),
                    contentDescription = "DefaultImage",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(MediumSmallRoundedCorner))
                        .clickable {
                            showImageSourceDialog = true
                        }
                )
            }
        }

    }


    // Image Source Selection Dialog
    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text(text = "Choose Image Source") },
            text = { Text(text = "Select Camera or File") },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = PlantGreen),
                    modifier = Modifier
                        .padding(end = SmallPadding)
                        .width(LargeWidth),
                    onClick = {
                        showImageSourceDialog = false
                        val photoFile = File(context.cacheDir, "captured_image.jpg")
                        val uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.provider",
                            photoFile
                        )
                        tempImageUri = uri
                        cameraLauncher.launch(uri)
                    }) {
                    Text(text = "Camera")
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = PlantGreen),
                    modifier = Modifier
                        .padding(end = SmallMediumPadding)
                        .width(LargeWidth),
                    onClick = {
                        showImageSourceDialog = false
                        filePickerLauncher.launch("image/*")
                    }) {
                    Text(text = "File")
                }
            }
        )
    }


}