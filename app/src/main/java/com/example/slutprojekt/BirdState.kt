package com.example.slutprojekt

import android.graphics.RectF

data class BirdState(
    val position: RectF,
    val action: BirdAction = BirdAction.IDLE,
)


enum class BirdAction {
    IDLE,
    FALLING,
}

const val BirdLiftAmount = -BirdSizeHeightModel

const val BirdFallingSpeed = -BirdLiftAmount * .75f

const val MillisForIdle = 150
const val MillisForFalling = MillisForIdle + 100
