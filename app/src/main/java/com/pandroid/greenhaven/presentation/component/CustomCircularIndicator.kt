package com.pandroid.greenhaven.presentation.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallLargeSize
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumWidth
import com.pandroid.greenhaven.ui.theme.PlantGreen

@Composable
fun CustomCircularIndicator() {
    CircularProgressIndicator(
        modifier = Modifier.size(MediumSmallLargeSize),
        color = PlantGreen,
        strokeWidth = MediumWidth
    )
}