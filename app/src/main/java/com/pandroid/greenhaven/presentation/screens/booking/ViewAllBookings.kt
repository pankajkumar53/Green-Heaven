package com.pandroid.greenhaven.presentation.screens.booking

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pandroid.greenhaven.R
import com.pandroid.greenhaven.data.resource.State
import com.pandroid.greenhaven.domain.model.BookingModel
import com.pandroid.greenhaven.domain.model.PlantModel
import com.pandroid.greenhaven.presentation.component.CustomCircularIndicator
import com.pandroid.greenhaven.presentation.navigation.Routes
import com.pandroid.greenhaven.presentation.screens.component.AppBar
import com.pandroid.greenhaven.presentation.screens.createPlant.PlantViewModel
import com.pandroid.greenhaven.presentation.utils.Dimens.ExtraLargeHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.ExtraSmallSize
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargeTextSize
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallRoundedCorner
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallTextSize
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumTextSize
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallElevation
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallMediumPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallMediumRoundedCorner
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallTextSize
import com.pandroid.greenhaven.presentation.utils.Dimens.VerySmallMediumPadding
import com.pandroid.greenhaven.ui.theme.ExtraDarkGray
import com.pandroid.greenhaven.ui.theme.Manrope
import com.pandroid.greenhaven.ui.theme.PlantGreen
import com.pandroid.greenhaven.ui.theme.White
import org.koin.androidx.compose.getViewModel

@Composable
fun ViewAllBookings(
    navController: NavController,
    viewModel: BookingViewModel = getViewModel(),
    plantViewModel: PlantViewModel = getViewModel()
) {
    val plantList by plantViewModel.plantList.collectAsState()
    val state by viewModel.bookState.observeAsState(State.Idle())

    // Trigger loading bookings on Composable initialization
    LaunchedEffect(Unit) {
        viewModel.loadBookings()
        plantViewModel.loadPlant()
    }

    // Handle different states (Loading, Success, Error)
    when (state) {
        is State.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CustomCircularIndicator()
            }
        }

        is State.Success -> {
            val bookings = (state as State.Success<List<BookingModel>>).data
            Column(modifier = Modifier.fillMaxSize()) {

                AppBar(navController = navController, text = "All Order's")

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(bookings) { booking ->
                        val plant = plantList.find { it.plantId == booking.plantId }
                        if (plant != null) {
                            BookingItem(booking, plant, navController)
                        }
                    }
                }
            }
        }

        is State.Error -> {
            val errorMessage = (state as State.Error).message
            Box(modifier = Modifier.fillMaxSize()) {
                AppBar(navController = navController, text = "All Order's")
                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        fontSize = MediumLargeTextSize,
                        color = ExtraDarkGray,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .clickable {
                                navController.navigate(Routes.Login.route)
                            }
                    )
                }
            }
        }

        is State.Idle -> {
            Text(
                text = "No bookings available",
                modifier = Modifier.fillMaxSize(),
                style = MaterialTheme.typography.bodyMedium
            )
        }

    }
}


@Composable
fun BookingItem(booking: BookingModel, plant: PlantModel, navController: NavController) {
    // Customize this function to display each booking item
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(SmallPadding),
        elevation = CardDefaults.elevatedCardElevation(SmallElevation),
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(SmallMediumRoundedCorner)
    ) {
        Column(modifier = Modifier.padding(MediumSmallPadding)) {

            AsyncImage(
                model = plant.plantImage,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.place_holder),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ExtraLargeHeight)
                    .clip(RoundedCornerShape(MediumSmallRoundedCorner))
                    .clickable {
                        navController.navigate(Routes.Each.createRoute(plantId = plant.plantId))
                    }
            )

            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = plant.title,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterStart),
                    fontSize = MediumTextSize,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "$${plant.price}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = PlantGreen,
                        fontFamily = Manrope
                    ),
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }

            HorizontalDivider(modifier = Modifier.padding(top = SmallPadding, bottom = VerySmallMediumPadding))

            Row(modifier = Modifier.padding(start = SmallMediumPadding)) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(ExtraSmallSize)
                )
                Text(text = booking.address, modifier = Modifier.padding(start = SmallPadding))
            }

            HorizontalDivider(modifier = Modifier.padding(top = VerySmallMediumPadding, bottom = VerySmallMediumPadding))

            Text(
                text = "Booking Details",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = MediumSmallTextSize,
                fontWeight = FontWeight.SemiBold,
                color = PlantGreen
            )

            HorizontalDivider(modifier = Modifier.padding(top = VerySmallMediumPadding, bottom = VerySmallMediumPadding))


            Text(
                text = "Name: ${booking.name}",
                modifier = Modifier.padding(start = SmallPadding),
                fontWeight = FontWeight.SemiBold,
            )

            Text(
                text = "Mobile: ${booking.phoneNumber}",
                modifier = Modifier.padding(start = SmallPadding),
                fontWeight = FontWeight.SemiBold,
            )

            Text(
                text = "Email: ${booking.email}",
                modifier = Modifier.padding(start = SmallPadding),
                fontWeight = FontWeight.SemiBold,
            )


            Text(
                text = "This Plant is Ordered on ${booking.time}",
                fontSize = SmallTextSize,
                color = Gray,
                modifier = Modifier.padding(start = SmallPadding, bottom = SmallPadding)
            )


        }
    }
}

