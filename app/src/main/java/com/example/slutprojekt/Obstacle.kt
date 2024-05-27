package com.example.slutprojekt

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp

@Composable
fun Obstacle(
    state: ObstacleState,
    viewPortStart: Float,
    gameUnitToDp: Dp,
    modifier: Modifier = Modifier

) {
    Image(
        painter = painterResource(id = R.drawable.cloud),
        contentScale = ContentScale.FillBounds,
        contentDescription = null,
        modifier = modifier
            .requiredSize(gameUnitToDp * state.position.width(), gameUnitToDp * state.position.height())
            .offset(
                gameUnitToDp * (state.position.left-viewPortStart),
                gameUnitToDp * (state.position.top),
            )

    )
}