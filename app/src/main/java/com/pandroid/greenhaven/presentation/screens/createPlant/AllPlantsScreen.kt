package com.pandroid.greenhaven.presentation.screens.createPlant

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pandroid.greenhaven.R
import com.pandroid.greenhaven.data.resource.Result
import com.pandroid.greenhaven.data.resource.State
import com.pandroid.greenhaven.domain.model.PlantModel
import com.pandroid.greenhaven.domain.model.UserModel
import com.pandroid.greenhaven.presentation.navigation.Routes
import com.pandroid.greenhaven.presentation.screens.auth.AuthViewModel
import com.pandroid.greenhaven.presentation.utils.Dimens.LargeSmallWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargePadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargeRoundedCorner
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargeSmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargeTextSize
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargeWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallLargeHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallRoundedCorner
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallTextSize
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumTextSize
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallExtraMediumTextSize
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallMediumPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallMediumTextSize
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallSize
import com.pandroid.greenhaven.presentation.utils.Dimens.VeryExtraLargeWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.VeryExtraSmallHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.VerySmallMediumPadding
import com.pandroid.greenhaven.ui.theme.Black
import com.pandroid.greenhaven.ui.theme.ExtraDarkGray
import com.pandroid.greenhaven.ui.theme.ExtraLightGray
import com.pandroid.greenhaven.ui.theme.LightGray
import com.pandroid.greenhaven.ui.theme.Manrope
import com.pandroid.greenhaven.ui.theme.PlantGreen
import com.pandroid.greenhaven.ui.theme.White
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun AllPlantsScreen(
    navController: NavController,
    viewModel: PlantViewModel = getViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val plantList by viewModel.plantList.collectAsState()

    var selectedPlant by remember { mutableStateOf<PlantModel?>(null) }
    var search by remember { mutableStateOf("") }


    LaunchedEffect(Unit) {
        viewModel.loadPlant()
    }

    // Search section
    SearchSection(text = search, onValueChange = {
        search = it
    })

    // Filter plants based on search query
    val filteredPlants = if (search.isEmpty()) {
        plantList // If search query is empty, show all plants
    } else {
        plantList.filter { plant ->
            plant.title.contains(search, ignoreCase = true) // Filter plants by title
        }
    }

    if (selectedPlant == null) {
        when (state) {
            State.Loading() -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Loading..",
                        fontSize = MediumLargeTextSize,
                        color = ExtraDarkGray,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            is State.Error -> {
                Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show()
            }

            else -> {
                // Show filtered plants
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    PlantPager(
                        navController = navController,
                        plants = filteredPlants,
                        onPlantSelected = { plant -> selectedPlant = plant }
                    )
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = SmallMediumPadding)
        ) {
            UpdateAndDeleteProduct(
                viewModel = viewModel,
                plantModel = selectedPlant!!,
                onDismiss = {
                    selectedPlant = null
                }
            )
        }

    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlantPager(
    navController: NavController,
    plants: List<PlantModel>,
    onPlantSelected: (PlantModel) -> Unit
) {
    val pagerState =
        rememberPagerState(initialPage = 0, pageCount = { 3 })
    val categories = listOf("Suggested", "Indoor", "Outdoor")
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = White,
            modifier = Modifier.padding(vertical = SmallPadding),
            indicator = { tabPositions ->
                SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        .height(VeryExtraSmallHeight),
                    color = PlantGreen
                )
            }
        ) {
            categories.forEachIndexed { index, category ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch { pagerState.scrollToPage(index) }
                    },
                    text = {
                        Text(
                            text = category,
                            fontFamily = Manrope,
                            fontWeight = FontWeight.Bold,
                            fontSize = SmallExtraMediumTextSize,
                            color = if (pagerState.currentPage == index) PlantGreen else Black
                        )
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState
        ) { page ->
            val filteredPlants = when (page) {
                0 -> plants
                1 -> plants.filter { it.category == "Indoor" }
                2 -> plants.filter { it.category == "Outdoor" }
                else -> plants
            }

            PlantList(
                navController = navController,
                plants = filteredPlants,
                onPlantSelected = onPlantSelected
            )
        }
    }
}


@Composable
fun PlantList(
    navController: NavController,
    plants: List<PlantModel>,
    onPlantSelected: (PlantModel) -> Unit,
    authViewModel: AuthViewModel = getViewModel()
) {
    val adminEmail = stringResource(R.string.admin)

    // User state
    val userState by authViewModel.userState.collectAsState()

    val userData = when (userState) {
        is Result.Success -> (userState as Result.Success<UserModel>).data
        else -> null
    }

    if (plants.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "No products available.", fontSize = SmallMediumTextSize,
                color = ExtraDarkGray,
                fontWeight = FontWeight.SemiBold
            )
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(SmallMediumPadding),
            verticalArrangement = Arrangement.spacedBy(SmallMediumPadding)
        ) {
            items(plants) { plant ->
                EachPlant(plant = plant, onClick = {
                    if (userData != null && userData.email == adminEmail) {
                        onPlantSelected(plant)
                    } else {
                        navController.navigate(Routes.Each.createRoute(plantId = plant.plantId))
                    }
                })
            }
        }
    }
}


@Composable
fun EachPlant(
    plant: PlantModel,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(MediumLargeRoundedCorner))
            .aspectRatio(3f / 4f)
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
                .aspectRatio(1f / 1f)
                .align(Alignment.TopCenter)
        )

        Text(
            text = if (plant.title.length > 13) plant.title.take(11) + "..." else plant.title,
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
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = Manrope
            ),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(SmallMediumPadding)
        )
    }


}


@Composable
fun SearchSection(text: String, onValueChange: (String) -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()

    ) {

        OutlinedTextField(
            value = text, onValueChange = onValueChange,
            modifier = Modifier
                .height(MediumSmallLargeHeight)
                .width(VeryExtraLargeWidth)
                .clip(RoundedCornerShape(MediumSmallRoundedCorner))
                .background(color = LightGray),
            placeholder = {
                Text(
                    text = "Search plants...",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = MediumTextSize,
                    color = ExtraDarkGray
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = MediumLargeSmallPadding)
                        .size(SmallSize),
                    tint = ExtraDarkGray
                )
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = LightGray,
                unfocusedContainerColor = LightGray,
                cursorColor = PlantGreen
            )
        )

        Spacer(modifier = Modifier.width(MediumLargeWidth))

        Box(
            modifier = Modifier
                .height(MediumSmallLargeHeight)
                .width(LargeSmallWidth)
                .background(
                    color = PlantGreen,
                    shape = RoundedCornerShape(MediumSmallRoundedCorner)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_1),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(SmallSize)
            )

        }
    }


}

