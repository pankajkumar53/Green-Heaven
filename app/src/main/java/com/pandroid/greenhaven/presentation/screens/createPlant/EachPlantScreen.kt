package com.pandroid.greenhaven.presentation.screens.createPlant

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pandroid.greenhaven.R
import com.pandroid.greenhaven.data.resource.State
import com.pandroid.greenhaven.domain.model.PlantImageUrlModel
import com.pandroid.greenhaven.domain.model.PlantModel
import com.pandroid.greenhaven.presentation.component.CustomButton
import com.pandroid.greenhaven.presentation.component.CustomCircularIndicator
import com.pandroid.greenhaven.presentation.navigation.Routes
import com.pandroid.greenhaven.presentation.screens.cart.CartViewModel
import com.pandroid.greenhaven.presentation.screens.fav.FavViewModel
import com.pandroid.greenhaven.presentation.utils.Dimens.ExtraLargeRoundedCorner
import com.pandroid.greenhaven.presentation.utils.Dimens.ExtraSmallWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.LargeExtraSmallWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.LargeExtraWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.LargeSmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumExtraSmallSize
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargeSmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargeTextSize
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumRoundedCorner
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallExtraLargeWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallLargePadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallLargeWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallRoundedCorner
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumTextSize
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallExtraLargeHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallLargeHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallMediumPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallMediumRoundedCorner
import com.pandroid.greenhaven.presentation.utils.Dimens.VeryMediumSmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.VerySmallMediumPadding
import com.pandroid.greenhaven.ui.theme.Black
import com.pandroid.greenhaven.ui.theme.ExtraDarkGray
import com.pandroid.greenhaven.ui.theme.LightGray
import com.pandroid.greenhaven.ui.theme.Manrope
import com.pandroid.greenhaven.ui.theme.PlantGreen
import com.pandroid.greenhaven.ui.theme.White
import com.pandroid.greenhaven.ui.theme.Yellow
import org.koin.androidx.compose.getViewModel

