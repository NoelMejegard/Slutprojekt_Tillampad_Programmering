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
fun Bird(
    state: BirdState,
    viewPortStart: Float,
    gameUnitToDp: Dp,
    modifier: Modifier = Modifier,
) {
    //inte nödvändigt
    val rotateDegrees =
        when (state.action) {
            BirdAction.IDLE -> 0f
            BirdAction.FLAPPING -> LiftingDegree
            BirdAction.DEAD -> DeadDegree
            BirdAction.FALLING -> FallingDegree
            BirdAction.FAST_FALLING -> FastFalling
        }
    Image(
        painter = painterResource(id = R.drawable.bird),
        contentScale = ContentScale.FillBounds,
        contentDescription = null,
        modifier = modifier
            .requiredSize(gameUnitToDp * state.position.width(), gameUnitToDp * state.position.height())
            //Kommer ändras
            .offset(
                gameUnitToDp * (state.position.left-viewPortStart),
                gameUnitToDp * (state.position.top),
            )
            .rotate(rotateDegrees)
    )
}

//behöver inga degrees
const val LiftingDegree = -10f
const val FallingDegree = -LiftingDegree
const val FastFalling = 60f
const val DeadDegree = FastFalling - 10f