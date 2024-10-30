package com.pandroid.greenhaven.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumRoundedCorner
import com.pandroid.greenhaven.presentation.utils.Dimens.MediumSmallTextSize
import com.pandroid.greenhaven.ui.theme.Manrope
import com.pandroid.greenhaven.ui.theme.PlantGreen

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    shape: RoundedCornerShape = RoundedCornerShape(MediumRoundedCorner),
    modifier: Modifier = Modifier.fillMaxWidth(),
    isEnabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = isEnabled,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = PlantGreen),
        shape = shape
    ) {
        Text(text = text, fontFamily = Manrope, fontSize = MediumSmallTextSize)
    }
}
