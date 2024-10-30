package com.pandroid.greenhaven.presentation.screens.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.pandroid.greenhaven.R
import com.pandroid.greenhaven.presentation.component.CustomButton
import com.pandroid.greenhaven.presentation.utils.Dimens.LargeHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.LargeTextSize
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumExtraSmallLargeSize
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumLargeSmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallRoundedCorner
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallWidth
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallElevation
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallLargeHeight
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallMediumPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallPadding
import com.pandroid.greenhaven.presentation.utils.Dimens.SmallTextSize
import com.pandroid.greenhaven.ui.theme.PlantGreen
import com.pandroid.greenhaven.ui.theme.White

@Preview(showSystemUi = true)
@Composable
fun AboutUs(navController: NavController = rememberNavController()) {

    val aboutUs = stringResource(id = R.string.about_us)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .padding(SmallMediumPadding)
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(MediumSmallRoundedCorner)),
            colors = CardDefaults.cardColors(White),
            elevation = CardDefaults.elevatedCardElevation(SmallElevation)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = PlantGreen)
                    .height(LargeHeight)
            ) {

                Image(
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(MediumExtraSmallLargeSize)
                        .align(Alignment.TopCenter)
                        .padding(top = SmallMediumPadding)
                )

                Text(
                    text = "About Us",
                    fontSize = LargeTextSize,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(top = SmallPadding, bottom = SmallPadding),
                    color = White
                )
            }


            Text(
                text = aboutUs,
                fontSize = SmallTextSize,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(
                        start = SmallPadding,
                        end = SmallPadding,
                        top = SmallMediumPadding,
                        bottom = SmallMediumPadding
                    )
            )

            Row {
                CustomButton(
                    modifier = Modifier
                        .padding(
                            start = SmallMediumPadding,
                            end = SmallMediumPadding,
                            top = MediumLargeSmallPadding
                        )
                        .fillMaxWidth(.47f)
                        .height(MediumHeight)
                        .background(
                            shape = RoundedCornerShape(MediumSmallRoundedCorner),
                            color = PlantGreen
                        ),
                    onClick = {
                        navController.popBackStack()
                    },
                    text = "Close"
                )



                CustomButton(
                    modifier = Modifier
                        .padding(
                            start = SmallMediumPadding,
                            end = SmallMediumPadding,
                            top = MediumLargeSmallPadding
                        )
                        .fillMaxWidth(.9f)
                        .height(MediumHeight)
                        .border(
                            width = MediumSmallWidth,
                            shape = RoundedCornerShape(MediumSmallRoundedCorner),
                            color = PlantGreen
                        ),
                    onClick = {
                        navController.popBackStack()
                    },
                    text = "Cancel"
                )


            }

            Spacer(modifier = Modifier.height(SmallLargeHeight))

        }

    }

}


