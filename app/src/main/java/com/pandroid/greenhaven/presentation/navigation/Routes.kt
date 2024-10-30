package com.pandroid.greenhaven.presentation.navigation


sealed class Routes(val route: String) {

    data object Splash : Routes("splash")

    data object GetStarted : Routes("get_started")

    data object SignUp : Routes("sign_up")

    data object Login : Routes("login")

    data object Forgot : Routes("forgot")

    data object Home : Routes("home")

    data object Each : Routes("each/{plantId}") {
        fun createRoute(plantId: String) = "each/$plantId"
    }

    data object Booking : Routes("booking/{plantId}/{count}") {
        fun createRoute(plantId: String, count: Int) = "booking/$plantId/$count"
    }

    data object AllBooking : Routes("allBooking")

    data object Fav : Routes("favourite")

    data object Cart : Routes("cart")

    data object Profile : Routes("profile")

    data object UpdateProfile : Routes("updateProfile")

    data object Search : Routes("search")

    data object ShareApp : Routes("shareApp")

    data object Contact : Routes("contact")

    data object AboutUs : Routes("about")

    data object PrivacyPolicy : Routes("privacyPolicy")

}
