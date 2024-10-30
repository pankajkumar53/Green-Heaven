package com.pandroid.greenhaven.presentation.screens.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pandroid.greenhaven.R
import com.pandroid.greenhaven.data.resource.Result
import com.pandroid.greenhaven.data.resource.State
import com.pandroid.greenhaven.domain.model.UserModel
import com.pandroid.greenhaven.presentation.component.CustomButton
import com.pandroid.greenhaven.presentation.component.CustomTextField
import com.pandroid.greenhaven.presentation.navigation.Routes
import com.pandroid.greenhaven.presentation.screens.auth.AuthViewModel
import com.pandroid.greenhaven.presentation.screens.component.AppBar
import com.pandroid.greenhaven.presentation.utils.Dimens
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargeTextSize
import com.pandroid.greenhaven.ui.theme.Black
import com.pandroid.greenhaven.ui.theme.ExtraDarkGray
import com.pandroid.greenhaven.ui.theme.LightGray
import com.pandroid.greenhaven.ui.theme.PlantGreen
import com.pandroid.greenhaven.ui.theme.White
import org.koin.androidx.compose.getViewModel

@Composable
fun UpdateScreen(
    navController: NavController,
    viewModel: AuthViewModel = getViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.getUser()
    }

    val userState by viewModel.userState.collectAsState()

    if (userState is Result.Success) {
        val userModel = (userState as Result.Success<UserModel>).data
        UpdateProfileForm(
            navController = navController,
            userModel = userModel,
            onUpdate = { updatedUser ->
                 viewModel.update(updatedUser)
                navController.navigate(Routes.Profile.route)
            })
    } else {
        userState?.let { LoadingOrErrorState(state = it, navController = navController) }
    }
}

@Composable
fun UpdateProfileForm(
    navController: NavController,
    userModel: UserModel,
    onUpdate: (UserModel) -> Unit
) {
    var name by remember { mutableStateOf(userModel.name) }
    var email by remember { mutableStateOf(userModel.email) }
    var phone by remember { mutableStateOf(userModel.phoneNumber) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .padding(Dimens.MediumSmallPadding)
    ) {
        ProfileImage(navController = navController, userModel = userModel)

        Spacer(modifier = Modifier.height(Dimens.SmallLargeHeight))

        CustomTextField(value = name, onValueChange = { name = it }, label = "Name")

        Spacer(modifier = Modifier.height(Dimens.SmallHeight))

        CustomTextField(value = email, onValueChange = { email = it }, label = "Email")

        Spacer(modifier = Modifier.height(Dimens.SmallHeight))

        CustomTextField(phone, onValueChange = { phone = it }, label = "Phone")

        Spacer(modifier = Modifier.height(Dimens.SmallMediumHeight))

        CustomButton(
            onClick = {
                onUpdate(
                    userModel.copy(
                        name = name,
                        email = email,
                        phoneNumber = phone
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.MediumHeight)
                .background(shape = RoundedCornerShape(Dimens.MediumSmallRoundedCorner), color = PlantGreen),
            text = "Update"
        )


    }

}

@Composable
fun ProfileImage(navController: NavController, userModel: UserModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.ExtraMediumLargeHeight)
    ) {
        AsyncImage(
            model = userModel.img,
            placeholder = painterResource(id = R.drawable.place_holder),
            contentDescription = "user image",
            modifier = Modifier
                .size(Dimens.LargeMediumSize)
                .clip(CircleShape)
                .align(Alignment.BottomCenter)
        )

        Icon(
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = "back",
            modifier = Modifier
                .padding(Dimens.SmallPadding)
                .size(Dimens.MediumSmallSize)
                .border(width = Dimens.ExtraSmallWidth, color = LightGray, shape = RoundedCornerShape(Dimens.MediumSmallRoundedCorner))
                .clickable {
                    navController.navigate(Routes.Profile.route)
                }
                .padding(Dimens.SmallPadding)
        )

    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Dimens.MediumSmallLargePadding)
            .height(Dimens.MediumMediumLargeHeight),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Update Your Account",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.MediumSmallPadding),
            fontSize = Dimens.LargeTextSize,
            fontWeight = FontWeight.SemiBold,
            color = PlantGreen,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Looks like you want to edit your \naccount or connect with social \naccounts",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimens.SmallPadding, start = Dimens.MediumSmallPadding, end = Dimens.MediumSmallPadding),
            fontSize = Dimens.SmallMediumTextSize,
            fontWeight = FontWeight.Normal,
            color = Black,
            textAlign = TextAlign.Center
        )
    }


}


@Composable
fun <T> LoadingOrErrorState(state: Result<T>, navController: NavController) {
    val context = LocalContext.current
    when (state) {
        is Result.Success -> {
            LaunchedEffect(Unit) {
                Toast.makeText(context, "Profile Updated Sucessefully", Toast.LENGTH_SHORT).show()

            }
        }

        is Result.Failure -> {
            val errorMessage = state.exception.localizedMessage
            Box(modifier = Modifier.fillMaxSize()) {
                AppBar(navController = navController, text = "Update Profile")

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
    }
}






