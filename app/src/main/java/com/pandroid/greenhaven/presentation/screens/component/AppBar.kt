package com.pandroid.greenhaven.presentation.screens.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallLargeHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumTextSize
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallMediumPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallSize
import com.pandroid.greenhaven.presentation.utils.Dimens.VerySmallMediumPadding
import com.pandroid.greenhaven.ui.theme.PlantGreen
import com.pandroid.greenhaven.ui.theme.White


@Composable
fun AppBar(navController: NavController, text: String) {
    Box(
        modifier = Modifier
            .height(MediumSmallLargeHeight)
            .fillMaxWidth()
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
                    .padding(top = MediumSmallPadding, start = VerySmallMediumPadding)
                    .size(SmallSize)
                    .clickable {
                        navController.popBackStack()
                    },
                tint = White
            )


            Text(
                text = text,
                fontSize = MediumTextSize,
                color = White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(
                    start = MediumSmallPadding,
                    top = MediumSmallPadding
                )
            )


        }
    }
}
