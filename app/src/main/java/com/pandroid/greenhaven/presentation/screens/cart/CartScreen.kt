package com.pandroid.greenhaven.presentation.screens.cart

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pandroid.greenhaven.R
import com.pandroid.greenhaven.data.resource.State
import com.pandroid.greenhaven.domain.model.PlantModel
import com.pandroid.greenhaven.presentation.component.CustomButton
import com.pandroid.greenhaven.presentation.component.CustomCircularIndicator
import com.pandroid.greenhaven.presentation.navigation.Routes
import com.pandroid.greenhaven.presentation.screens.component.AppBar
import com.pandroid.greenhaven.presentation.screens.fav.EmptyFavoritesMessage
import com.pandroid.greenhaven.presentation.utils.Dimens
import com.pandroid.greenhaven.ui.theme.Black
import com.pandroid.greenhaven.ui.theme.ExtraDarkGray
import com.pandroid.greenhaven.ui.theme.LightGray
import com.pandroid.greenhaven.ui.theme.Manrope
import com.pandroid.greenhaven.ui.theme.PlantGreen
import com.pandroid.greenhaven.ui.theme.White
import org.koin.androidx.compose.getViewModel

@Composable
fun CartScreen(navController: NavController, viewModel: CartViewModel = getViewModel()) {

    val state by viewModel.cartState.observeAsState(initial = State.Idle())

    var showCheckoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadCart()
    }

    val cartItems by viewModel.cartList.observeAsState(emptyList())
    val selectedPlants by viewModel.selectedPlants.observeAsState(emptySet())

    when (state) {
        is State.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CustomCircularIndicator()
            }
        }

        is State.Error -> {
            val errorMessage = (state as State.Error).message ?: "Unknown error"
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = errorMessage,
                    fontSize = Dimens.MediumLargeTextSize,
                    color = ExtraDarkGray,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clickable { navController.navigate(Routes.Login.route) }
                )
            }
        }

        is State.Success -> {
            if (cartItems.isEmpty()) {
                EmptyFavoritesMessage(text = "No Cart yet. Add some plants to your cart!")
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    AppBar(navController = navController, text = "Your Cart!")

                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(cartItems) { plant ->
                            CartItem(
                                plant = plant,
                                isSelected = selectedPlants.contains(plant.plantId),
                                onItemSelected = { viewModel.togglePlantSelection(plant.plantId) },
                                onDelete = { viewModel.deleteCart(plant.plantId) }
                            )
                        }
                    }

                    if (selectedPlants.isNotEmpty()) {
                        CustomButton(
                            onClick = { showCheckoutDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Dimens.MediumSmallPadding)
                                .height(Dimens.MediumSmallHeight)
                                .align(Alignment.CenterHorizontally),
                            text = "Checkout (${selectedPlants.size} Plants selected)"
                        )
                    }

                    Spacer(modifier = Modifier.height(Dimens.MediumLargeHeight))
                }
            }
        }

        else -> {
            EmptyFavoritesMessage(text = "No Cart yet. Add some plants to your cart!")
        }
    }

    if (showCheckoutDialog) {
        CheckoutDialog(
            selectedItems = cartItems.filter { selectedPlants.contains(it.plantId) },
            onDismiss = { showCheckoutDialog = false },
            onConfirm = {
                viewModel.proceedToCheckout()
                showCheckoutDialog = false
            }
        )
    }
}

@Composable
fun CheckoutDialog(
    selectedItems: List<PlantModel>,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val totalPrice = selectedItems.sumOf { it.price.toDouble() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White, shape = RoundedCornerShape(Dimens.MediumRoundedCorner))
            .padding(Dimens.MediumSmallPadding)
    ) {
        Column {
            Text(
                text = "Order Summary",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = Manrope
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(Dimens.MediumSmallPadding))

            LazyColumn {
                items(selectedItems) { item ->
                    Text(
                        text = "${item.title}: $${item.price}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = Manrope
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.MediumSmallPadding))

            Text(
                text = "Total: $${totalPrice}", style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = Manrope
                )
            )

            Spacer(modifier = Modifier.height(Dimens.MediumLargePadding))

            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                CustomButton(
                    onClick = onConfirm,
                    text = "Confirm Order",
                    modifier = Modifier
                        .fillMaxWidth(0.52f)
                        .border(
                            color = PlantGreen,
                            width = Dimens.MediumWidth,
                            shape = RoundedCornerShape(Dimens.MediumRoundedCorner)
                        )
                )

                Spacer(modifier = Modifier.width(Dimens.SmallPadding))

                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(Dimens.MediumRoundedCorner),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            color = PlantGreen,
                            width = Dimens.ExtraSmallWidth,
                            shape = RoundedCornerShape(Dimens.MediumRoundedCorner)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = White,
                        contentColor = Black
                    )
                ) {
                    Text(
                        "Cancel",
                        fontSize = Dimens.MediumSmallTextSize,
                        fontFamily = Manrope,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun CartItem(
    plant: PlantModel,
    isSelected: Boolean,
    onItemSelected: () -> Unit,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.SmallPadding)
            .border(
                width = Dimens.ExtraSmallWidth,
                shape = RoundedCornerShape(Dimens.MediumSmallRoundedCorner),
                color = LightGray
            )
            .background(
                if (isSelected) Color.LightGray else Color.Transparent,
                shape = RoundedCornerShape(Dimens.MediumSmallRoundedCorner)
            )
            .clickable { onItemSelected() }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = plant.plantImage,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.place_holder),
                modifier = Modifier
                    .padding(Dimens.SmallPadding)
                    .size(Dimens.MediumLargeSmallSize)
                    .clip(RoundedCornerShape(Dimens.MediumRoundedCorner))
                    .border(
                        Dimens.ExtraSmallWidth,
                        shape = RoundedCornerShape(Dimens.MediumRoundedCorner),
                        color = LightGray
                    )
            )

            Column(modifier = Modifier.padding(Dimens.SmallPadding)) {
                Text(
                    text = plant.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = Manrope,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = plant.category,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = Manrope,
                        color = ExtraDarkGray,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "$${plant.price}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = Manrope,
                        color = PlantGreen,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
        IconButton(
            onClick = onDelete,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(Dimens.MediumSmallPadding)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                modifier = Modifier.size(Dimens.LargeSmallWidth)
            )
        }
    }
}




