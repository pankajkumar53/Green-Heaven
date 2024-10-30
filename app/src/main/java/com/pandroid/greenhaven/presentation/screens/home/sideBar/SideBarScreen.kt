package com.pandroid.greenhaven.presentation.screens.home.sideBar

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.pandroid.greenhaven.R
import com.pandroid.greenhaven.presentation.navigation.Routes
import com.pandroid.greenhaven.presentation.utils.Dimens.LargeExtraWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.LargeSmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumExtraLargeSize
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargePadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargeSmallWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallSize
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallExtraMediumSize
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallMediumHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallMediumPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.VeryExtraSmallSize
import com.pandroid.greenhaven.presentation.utils.Dimens.VerySmallMediumPadding
import com.pandroid.greenhaven.ui.theme.Black
import com.pandroid.greenhaven.ui.theme.White

@Composable
fun Sidebar(onClose: () -> Unit, navController: NavController) {
    Column(
        modifier = Modifier
            .width(LargeExtraWidth)
            .fillMaxHeight()
            .background(White)
            .padding(MediumSmallPadding),
        verticalArrangement = Arrangement.spacedBy(SmallMediumPadding)
    ) {

        Row(modifier = Modifier.padding(top = MediumLargePadding)) {
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    modifier = Modifier.size(MediumSmallSize),
                    tint = Black
                )
            }

            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = LargeSmallPadding)
                    .size(MediumExtraLargeSize)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(SmallMediumHeight))

        SideBarContent(
            icons = painterResource(id = R.drawable.ic_fav_outline),
            title = "Favorites",
            navController = navController,
            route = Routes.Fav.route
        )

        SideBarContent(
            icons = painterResource(id = R.drawable.ic_booking),
            title = "My bookings",
            navController = navController,
            route = Routes.AllBooking.route
        )

        SideBarContent(
            icons = painterResource(id = R.drawable.ic_profile_outline),
            title = "My Profile", navController = navController, route = Routes.Profile.route
        )

        SideBarContent(
            icons = painterResource(id = R.drawable.ic_share),
            title = "Share App",
            navController = navController,
            route = Routes.ShareApp.route
        )

        SideBarContent(
            icons = painterResource(id = R.drawable.ic_star_outline),
            title = "Rate Us",
            navController = navController,
            route = Routes.Home.route
        )

        SideBarContent(
            icons = painterResource(id = R.drawable.ic_privacy),
            title = "Privacy Policy",
            navController = navController,
            route = Routes.PrivacyPolicy.route
        )

        SideBarContent(
            icons = painterResource(id = R.drawable.ic_mail),
            title = "Contact Us",
            navController = navController,
            route = Routes.Contact.route
        )

        SideBarContent(
            icons = painterResource(id = R.drawable.ic_more),
            title = "About us",
            navController = navController,
            route = Routes.AboutUs.route
        )

    }

}

@Composable
fun SideBarContent(
    navController: NavController,
    icons: Painter,
    title: String,
    route: String,
    context: Context = LocalContext.current
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(MediumSmallHeight)
            .padding(bottom = VerySmallMediumPadding, start = SmallMediumPadding)
            .clickable {
                when (route) {
                    Routes.ShareApp.route -> {
                        // Share app logic
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
                        // Contact us logic (open email)
                        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:devpankajkumarcse@gmail.com")
                            putExtra(Intent.EXTRA_SUBJECT, "Support Request")
                            putExtra(Intent.EXTRA_TEXT, "Hello, I need assistance with...")
                        }
                        try {
                            context.startActivity(emailIntent)
                        } catch (e: ActivityNotFoundException) {
                            // Handle the case where no email client is installed
                            Toast
                                .makeText(context, "No email client found", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    else -> {
                        // For other routes, navigate as usual
                        navController.navigate(route)
                    }
                }
            }
    ) {
        Image(
            painter = icons,
            contentDescription = "Icon",
            colorFilter = ColorFilter.tint(Black),
            modifier = Modifier.size(SmallExtraMediumSize)
        )

        Spacer(modifier = Modifier.width(MediumWidth))

        Text(text = title, color = Black, fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.width(MediumLargeSmallWidth))

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = Black,
            modifier = Modifier
                .padding(top = VerySmallMediumPadding)
                .size(VeryExtraSmallSize)
        )
    }
}

