package com.pandroid.greenhaven.presentation.screens.component

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import coil.compose.rememberAsyncImagePainter
import com.pandroid.greenhaven.domain.model.PlantImageUrlModel
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.LargeMediumSize
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallMediumRoundedCorner
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallMediumTextSize
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.VerySmallPadding
import com.pandroid.greenhaven.ui.theme.Manrope
import com.pandroid.greenhaven.ui.theme.PlantGreen
import com.pandroid.greenhaven.ui.theme.White
import kotlinx.parcelize.RawValue

@Composable
fun MultiImagePicker(
    default: @RawValue ArrayList<PlantImageUrlModel>? = null,
    images: List<Uri>,
    onImagesPicked: (List<Uri>) -> Unit
) {
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents(),
            onResult = { uris -> onImagesPicked(uris) })


    Button(
        onClick = {
            launcher.launch("image/*")
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = MediumSmallPadding, end = MediumSmallPadding)
            .height(MediumSmallHeight),

        shape = RoundedCornerShape(SmallMediumRoundedCorner),
        colors = ButtonDefaults.buttonColors(containerColor = PlantGreen)
    ) {
        Text(
            text = "Add Plant Images",
            color = White,
            fontSize = SmallMediumTextSize,
            fontWeight = FontWeight.SemiBold,
            fontFamily = Manrope
        )
    }


    LazyRow(modifier = Modifier.padding(top = SmallPadding)) {

        if (default != null && default.isNotEmpty()) {
            items(default.size) { index ->
                Image(
                    painter = rememberAsyncImagePainter(model = default[index].imageUrl),
                    contentDescription = "Default Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(LargeMediumSize)
                        .padding(VerySmallPadding)
                )
            }
        }


        items(images.size) { index ->
            Image(
                painter = rememberAsyncImagePainter(images[index]),
                contentScale = ContentScale.Crop,
                contentDescription = "Selected Image",
                modifier = Modifier
                    .size(LargeMediumSize)
                    .padding(VerySmallPadding)
            )
        }
    }
}