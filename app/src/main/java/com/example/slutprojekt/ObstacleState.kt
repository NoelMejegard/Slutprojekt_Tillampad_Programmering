package com.example.slutprojekt

import android.graphics.RectF

data class ObstacleList (
    val list: MutableList<ObstacleState>,
    var start: Float,
) {
    fun updatePosition(birdHeight: Float) {
        start+=birdHeight
        for (obstacle in list){
            obstacle.position.offset(0f, birdHeight)
        }
    }
}

data class ObstacleState (
    val position: RectF,

)