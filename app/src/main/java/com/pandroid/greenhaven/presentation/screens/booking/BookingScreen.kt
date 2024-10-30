package com.pandroid.greenhaven.presentation.screens.booking

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pandroid.greenhaven.R
import com.pandroid.greenhaven.data.resource.Result
import com.pandroid.greenhaven.data.resource.State
import com.pandroid.greenhaven.domain.model.BookingModel
import com.pandroid.greenhaven.domain.model.PlantModel
import com.pandroid.greenhaven.domain.model.UserModel
import com.pandroid.greenhaven.presentation.component.CustomButton
import com.pandroid.greenhaven.presentation.component.CustomCircularIndicator
import com.pandroid.greenhaven.presentation.component.CustomTextField
import com.pandroid.greenhaven.presentation.navigation.Routes
import com.pandroid.greenhaven.presentation.screens.auth.AuthViewModel
import com.pandroid.greenhaven.presentation.screens.component.AppBar
import com.pandroid.greenhaven.presentation.screens.createPlant.PlantViewModel
import com.pandroid.greenhaven.presentation.utils.Dimens.ExtraLargeSmallWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumRoundedCorner
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallRoundedCorner
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallMediumPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.VeryExtraSmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.VerySmallMediumPadding
import com.pandroid.greenhaven.ui.theme.LightGray
import com.pandroid.greenhaven.ui.theme.Manrope
import com.pandroid.greenhaven.ui.theme.PlantGreen
import com.pandroid.greenhaven.ui.theme.White
import org.koin.androidx.compose.getViewModel

@Composable
fun BookingScreen(
    navController: NavController,
    plantId: String,
    count: Int,
    viewModel: PlantViewModel = getViewModel(),
    authViewModel: AuthViewModel = getViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.loadPlant()
        authViewModel.getUser()
    }

    val userState by authViewModel.userState.collectAsState()

    val user = when (userState) {
        is Result.Success -> (userState as Result.Success<UserModel>).data
        else -> null
    }

    val plantList by viewModel.plantList.collectAsState()

    val plant = plantList?.find { it.plantId == plantId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        AppBar(navController = navController, "Order Your Plant")

        if (user != null && plant != null) {
            BookingContent(userModel = user, plant = plant, navController = navController, count = count)
        }
    }
}


@Composable
fun BookingContent(
    navController: NavController,
    userModel: UserModel,
    plant: PlantModel,
    count: Int ,
    viewModel: BookingViewModel = getViewModel()
) {

    val context = LocalContext.current

    var name by remember { mutableStateOf(userModel.name) }
    var email by remember { mutableStateOf(userModel.email) }
    var phone by remember { mutableStateOf(userModel.phoneNumber) }
    var address by remember { mutableStateOf("") }

    val state by viewModel.bookState.observeAsState(initial = State.Idle())

    when (state) {
        is State.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CustomCircularIndicator()
            }
        }

        is State.Success -> {
            LaunchedEffect(Unit) {
                Toast.makeText(context, "Thanks for ordering our plant!", Toast.LENGTH_SHORT).show()
                navController.navigate(Routes.Home.route)
            }
        }

        is State.Error -> {
            val errorMessage = (state as State.Error).message
            LaunchedEffect(Unit) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        else -> {
            // Handle other cases
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MediumSmallPadding)
    ) {

        AsyncImage(
            model = plant.plantImage,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.place_holder),
            modifier = Modifier
                .fillMaxHeight(.2f)
                .fillMaxWidth(.4f)
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(MediumRoundedCorner))
        )

        Spacer(modifier = Modifier.fillMaxHeight(0.05f))

        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Title", style = MaterialTheme.typography.titleMedium.copy(
                    color = PlantGreen, fontWeight = FontWeight.SemiBold, fontFamily = Manrope
                ), modifier = Modifier.align(Alignment.CenterStart)
            )


            Text(
                text = if (plant.title.length > 27) plant.title.take(24) + "..." else plant.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = PlantGreen, fontWeight = FontWeight.SemiBold, fontFamily = Manrope
                ),
                modifier = Modifier.align(Alignment.CenterEnd)
            )

        }

        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Quantity", style = MaterialTheme.typography.titleMedium.copy(
                    color = PlantGreen, fontWeight = FontWeight.SemiBold, fontFamily = Manrope
                ), modifier = Modifier.align(Alignment.CenterStart)
            )


            Text(
                text = "$count pc", style = MaterialTheme.typography.titleMedium.copy(
                    color = PlantGreen, fontWeight = FontWeight.SemiBold, fontFamily = Manrope
                ), modifier = Modifier.align(Alignment.CenterEnd)
            )

        }

        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Price", style = MaterialTheme.typography.titleMedium.copy(
                    color = PlantGreen, fontWeight = FontWeight.SemiBold, fontFamily = Manrope
                ), modifier = Modifier.align(Alignment.CenterStart)
            )


            Text(
                text = "$${plant.price}", style = MaterialTheme.typography.titleMedium.copy(
                    color = PlantGreen, fontWeight = FontWeight.SemiBold, fontFamily = Manrope
                ), modifier = Modifier.align(Alignment.CenterEnd)
            )

        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = VerySmallMediumPadding, top = VeryExtraSmallPadding),
            thickness = ExtraLargeSmallWidth,
            color = LightGray
        )

        Box(modifier = Modifier.fillMaxWidth()) {

            Text(
                text = "Total", style = MaterialTheme.typography.titleMedium.copy(
                    color = PlantGreen, fontWeight = FontWeight.ExtraBold, fontFamily = Manrope
                ), modifier = Modifier.align(Alignment.CenterStart)
            )


            Text(
                text = "$${plant.price.toInt() * count}",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = PlantGreen, fontWeight = FontWeight.ExtraBold, fontFamily = Manrope
                ),
                modifier = Modifier.align(Alignment.CenterEnd)
            )

        }

        Spacer(modifier = Modifier.fillMaxHeight(0.05f))

        CustomTextField(
            value = name, onValueChange = { name = it }, label = "Full Name"
        )

        CustomTextField(
            value = email, onValueChange = { email = it }, label = "Email"
        )

        CustomTextField(
            value = phone,
            onValueChange = { phone = it },
            label = "Phone Number",
            keyboardType = KeyboardType.Phone
        )

        CustomTextField(
            value = address,
            onValueChange = { address = it },
            label = "Enter Address",
            modifier = Modifier
                .fillMaxHeight(.5f)
                .fillMaxWidth(),
            singleLine = false
        )

        Spacer(modifier = Modifier.height(MediumSmallPadding))

        CustomButton(
            onClick = {
                if (email.isNotEmpty() && name.isNotEmpty() && phone.isNotEmpty() && phone != "No phone number" && address != "") {
                    viewModel.uploadBooking(
                        bookingModel = BookingModel(
                            email = email,
                            name = name,
                            phoneNumber = phone,
                            address = address,
                            plantId = plant.plantId,
                            userId = userModel.userId
                        )
                    )
                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .padding(top = SmallMediumPadding)
                .fillMaxWidth()
                .height(MediumSmallHeight)
                .background(shape = RoundedCornerShape(MediumSmallRoundedCorner), color = PlantGreen),
            text = "Order Plant"
        )

    }

}


