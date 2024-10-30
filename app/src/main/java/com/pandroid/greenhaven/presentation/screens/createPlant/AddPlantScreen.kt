package com.pandroid.greenhaven.presentation.screens.createPlant

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.pandroid.greenhaven.data.resource.State
import com.pandroid.greenhaven.presentation.component.CustomButton
import com.pandroid.greenhaven.presentation.component.CustomCircularIndicator
import com.pandroid.greenhaven.presentation.component.CustomTextField
import com.pandroid.greenhaven.presentation.component.RectangleImageWithIcon
import com.pandroid.greenhaven.presentation.navigation.Routes
import com.pandroid.greenhaven.presentation.screens.component.CustomDropdown
import com.pandroid.greenhaven.presentation.screens.component.MultiImagePicker
import com.pandroid.greenhaven.presentation.utils.Dimens.LargeHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.ExtraLargeHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargeSmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallMediumHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.LargeWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallMediumRoundedCorner
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallPadding
import com.pandroid.greenhaven.utils.NodeRef.CATEGORY
import org.koin.androidx.compose.getViewModel


@Composable
fun AddPlantScreen(
    navController: NavController, viewModel: PlantViewModel = getViewModel()
) {

    val context = LocalContext.current

    // Local state for form inputs
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var title by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    var images by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val scrollState = rememberScrollState()

    // Observe the ViewModel's state
    val plantState by viewModel.state.collectAsState()


    // Call composable
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = MediumLargeSmallPadding)
            .verticalScroll(scrollState)
    ) {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(ExtraLargeHeight),
            contentAlignment = Alignment.Center
        ) {
            RectangleImageWithIcon(selectedImageUri = imageUri, onImageSelected = { uri ->
                imageUri = uri
            })
        }

        Spacer(modifier = Modifier.height(SmallMediumHeight))

        MultiImagePicker(images = images, onImagesPicked = { uris -> images = uris })

        Spacer(modifier = Modifier.height(SmallHeight))

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

        CustomDropdown(
            items = CATEGORY,
            selectedItem = category,
            onItemSelected = { selectedValue -> category = selectedValue },
            itemToString = { it },
            label = "Select Category",
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(modifier = Modifier.height(SmallMediumHeight))

        CustomButton(
            onClick = {
                if (imageUri != null && title != "" && rating != "" && description != "" && price != "" && category != "") {
                    viewModel.addPlant(
                        imageUri = imageUri!!,
                        title = title,
                        rating = rating,
                        description = description,
                        price = price,
                        category = category,
                        images = images,
                    )
                    // Reset form
                    imageUri = null
                    title = ""
                    rating = ""
                    description = ""
                    price = ""
                    images = emptyList()

                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            },
            text = "Add Plant",
            modifier = Modifier
                .height(MediumSmallHeight)
                .fillMaxWidth()
                .padding(start = MediumSmallPadding, end = MediumSmallPadding),
            shape = RoundedCornerShape(SmallMediumRoundedCorner)
        )

        Spacer(modifier = Modifier.height(LargeHeight))

    }

    when (plantState) {
        is State.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CustomCircularIndicator()
            }
        }

        is State.Success -> {
            LaunchedEffect(Unit) {
                Toast.makeText(context, "Plant Added Successfully", Toast.LENGTH_SHORT).show()
                navController.navigate(Routes.Home.route)
            }

        }

        is State.Error -> Text("Error: ${(plantState as State.Error).message}")
        else -> Unit
    }
}


@Composable
fun ProductForm(
    title: String,
    rating: String,
    description: String,
    price: String,
    onTitleChange: (String) -> Unit,
    onRatingChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = MediumLargeSmallPadding, end = MediumLargeSmallPadding)
    ) {

        CustomTextField(
            value = title,
            onValueChange = onTitleChange,
            label = "Title",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(SmallHeight))

        CustomTextField(
            value = rating,
            onValueChange = onRatingChange,
            label = "Rating",
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Number
        )

        CustomTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = "Description",
            singleLine = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(LargeWidth)
        )

        Spacer(modifier = Modifier.height(SmallPadding))

        CustomTextField(
            value = price,
            onValueChange = onPriceChange,
            label = "Price",
            keyboardType = KeyboardType.Number,
            modifier = Modifier.fillMaxWidth()
        )


    }
}


