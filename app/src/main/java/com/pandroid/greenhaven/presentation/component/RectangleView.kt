package com.pandroid.greenhaven.presentation.component

import androidx.compose.foundation.shape.GenericShape

val DownwardDentArcShape = GenericShape { size, _ ->
    moveTo(0f, 0f)

    cubicTo(
        size.width / 2f, size.height * 0.6f,
        size.width / 2f, size.height * 0.6f,
        size.width, 0f
    )


    lineTo(size.width, size.height)
    lineTo(0f, size.height)
    close()
}








