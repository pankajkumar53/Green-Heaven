package com.pandroid.greenhaven.presentation.screens.auth

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.pandroid.greenhaven.R
import com.pandroid.greenhaven.data.resource.State
import com.pandroid.greenhaven.presentation.component.CustomButton
import com.pandroid.greenhaven.presentation.component.CustomCircularIndicator
import com.pandroid.greenhaven.presentation.component.CustomTextField
import com.pandroid.greenhaven.presentation.navigation.Routes
import com.pandroid.greenhaven.presentation.screens.component.BackGroundImg
import com.pandroid.greenhaven.presentation.utils.Dimens.ExtraSmallWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.LargeRoundedCorner
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargeSmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallRoundedCorner
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallSize
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallLargeHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallPadding
import com.pandroid.greenhaven.ui.theme.Manrope
import com.pandroid.greenhaven.ui.theme.PlantGreen
import com.pandroid.greenhaven.ui.theme.White
import org.koin.androidx.compose.getViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel = getViewModel()) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Collect the state from the ViewModel
    val state by viewModel.state.collectAsState()

    BackGroundImg(navController = navController) {

        Text(
            text = "Welcome Back!", style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ), fontFamily = Manrope
        )

        Text(
            text = "Login to your Account", style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Normal
            ), color = Color.LightGray
        )

        Spacer(modifier = Modifier.height(SmallLargeHeight))


        // Email input field
        CustomTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            keyboardType = KeyboardType.Email
        )

        // Password input field
        CustomTextField(value = password,
            onValueChange = { password = it },
            label = "Password",
            isPassword = true,
            isPasswordVisible = isPasswordVisible,
            onPasswordToggleClick = { isPasswordVisible = !isPasswordVisible }
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Forgot Password?", modifier = Modifier.clickable {
                navController.navigate(Routes.Forgot.route)
            }.align(AbsoluteAlignment.Right))
        }

        Spacer(modifier = Modifier.height(SmallLargeHeight))

        // Sign Up Button
        CustomButton(
            text = "Login", onClick = {
                if (email != "" && password != "") {
                    viewModel.loginUser(
                        email = email,
                        password = password,
                    )
                } else {
                    Toast.makeText(context, "Please fill All fields", Toast.LENGTH_SHORT).show()
                }
            }, modifier = Modifier
                .height(MediumHeight)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(SmallLargeHeight))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(text = "Don't Have an Account?", fontWeight = FontWeight.SemiBold)
            Text(text = " Register.",
                color = PlantGreen,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable {
                    navController.navigate(Routes.SignUp.route)
                })
        }

        Spacer(modifier = Modifier.height(MediumSmallHeight))


    }

    // Display different UI based on the state
    when (state) {
        is State.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CustomCircularIndicator()
            }
        }

        is State.Success -> {
            LaunchedEffect(Unit) {
                email = ""
                password = ""
                navController.navigate(Routes.Home.route) {
                    popUpTo(Routes.Login.route) {
                        inclusive = true
                    }
                }
            }
        }

        is State.Error -> {
            LaunchedEffect(Unit) {
                Toast.makeText(
                    context,
                    (state as State.Error).message ?: "An error occurred",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        else -> Unit
    }


}

/*Suggests a safe and vibrant place for plant lovers to find their perfect plants.*/
