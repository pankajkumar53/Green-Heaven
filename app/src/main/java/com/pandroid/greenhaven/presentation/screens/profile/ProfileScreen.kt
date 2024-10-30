package com.pandroid.greenhaven.presentation.screens.profile

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pandroid.greenhaven.R
import com.pandroid.greenhaven.data.resource.Result
import com.pandroid.greenhaven.domain.model.UserModel
import com.pandroid.greenhaven.presentation.component.CustomButton
import com.pandroid.greenhaven.presentation.navigation.Routes
import com.pandroid.greenhaven.presentation.screens.auth.AuthViewModel
import com.pandroid.greenhaven.presentation.screens.component.AppBar
import com.pandroid.greenhaven.presentation.utils.Dimens
import com.pandroid.greenhaven.ui.theme.Black
import com.pandroid.greenhaven.ui.theme.LightGray
import com.pandroid.greenhaven.ui.theme.Manrope
import com.pandroid.greenhaven.ui.theme.PlantGreen
import com.pandroid.greenhaven.ui.theme.White
import org.koin.androidx.compose.getViewModel

@Composable
fun ProfileScreen(navController: NavController, viewModel: AuthViewModel = getViewModel()) {

    LaunchedEffect(Unit) {
        viewModel.getUser()
    }

    val userState by viewModel.userState.collectAsState()
    val userData = when (userState) {
        is Result.Success -> (userState as Result.Success<UserModel>).data
        else -> null
    }
    val scrollState  = rememberScrollState()

    var showDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        AppBar(navController = navController, text = "Your Profile")

        if (userData != null) {
            UserSection(userModel = userData)
        }

        EachSection(
            icon = painterResource(id = R.drawable.ic_edit),
            text = "Edit Profile",
            navController = navController,
            routes = Routes.UpdateProfile.route
        )

        EachSection(
            icon = painterResource(id = R.drawable.ic_history),
            text = "Booking History",
            navController = navController,
            routes = Routes.AllBooking.route
        )

        EachSection(
            icon = painterResource(id = R.drawable.ic_share),
            text = "Share App",
            navController = navController,
            routes = Routes.ShareApp.route
        )

        EachSection(
            icon = painterResource(id = R.drawable.ic_policy),
            text = "Privacy Policy",
            navController = navController,
            routes = Routes.PrivacyPolicy.route
        )

        EachSection(
            icon = painterResource(id = R.drawable.ic_mail),
            text = "Contact Us",
            navController = navController,
            routes = Routes.Contact.route
        )

        EachSection(
            icon = painterResource(id = R.drawable.ic_info),
            text = "About Us",
            navController = navController,
            routes = Routes.AboutUs.route
        )

        // Log out button to show confirmation dialog
        CustomButton(
            modifier = Modifier
                .padding(start = Dimens.SmallMediumPadding, end = Dimens.SmallMediumPadding, top = Dimens.MediumLargeSmallPadding)
                .fillMaxWidth()
                .height(Dimens.MediumHeight),
            onClick = {
                showDialog = true
            },
            text = "Log Out"
        )

        Spacer(modifier = Modifier.height(Dimens.LargeHeight))

    }


    // Display logout confirmation dialog
    if (showDialog) {
        LogOutConfirmationDialog(
            onConfirm = {
                 viewModel.logout()
                if (context is Activity) {
                    context.finishAffinity()
                }
            },
            onDismiss = {
                showDialog = false
            }
        )

    }

}

