package com.pandroid.greenhaven.presentation.screens.home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.pandroid.greenhaven.R
import com.pandroid.greenhaven.data.resource.Result
import com.pandroid.greenhaven.domain.model.UserModel
import com.pandroid.greenhaven.presentation.component.ScaffoldWithBottomNavBar
import com.pandroid.greenhaven.presentation.navigation.Routes
import com.pandroid.greenhaven.presentation.screens.auth.AuthViewModel
import com.pandroid.greenhaven.presentation.screens.createPlant.AllPlantsScreen
import com.pandroid.greenhaven.presentation.screens.home.sideBar.Sidebar
import com.pandroid.greenhaven.presentation.utils.Dimens.LargeMediumWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.LargeSmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargeSmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargeSmallSize
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumMediumHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallRoundedCorner
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallSize
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallLargeHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallMediumTextSize
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.VeryExtraSmallWidth
import com.pandroid.greenhaven.ui.theme.Black
import com.pandroid.greenhaven.ui.theme.ExtraDarkGray
import com.pandroid.greenhaven.ui.theme.LightGray
import com.pandroid.greenhaven.ui.theme.Manrope
import com.pandroid.greenhaven.ui.theme.PlantGreen
import com.pandroid.greenhaven.ui.theme.White
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun HomeScreen(
    navController: NavController = rememberNavController(),
    homeViewModel: HomeViewModel = getViewModel(),
    authViewModel: AuthViewModel = getViewModel()
) {

    val activity = LocalContext.current as? Activity
    var showDialog by remember { mutableStateOf(false) }

    BackHandler {
        showDialog = true
    }

    // Display AlertDialog if `showDialog` is true
    if (showDialog) {

        AlertDialog(
            containerColor = White,
            onDismissRequest = { showDialog = false },
            title = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.icon),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(bottom = LargeSmallPadding)
                            .size(MediumLargeSmallSize)
                            .clip(CircleShape)
                    )


                    Text(
                        text = "Exit Application",
                        modifier = Modifier.align(Alignment.BottomCenter),
                        fontFamily = Manrope,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Text(
                    "Do you want to Exit Application?",
                    fontSize = SmallMediumTextSize,
                    fontFamily = Manrope,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = PlantGreen),
                    modifier = Modifier
                        .padding(end = MediumLargeSmallPadding)
                        .width(LargeMediumWidth)
                        .border(
                            shape = RoundedCornerShape(MediumSmallRoundedCorner),
                            width = MediumWidth,
                            color = PlantGreen
                        ),
                    shape = RoundedCornerShape(MediumSmallRoundedCorner),
                    onClick = {
                        activity?.finish()
                    }) {
                    Text(text = "Exit App", fontFamily = Manrope)
                }

            },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    modifier = Modifier
                        .padding(end = SmallPadding)
                        .width(LargeMediumWidth)
                        .border(
                            shape = RoundedCornerShape(MediumSmallRoundedCorner),
                            width = MediumSmallWidth,
                            color = PlantGreen
                        ),
                    shape = RoundedCornerShape(MediumSmallRoundedCorner),
                    onClick = { showDialog = false }) {
                    Text("Cancel", color = Black, fontFamily = Manrope)
                }

            }
        )


    }


    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()



    LaunchedEffect(Unit) {
        authViewModel.getUser()
    }

    // Observe the user state
    val userState by authViewModel.userState.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Sidebar(
                onClose = {
                    scope.launch { drawerState.close() }
                },
                navController = navController
            )
        }
    ) {
        ScaffoldWithBottomNavBar(navController = navController) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MediumSmallPadding)
            )
            {

                TopSection(navController = navController, userState = userState, onClick = {
                    scope.launch { drawerState.open() }
                })

                Spacer(modifier = Modifier.height(MediumMediumHeight))

                Text(
                    text = "Let’s Find\n" +
                            "Your Plants!", style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    fontFamily = Manrope
                )

                Spacer(modifier = Modifier.height(SmallLargeHeight))

                AllPlantsScreen(navController = navController)

            }
        }


    }

}

@Composable
fun TopSection(navController: NavController, userState: Result<UserModel>?, onClick: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(MediumSmallHeight)
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_sidebar),
            contentDescription = "sidebar icon",
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(MediumSmallSize)
                .border(
                    color = PlantGreen,
                    width = VeryExtraSmallWidth,
                    shape = RoundedCornerShape(MediumSmallRoundedCorner)
                )
                .padding(SmallPadding)
                .clickable { onClick() }
        )

        val userData = when (userState) {
            is Result.Success -> userState.data
            else -> null
        }

        if (userData != null) {
            AsyncImage(
                model = userData.img,
                contentDescription = "User profile image",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = SmallPadding)
                    .size(MediumSmallSize)
                    .clip(CircleShape)
                    .clickable {
                        navController.navigate(Routes.Profile.route)
                    }
            )
        } else {
            AsyncImage(
                model = R.drawable.ic_person,
                colorFilter = ColorFilter.tint(Black),
                placeholder = painterResource(id = R.drawable.ic_person),
                contentDescription = "default profile image",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(MediumSmallSize)
                    .border(color = PlantGreen, width = VeryExtraSmallWidth, shape = CircleShape)
                    .clickable {
                        navController.navigate(Routes.Login.route)
                    }
            )
        }

    }

}





