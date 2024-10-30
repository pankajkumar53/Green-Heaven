package com.pandroid.greenhaven.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pandroid.greenhaven.R
import com.pandroid.greenhaven.presentation.navigation.Routes
import com.pandroid.greenhaven.presentation.utils.Dimens.LargePadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumElevation
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumExtraLargeWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumExtraSmallSize
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargeHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargeSmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSize
import com.pandroid.greenhaven.ui.theme.Black
import com.pandroid.greenhaven.ui.theme.PlantGreen
import com.pandroid.greenhaven.ui.theme.White

@Composable
fun ScaffoldWithBottomNavBar(
    navController: NavController,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) },
        containerColor = White
    ) { innerPadding ->
        content(innerPadding)
    }
}

@Composable
fun BottomNavigationBar(navController: NavController = rememberNavController()) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(MediumLargeHeight)
            .background(color = Color.Transparent)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(MediumLargeHeight),
            shape = DownwardDentArcShape,
            elevation = CardDefaults.cardElevation(MediumElevation),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            // Navigation bar for Home, Favorites, Cart, Profile
            NavigationBar(containerColor = White, modifier = Modifier.padding(top = LargePadding)) {

                // Home Navigation Item
                NavigationBarItem(
                    selected = currentRoute == Routes.Home.route,
                    onClick = {
                        if (currentRoute != Routes.Home.route) {
                            navController.navigate(Routes.Home.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (currentRoute == Routes.Home.route) Icons.Filled.Home else Icons.Outlined.Home,
                            contentDescription = "Home",
                            modifier = Modifier.size(MediumExtraSmallSize),
                            tint = if (currentRoute == Routes.Home.route) PlantGreen else Black
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PlantGreen,
                        unselectedIconColor = Black,
                        indicatorColor = Color.Transparent
                    )
                )

                // Favorite Navigation Item
                NavigationBarItem(
                    selected = currentRoute == Routes.Fav.route,
                    onClick = {
                        if (currentRoute != Routes.Fav.route) {
                            navController.navigate(Routes.Fav.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (currentRoute == Routes.Fav.route) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorites",
                            modifier = Modifier.size(MediumExtraSmallSize),
                            tint = if (currentRoute == Routes.Fav.route) PlantGreen else Black
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PlantGreen,
                        unselectedIconColor = Black,
                        indicatorColor = Color.Transparent
                    )
                )

                // Spacer to leave a gap between items
                Spacer(modifier = Modifier.width(MediumExtraLargeWidth))

                // Cart Navigation Item
                NavigationBarItem(
                    selected = currentRoute == Routes.Cart.route,
                    onClick = {
                        if (currentRoute != Routes.Cart.route) {
                            navController.navigate(Routes.Cart.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (currentRoute == Routes.Cart.route) Icons.Filled.ShoppingCart else Icons.Outlined.ShoppingCart,
                            contentDescription = "Cart",
                            modifier = Modifier.size(MediumExtraSmallSize),
                            tint = if (currentRoute == Routes.Cart.route) PlantGreen else Black
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PlantGreen,
                        unselectedIconColor = Black,
                        indicatorColor = Color.Transparent
                    )
                )

                // Profile Navigation Item
                NavigationBarItem(
                    selected = currentRoute == Routes.Profile.route,
                    onClick = {
                        if (currentRoute != Routes.Profile.route) {
                            navController.navigate(Routes.Profile.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (currentRoute == Routes.Profile.route) Icons.Filled.Person else Icons.Outlined.Person,
                            contentDescription = "Profile",
                            modifier = Modifier.size(MediumExtraSmallSize),
                            tint = if (currentRoute == Routes.Profile.route) PlantGreen else Black
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PlantGreen,
                        unselectedIconColor = Black,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }

        // Search Icon positioned separately and centered
        Icon(
            painter = painterResource(id = if (currentRoute == Routes.Search.route) R.drawable.img else R.drawable.img),
            contentDescription = "Search",
            modifier = Modifier
                .size(MediumSize)
                .align(Alignment.Center)
                .background(color = PlantGreen, shape = CircleShape)
                .padding(MediumLargeSmallPadding)
                .clickable {
                    if (currentRoute != Routes.Search.route) {
                        navController.navigate(Routes.Search.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
            tint = if (currentRoute == Routes.Search.route) Black else White
        )
    }

}
