package com.example.slutprojekt

import android.graphics.RectF

data class BirdState(
    val position: RectF,

    //kommer antagligen inte behövas
    val action: BirdAction = BirdAction.IDLE,
)


//Blir antagligen inte nödvändig
enum class BirdAction {
    IDLE,
    FLAPPING,
    FALLING,
    FAST_FALLING,
    DEAD,
}

//Kommer ändras så att det blir motsvarande en "ruta"
const val BirdLiftAmount = -BirdSizeHeightModel * 1.5f

const val BirdFallingSpeed = -BirdLiftAmount * .75f

//kommer nog inte ha fast falling
const val BirdFastFallingSpeed = BirdFallingSpeed * 4.5f
const val MillisForIdle = 150
const val MillisForFalling = MillisForIdle + 100
const val MillisForFastFall = MillisForFalling + 100
