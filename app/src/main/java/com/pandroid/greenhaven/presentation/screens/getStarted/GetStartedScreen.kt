package com.pandroid.greenhaven.presentation.screens.getStarted

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.pandroid.greenhaven.R
import com.pandroid.greenhaven.presentation.navigation.Routes
import com.pandroid.greenhaven.presentation.splash.SplashViewModel
import com.pandroid.greenhaven.presentation.utils.Dimens.ExtraLargePadding
import com.pandroid.greenhaven.presentation.utils.Dimens.ExtraLargeTextSize
import com.pandroid.greenhaven.presentation.utils.Dimens.ExtraLargeWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.LargePadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumRoundedCorner
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumTextSize
import com.pandroid.greenhaven.presentation.utils.Dimens.VeryExtraLargePadding
import com.pandroid.greenhaven.ui.theme.Manrope
import com.pandroid.greenhaven.ui.theme.PlantGreen
import org.koin.androidx.compose.getViewModel


@Composable
fun GetStartedScreen(
    navController: NavController,
    splashViewModel: SplashViewModel = getViewModel()
) {
    Box(modifier = Modifier.fillMaxSize()) {

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

        Text(
            text = "Find Plants",
            fontSize = ExtraLargeTextSize,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = VeryExtraLargePadding)
        )

        // Text at the center
        Text(
            text = "\" Suggests a safe and vibrant\n \t \t \t \t place for plant lovers \n  to find their perfect plants.\"",
            fontSize = MediumTextSize,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = ExtraLargePadding)
        )

        // Button at the bottom
        Button(
            onClick = {
                splashViewModel.completeOnboarding()
                navController.navigate(Routes.Home.route)
            },
            colors = ButtonDefaults.buttonColors(containerColor = PlantGreen),
            shape = RoundedCornerShape(MediumRoundedCorner),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = LargePadding)
                .height(MediumHeight)
                .width(ExtraLargeWidth)
        ) {
            Text(text = "Get Started", fontSize = MediumTextSize, fontFamily = Manrope)
        }

    }

}

