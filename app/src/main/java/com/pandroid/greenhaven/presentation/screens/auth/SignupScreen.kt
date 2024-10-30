package com.pandroid.greenhaven.presentation.screens.auth

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.pandroid.greenhaven.R
import com.pandroid.greenhaven.data.resource.State
import com.pandroid.greenhaven.domain.model.UserModel
import com.pandroid.greenhaven.presentation.component.CustomButton
import com.pandroid.greenhaven.presentation.component.CustomCircularIndicator
import com.pandroid.greenhaven.presentation.component.CustomTextField
import com.pandroid.greenhaven.presentation.component.RoundImageWithIcon
import com.pandroid.greenhaven.presentation.navigation.Routes
import com.pandroid.greenhaven.presentation.screens.component.BackGroundImg
import com.pandroid.greenhaven.presentation.utils.Dimens.ExtraSmallWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.LargeRoundedCorner
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallLargeHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumExtraSmallSize
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallSize
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargeSmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallRoundedCorner
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallTextSize
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargeWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.ExtraSmallHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.VerySmallPadding
import com.pandroid.greenhaven.ui.theme.Manrope
import com.pandroid.greenhaven.ui.theme.PlantGreen
import com.pandroid.greenhaven.ui.theme.White
import org.koin.androidx.compose.getViewModel

@Composable
fun SignupScreen(navController: NavController, viewModel: AuthViewModel = getViewModel()) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val oneTapClient = remember { Identity.getSignInClient(context) }
    val signInRequest = remember {
        BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    val signInIntentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
            val idToken = credential.googleIdToken
            if (idToken != null) {
                viewModel.signInWithGoogle(idToken)
            } else {
                Toast.makeText(context, "No ID Token received", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Sign-in failed", Toast.LENGTH_SHORT).show()
        }
    }


    // Collect the state from the ViewModel
    val state by viewModel.state.collectAsState()
    val googleSignIn by viewModel.googleSignIn.collectAsState()



    BackGroundImg(navController = navController) {


        Text(
            text = "Register",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            fontFamily = Manrope
        )

        Text(
            text = "Create your new Account",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Normal
            ),
            color = Color.LightGray
        )

        Spacer(modifier = Modifier.height(ExtraSmallHeight))

        // Image picker (Placeholder image for now)
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            RoundImageWithIcon(
                selectedImageUri = imageUri,
                onImageSelected = { uri -> imageUri = uri }
            )
        }


        // Name input field
        CustomTextField(
            value = name,
            onValueChange = { name = it },
            label = "Name"
        )

        // Email input field
        CustomTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            keyboardType = KeyboardType.Email
        )

        // Phone input field
        CustomTextField(
            value = phone,
            onValueChange = { phone = it },
            label = "Phone",
            keyboardType = KeyboardType.Phone
        )

        // Password input field
        CustomTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            isPassword = true,
            isPasswordVisible = isPasswordVisible,
            onPasswordToggleClick = { isPasswordVisible = !isPasswordVisible }
        )

        Spacer(modifier = Modifier.height(SmallLargeHeight))

        // Sign Up Button
        CustomButton(
            text = "Register",
            onClick = {
                if (name != "" && email != "" && password != "" && imageUri != null) {
                    viewModel.signUpUser(
                        userModel = UserModel(
                            name = name,
                            email = email,
                            phoneNumber = phone
                        ),
                        password,
                        img = imageUri!!
                    )
                } else {
                    Toast.makeText(context, "Please fill All fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .height(MediumHeight)
                .fillMaxWidth()
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "OR",
                fontSize = MediumSmallTextSize,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = MediumLargeSmallPadding)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SmallPadding),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "google logo",
                modifier = Modifier
                    .padding(top = VerySmallPadding)
                    .size(MediumExtraSmallSize)
                    .clickable {
                        oneTapClient
                            .beginSignIn(signInRequest)
                            .addOnSuccessListener { result ->
                                try {
                                    signInIntentLauncher.launch(
                                        IntentSenderRequest
                                            .Builder(
                                                result.pendingIntent.intentSender
                                            )
                                            .build()
                                    )

                                } catch (e: Exception) {
                                    Log.e(
                                        "OneTap",
                                        "Couldn't start One Tap UI: ${e.localizedMessage}"
                                    )
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.e("OneTap", "One Tap failed: ${e.localizedMessage}")
                            }
                    }
            )

            Spacer(modifier = Modifier.width(MediumLargeWidth))

            Image(
                painter = painterResource(id = R.drawable.ic_facebook),
                contentDescription = "facebook logo",
                modifier = Modifier
                    .padding(top = VerySmallPadding)
                    .size(MediumExtraSmallSize)
            )

            Spacer(modifier = Modifier.width(MediumLargeWidth))

            Image(
                painter = painterResource(id = R.drawable.ic_twitter),
                contentDescription = "twitter logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(MediumSmallSize)
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(text = "Already Have an Account?", fontWeight = FontWeight.SemiBold)
            Text(
                text = " Login.",
                color = PlantGreen,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable {
                    navController.navigate(Routes.Login.route)
                })
        }

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
                imageUri = null
                name = ""
                email = ""
                phone = ""
                password = ""
                navController.navigate(Routes.Home.route) {
                    popUpTo(Routes.SignUp.route) {
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

    when (googleSignIn) {
        is State.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CustomCircularIndicator()
            }
        }

        is State.Success -> {
            LaunchedEffect(Unit) {
                imageUri = null
                name = ""
                email = ""
                phone = ""
                password = ""
                navController.navigate(Routes.Home.route) {
                    popUpTo(Routes.SignUp.route) {
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
