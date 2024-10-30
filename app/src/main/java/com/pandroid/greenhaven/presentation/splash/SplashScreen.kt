package com.pandroid.greenhaven.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.pandroid.greenhaven.R
import com.pandroid.greenhaven.presentation.utils.Dimens.LargeSize
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargeSmallPadding

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.plantGreen)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon),
            contentDescription = "icon",
            modifier = Modifier.padding(bottom = MediumLargeSmallPadding).size(LargeSize)
        )
    }
}