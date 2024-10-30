package com.pandroid.greenhaven.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pandroid.greenhaven.presentation.component.ScaffoldWithBottomNavBar
import com.pandroid.greenhaven.presentation.screens.auth.ForgotPasswordScreen
import com.pandroid.greenhaven.presentation.screens.cart.CartScreen
import com.pandroid.greenhaven.presentation.screens.fav.FavoriteScreen
import com.pandroid.greenhaven.presentation.screens.getStarted.GetStartedScreen
import com.pandroid.greenhaven.presentation.screens.home.HomeScreen
import com.pandroid.greenhaven.presentation.screens.auth.LoginScreen
import com.pandroid.greenhaven.presentation.screens.profile.ProfileScreen
import com.pandroid.greenhaven.presentation.screens.auth.SignupScreen
import com.pandroid.greenhaven.presentation.screens.booking.BookingScreen
import com.pandroid.greenhaven.presentation.screens.booking.ViewAllBookings
import com.pandroid.greenhaven.presentation.screens.component.AboutUs
import com.pandroid.greenhaven.presentation.screens.component.PrivacyPolicy
import com.pandroid.greenhaven.presentation.screens.createPlant.AddPlantScreen
import com.pandroid.greenhaven.presentation.screens.createPlant.EachPlantScreen
import com.pandroid.greenhaven.presentation.screens.profile.UpdateScreen
import com.pandroid.greenhaven.presentation.splash.SplashScreen

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier, startDestination: String) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable(Routes.Splash.route) {
            SplashScreen()
        }

        composable(Routes.GetStarted.route) {
            GetStartedScreen(navController = navController)
        }

        composable(Routes.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(Routes.SignUp.route) {
            SignupScreen(navController = navController)
        }

        composable(Routes.Forgot.route) {
            ForgotPasswordScreen(navController = navController)
        }

        composable(Routes.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(Routes.Fav.route) {
            ScaffoldWithBottomNavBar(navController = navController) {
                FavoriteScreen(navController = navController)
            }
        }

        composable(Routes.Search.route) {
            ScaffoldWithBottomNavBar(navController = navController) {
                /*SearchScreen(navController = navController)*/
                AddPlantScreen(navController = navController)
            }
        }

        composable(Routes.Cart.route) {
            ScaffoldWithBottomNavBar(navController = navController) {
                CartScreen(navController = navController)
            }
        }

        composable(Routes.Profile.route) {
            ScaffoldWithBottomNavBar(navController = navController) {
                ProfileScreen(navController = navController)
            }
        }

        composable(
            Routes.Each.route,
            arguments = listOf(navArgument("plantId") { defaultValue = "" })
        ) { backStackEntry ->
            val plantId = backStackEntry.arguments?.getString("plantId") ?: ""
            EachPlantScreen(navController, plantId =  plantId)
        }

        composable(
            Routes.Booking.route,
            arguments = listOf(
                navArgument("plantId") { defaultValue = "" },
                navArgument("count") { defaultValue = 1 }
            )
        ) { backStackEntry ->
            val plantId = backStackEntry.arguments?.getString("plantId") ?: ""
            val count = backStackEntry.arguments?.getInt("count") ?: 1
            BookingScreen(navController = navController, plantId = plantId, count = count)
        }

        composable(Routes.AllBooking.route) {
            ViewAllBookings(navController = navController)
        }

        composable(Routes.AboutUs.route) {
            AboutUs(navController = navController)
        }

        composable(Routes.PrivacyPolicy.route) {
            PrivacyPolicy(navController = navController)
        }

        composable(Routes.UpdateProfile.route) {
            UpdateScreen(navController = navController)
        }

    }

}