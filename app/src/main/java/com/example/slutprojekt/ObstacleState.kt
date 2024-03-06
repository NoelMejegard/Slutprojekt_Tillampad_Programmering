package com.example.slutprojekt

import android.graphics.RectF

//antagligen inte nödvändig
data class ObstacleList (
    val list: MutableList<ObstacleState>,
    val start: Float,
)

data class ObstacleState (
    val position: RectF,

)