@Composable
fun EachSection(
    icon: Painter,
    text: String,
    navController: NavController,
    routes: String,
    context: Context = LocalContext.current
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.MediumLargeLargeHeight)
            .padding(start = Dimens.SmallMediumPadding, end = Dimens.SmallMediumPadding, top = Dimens.VerySmallMediumPadding)
            .clickable {
                when (routes) {
                    Routes.ShareApp.route -> {
                        // Share App logic
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            val shareMessage =
                                "Check out this amazing app: https://play.google.com/store/apps/details?id=${context.packageName}"
                            putExtra(Intent.EXTRA_TEXT, shareMessage)
                        }
                        context.startActivity(
                            Intent.createChooser(shareIntent, "Share App via")
                        )
                    }

                    Routes.Contact.route -> {
                        // Contact Us logic (open email client)
                        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:appsupport@gmail.com")
                            putExtra(Intent.EXTRA_SUBJECT, "Support Request")
                            putExtra(Intent.EXTRA_TEXT, "Hello, I need assistance with...")
                        }
                        try {
                            context.startActivity(emailIntent)
                        } catch (e: ActivityNotFoundException) {
                            Toast
                                .makeText(context, "No email client found", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    else -> {
                        // For other routes, navigate as usual
                        navController.navigate(routes)
                    }
                }
            }
            .border(width = Dimens.ExtraSmallWidth, shape = RoundedCornerShape(Dimens.MediumSmallRoundedCorner), color = LightGray)
    ) {
        Text(
            text = text,
            color = Black,
            fontSize = Dimens.MediumTextSize,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(start = Dimens.LargeMediumPadding, top = Dimens.MediumLargeSmallPadding)
        )

        Image(
            painter = icon,
            contentDescription = null,
            colorFilter = ColorFilter.tint(Black),
            modifier = Modifier
                .padding(start = Dimens.MediumLargeSmallPadding, top = Dimens.MediumSmallPadding)
                .size(Dimens.SmallSize)
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Arrow Icon",
            tint = Black,
            modifier = Modifier
                .padding(start = Dimens.VeryVeryExtraLargePadding, top = Dimens.MediumSmallPadding)
                .size(Dimens.SmallMediumSize)
        )
    }
}

@Composable
fun UserSection(userModel: UserModel) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = PlantGreen, shape = RoundedCornerShape(Dimens.SmallMediumRoundedCorner))
    ) {

        AsyncImage(
            model = userModel.img,
            placeholder = painterResource(id = R.drawable.ic_person),
            contentDescription = "default profile image",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(Dimens.MediumExtraLargeSize)
                .clip(CircleShape)

        )

        Text(
            text = userModel.name,
            color = White,
            fontSize = Dimens.MediumTextSize,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = userModel.phoneNumber,
            fontSize = Dimens.MediumSmallTextSize,
            fontWeight = FontWeight.Normal,
            color = LightGray,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = userModel.email,
            fontSize = Dimens.MediumSmallTextSize,
            color = LightGray,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(Dimens.SmallHeight))

    }

}


@Composable
fun LogOutConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .padding(Dimens.SmallMediumPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.VeryExtraLargeHeight)
                .clip(RoundedCornerShape(Dimens.MediumSmallRoundedCorner)),
            colors = CardDefaults.cardColors(White),
            elevation = CardDefaults.elevatedCardElevation(Dimens.SmallElevation)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = PlantGreen)
                    .height(Dimens.LargeHeight)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(Dimens.MediumExtraSmallLargeSize)
                        .align(Alignment.TopCenter)
                        .padding(top = Dimens.SmallMediumPadding)
                )

                Text(
                    text = "Log Out",
                    fontSize = Dimens.LargeTextSize,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(top = Dimens.SmallPadding, bottom = Dimens.SmallPadding),
                    color = White
                )
            }

            Text(
                text = "Do you really want to log out? We will miss you... You can still cancel the process, or click logout to proceed.",
                fontSize = Dimens.SmallTextSize,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = Dimens.SmallPadding, end = Dimens.SmallPadding, top = Dimens.SmallMediumPadding, bottom = Dimens.SmallMediumPadding)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                CustomButton(
                    modifier = Modifier
                        .padding(Dimens.SmallMediumPadding)
                        .weight(1f)
                        .height(Dimens.MediumHeight),
                    onClick = {
                        onConfirm()
                    },
                    text = "Log Out"
                )


                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = White,
                        contentColor = PlantGreen
                    ),
                    modifier = Modifier
                        .padding(Dimens.SmallMediumPadding)
                        .weight(1f)
                        .height(Dimens.MediumHeight)
                        .border(Dimens.ExtraSmallWidth, PlantGreen, shape = RoundedCornerShape(Dimens.MediumSmallRoundedCorner)),
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(text = "Cancel", fontFamily = Manrope, fontSize = Dimens.MediumSmallTextSize)
                }
            }
        }
    }
}