@Composable
fun EachPlantScreen(
    navController: NavController,
    viewModel: PlantViewModel = getViewModel(),
    plantId: String
) {

    LaunchedEffect(Unit) {
        viewModel.loadPlant()
    }

    val state by viewModel.state.collectAsState()
    val plantList by viewModel.plantList.collectAsState()

    val plant = plantList.find { it.plantId == plantId }

    when (state) {
        is State.Loading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(White)
            ) {
                Text(
                    text = "Loading..",
                    fontSize = MediumLargeTextSize,
                    color = ExtraDarkGray,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        is State.Success -> {
            if (plant != null) {
                PlantDetail(plant, navController = navController)
            }
        }

        else -> Unit
    }


}

@Composable
fun PlantDetail(
    plantModel: PlantModel,
    viewModel: FavViewModel = getViewModel(),
    cartViewModel: CartViewModel = getViewModel(),
    navController: NavController
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }

    val favProducts by viewModel.favList.observeAsState(initial = emptyList())

    val isFavorite = plantModel.let { favProducts.any { it.plantId == plantModel.plantId } }

    val cartState by cartViewModel.cartState.observeAsState(initial = State.Idle())

    when (cartState) {
        is State.Error -> {
            val errorMessage = (cartState as State.Error).message ?: "Unknown error"
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    color = LightGray,
                    fontFamily = Manrope,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        is State.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CustomCircularIndicator()
            }
        }

        is State.Success -> {
            LaunchedEffect(Unit) {
                Toast.makeText(context, "Plant added to cart successfully", Toast.LENGTH_SHORT).show()
            }
        }

        else -> {
            Unit
        }
    }

    val plantImages =
        plantModel.plantImages + PlantImageUrlModel(
            imageId = "cover",
            imageUrl = plantModel.plantImage
        )

    var count by remember { mutableIntStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {

            Box(
                modifier = Modifier
                    .aspectRatio(1f / .8f)
                    .clip(RoundedCornerShape(bottomEnd = ExtraLargeRoundedCorner))
                    .background(LightGray)
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(LargeSmallPadding)
                ) {
                    items(plantImages) { image ->
                        AsyncImage(
                            model = image.imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(R.drawable.place_holder),
                            modifier = Modifier
                                .width(LargeExtraWidth)
                                .padding(horizontal = VeryMediumSmallPadding)
                                .clip(RoundedCornerShape(MediumRoundedCorner))
                        )
                    }
                }
            }


            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "back icon",
                modifier = Modifier
                    .padding(SmallMediumPadding)
                    .size(MediumExtraSmallSize)
                    .clickable {
                        navController.popBackStack()
                    }
            )

            if (isFavorite) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    tint = PlantGreen,
                    contentDescription = "fav icon",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(SmallMediumPadding)
                        .size(MediumExtraSmallSize)
                        .clickable {
                            viewModel.deleteFavorite(plantModel.plantId)
                            viewModel.loadFavorites()
                        }
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    tint = Black,
                    contentDescription = "back icon",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(SmallMediumPadding)
                        .size(MediumExtraSmallSize)
                        .clickable {
                            viewModel.addFavorite(plantModel)
                            viewModel.loadFavorites()
                        }
                )
            }


            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = SmallMediumPadding)
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.ic_sub),
                    contentDescription = "minus",
                    tint = White,
                    modifier = Modifier
                        .clickable {
                            if (count != 1) {
                                count--
                            }
                        }
                        .height(SmallExtraLargeHeight)
                        .width(MediumSmallExtraLargeWidth)
                        .background(
                            color = PlantGreen,
                            shape = RoundedCornerShape(SmallMediumRoundedCorner)
                        )
                        .padding(SmallMediumPadding)
                )


                Text(
                    text = "$count",
                    fontSize = MediumTextSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(
                        start = VerySmallMediumPadding,
                        end = VerySmallMediumPadding
                    )
                )


                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "minus",
                    tint = White,
                    modifier = Modifier
                        .clickable {
                            count++
                        }
                        .height(SmallExtraLargeHeight)
                        .width(MediumSmallExtraLargeWidth)
                        .background(
                            color = PlantGreen,
                            shape = RoundedCornerShape(SmallMediumRoundedCorner)
                        )
                        .padding(VerySmallMediumPadding)
                )

            }

        }


        Text(
            text = plantModel.title, style = MaterialTheme.typography.headlineSmall.copy(
                fontFamily = Manrope,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(start = SmallMediumPadding, top = SmallMediumPadding)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SmallMediumPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = plantModel.category,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = Manrope,
                    fontWeight = FontWeight.Bold,
                    color = LightGray
                ),
                modifier = Modifier.weight(1f)
            )


            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(VeryMediumSmallPadding),
                modifier = Modifier.wrapContentWidth(Alignment.End)
            ) {
                val rating = plantModel.rating.toFloat()
                val fullStars = rating.toInt()
                val hasHalfStar = rating % 1 != 0f

                items(fullStars) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "filled rating icon",
                        tint = Yellow
                    )
                }

                if (hasHalfStar) {
                    item {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.StarHalf,
                            contentDescription = "half-filled rating icon",
                            tint = Yellow
                        )
                    }
                }

                items(5 - fullStars - if (hasHalfStar) 1 else 0) {
                    Icon(
                        imageVector = Icons.Outlined.StarOutline,
                        contentDescription = "outlined rating icon",
                        tint = Yellow
                    )
                }

            }


        }


        Spacer(modifier = Modifier.height(SmallLargeHeight))

        Text(
            text = "$${plantModel.price}",
            style = MaterialTheme.typography.titleMedium.copy(
                color = White,
                fontWeight = FontWeight.SemiBold,
                fontFamily = Manrope
            ),
            modifier = Modifier
                .background(
                    PlantGreen,
                    shape = RoundedCornerShape(
                        topEnd = MediumSmallPadding,
                        bottomEnd = MediumSmallPadding
                    )
                )
                .padding(
                    top = SmallMediumPadding,
                    bottom = SmallMediumPadding,
                    end = MediumLargeSmallPadding,
                    start = MediumSmallLargePadding
                )
        )

        HorizontalDivider(
            thickness = DividerDefaults.Thickness.plus(ExtraSmallWidth),
            modifier = Modifier.padding(
                start = MediumSmallPadding,
                end = MediumSmallPadding,
                top = MediumLargeSmallPadding
            )
        )

        Text(
            text = "About", style = MaterialTheme.typography.titleLarge.copy(
                fontFamily = Manrope,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(start = SmallMediumPadding, top = SmallMediumPadding)
        )

        Text(
            text = plantModel.description, style = MaterialTheme.typography.bodyLarge.copy(
                fontFamily = Manrope,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(start = SmallMediumPadding, top = SmallMediumPadding)
        )


        Spacer(modifier = Modifier.height(SmallLargeHeight))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = SmallMediumPadding)
        ) {

            Box(
                modifier = Modifier
                    .height(MediumSmallHeight)
                    .width(LargeExtraSmallWidth)
                    .background(
                        color = LightGray,
                        shape = RoundedCornerShape(MediumSmallRoundedCorner)
                    )
                    .clickable {
                        cartViewModel.addCart(plantModel)
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_cart),
                    contentDescription = "cart icon",
                    modifier = Modifier
                        .padding(VerySmallMediumPadding)
                )
            }

            Spacer(modifier = Modifier.width(MediumSmallLargeWidth))

            CustomButton(
                text = "Buy Now",
                onClick = {
                    navController.navigate(
                        Routes.Booking.createRoute(
                            plantId = plantModel.plantId,
                            count = count
                        )
                    )

                },
                modifier = Modifier
                    .height(MediumSmallHeight)
                    .fillMaxWidth(.95f)
            )


        }


    }


}
