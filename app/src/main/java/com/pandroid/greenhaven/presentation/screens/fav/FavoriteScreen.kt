package com.pandroid.greenhaven.presentation.screens.fav

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pandroid.greenhaven.R
import com.pandroid.greenhaven.data.resource.State
import com.pandroid.greenhaven.domain.model.PlantModel
import com.pandroid.greenhaven.presentation.component.CustomCircularIndicator
import com.pandroid.greenhaven.presentation.navigation.Routes
import com.pandroid.greenhaven.presentation.screens.component.AppBar
import com.pandroid.greenhaven.presentation.utils.Dimens.ExtraSmallWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.LargePadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargeMediumSmallSize
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargeTextSize
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumRoundedCorner
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSize
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallRoundedCorner
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallLargeHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallMediumHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallMediumPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallPadding
import com.pandroid.greenhaven.ui.theme.ExtraDarkGray
import com.pandroid.greenhaven.ui.theme.LightGray
import com.pandroid.greenhaven.ui.theme.Manrope
import com.pandroid.greenhaven.ui.theme.PlantGreen
import org.koin.androidx.compose.getViewModel

@Composable
fun FavoriteScreen(navController: NavController, viewModel: FavViewModel = getViewModel()) {

    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }

    val favState by viewModel.favState.observeAsState(initial = State.Idle())
    val favoritePlants by viewModel.favList.observeAsState(emptyList())

    when (favState) {
        is State.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CustomCircularIndicator()
            }
        }

        is State.Error -> {
            val errorMessage = (favState as State.Error).message ?: "Unknown error"
            Box(modifier = Modifier.fillMaxSize()) {

                Text(
                    text = errorMessage,
                    fontSize = MediumLargeTextSize,
                    color = ExtraDarkGray,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.Center).clickable {
                        navController.navigate(Routes.Login.route)
                    }
                )

            }
        }

        is State.Success -> {
            if (favoritePlants.isEmpty()) {
                EmptyFavoritesMessage(text = "No favorites yet. Add some plants to your favorites!")
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    AppBar(navController, "Favorite")
                    Spacer(modifier = Modifier.height(SmallMediumHeight))
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(.96f)
                            .padding(
                                start = SmallMediumPadding,
                                end = SmallMediumPadding,
                                bottom = LargePadding
                            )
                    ) {
                        items(favoritePlants) { plant ->
                            EachFavPlant(plant = plant, onClick = {
                                navController.navigate(Routes.Each.createRoute(plantId = plant.plantId))
                            })
                            Spacer(modifier = Modifier.height(SmallLargeHeight))
                        }
                    }

                }

            }
        }

        else -> {
            EmptyFavoritesMessage(text = "No favorites yet. Add some plants to your favorites!")
        }
    }

}


@Composable
fun EmptyFavoritesMessage(text: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.Center)
        )

    }
}

/*@Composable
fun EachFavPlant(
    plant: PlantModel,
    viewModel: FavViewModel = getViewModel(),
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(MediumLargeRoundedCorner))
            .aspectRatio(16f / 12f)
            .background(ExtraLightGray)
            .clickable {
                onClick()
            }
    ) {

        AsyncImage(
            model = plant.plantImage, contentDescription = "cover image",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.place_holder),
            modifier = Modifier
                .padding(VerySmallMediumPadding)
                .clip(RoundedCornerShape(MediumSmallRoundedCorner))
                .aspectRatio(16f / 9f)
                .align(Alignment.TopCenter)
        )

        Icon(
            imageVector = Icons.Default.Favorite,
            tint = PlantGreen,
            contentDescription = "fav icon",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(SmallMediumPadding)
                .size(MediumExtraSmallSize)
                .clickable {
                    viewModel.deleteFavorite(plant.plantId)
                    viewModel.loadFavorites()
                }
        )

        Text(
            text = if (plant.title.length > 20) plant.title.take(19) + "..." else plant.title,
            fontWeight = FontWeight.Bold,
            fontFamily = Manrope,
            fontSize = MediumSmallTextSize,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = SmallMediumPadding, bottom = MediumLargePadding)
        )

        Text(
            text = plant.category,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = Manrope,
                color = ExtraDarkGray
            ),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = SmallMediumPadding, bottom = MediumLargeSmallPadding)
        )

        Text(
            text = "$${plant.price}",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = Manrope,
                color = PlantGreen
            ),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(SmallMediumPadding)
        )

    }


}*/

@Composable
fun EachFavPlant(
    plant: PlantModel,
    viewModel: FavViewModel = getViewModel(),
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = ExtraSmallWidth,
                shape = RoundedCornerShape(MediumSmallRoundedCorner),
                color = LightGray
            )
            .clickable {
                onClick()
            }
    )
    {
        Row {

            AsyncImage(
                model = plant.plantImage,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.place_holder),
                modifier = Modifier
                    .padding(SmallPadding)
                    .size(MediumLargeMediumSmallSize)
                    .clip(RoundedCornerShape(MediumRoundedCorner))
                    .border(
                        width = ExtraSmallWidth,
                        shape = RoundedCornerShape(MediumRoundedCorner),
                        color = LightGray
                    )
            )

            Column(
                modifier = Modifier
                    .padding(top = SmallMediumPadding, start = SmallMediumPadding)
            ) {

                Text(
                    text = plant.title, style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = Manrope,
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = plant.category, style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = Manrope,
                        color = ExtraDarkGray,
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = "$${plant.price}", style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = Manrope,
                        color = PlantGreen,
                        fontWeight = FontWeight.Bold
                    )
                )

            }

        }

        IconButton(
            onClick = {
                viewModel.deleteFavorite(plant.plantId)
                viewModel.loadFavorites()
            }, modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(SmallMediumPadding)
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                tint = PlantGreen,
                contentDescription = "fav icon",
                modifier = Modifier.size(MediumSize)
            )
        }
    }


}

