package com.pandroid.greenhaven.presentation.screens.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.pandroid.greenhaven.R
import com.pandroid.greenhaven.presentation.utils.Dimens.ExtraSmallWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.LargeRoundedCorner
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargeSmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallRoundedCorner
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallSize
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallPadding
import com.pandroid.greenhaven.ui.theme.White


@Composable
fun BackGroundImg(navController: NavController, content: @Composable () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.get_started),
            contentDescription = "background image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Dark overlay to make the background image darker
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )

        Icon(
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = "back",
            tint = White,
            modifier = Modifier
                .padding(MediumSmallPadding)
                .size(MediumSmallSize)
                .border(
                    width = ExtraSmallWidth,
                    color = White,
                    shape = RoundedCornerShape(MediumSmallRoundedCorner)
                )
                .background(
                    Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(MediumSmallRoundedCorner)
                )
                .clickable {
                    navController.popBackStack()
                }
                .padding(SmallPadding)
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(
                    color = White, shape = RoundedCornerShape(
                        topStart = LargeRoundedCorner,
                        topEnd = LargeRoundedCorner
                    )
                )
                .padding(MediumLargeSmallPadding)
        ) {
            content()
        }
    }

}