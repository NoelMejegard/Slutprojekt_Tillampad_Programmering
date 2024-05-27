package com.example.slutprojekt

import android.app.Application
import android.graphics.RectF
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

enum class GameStatus {
    WAITING,
    RUNNING,
    OVER,
}
const val BirdSizeWidthModel = 40f
const val BirdSizeHeightModel = BirdSizeWidthModel*7/10
const val GameScreenHeight = 500f
const val ViewPortOffset = BirdSizeHeightModel*4
const val GapBetweenObstacles = BirdSizeWidthModel*8
const val GapBetweenRows = BirdSizeHeightModel*6
const val HorizontalSpeed = BirdSizeWidthModel*4
private var points=0
private var restartText =""

data class GameState (
    val birdState: BirdState = BirdState(RectF()),
    val obstacles: List<ObstacleList> = emptyList(),
    val status: GameStatus = GameStatus.WAITING,
) {

    fun distance(): Float {
        return birdState.position.left
    }
    fun points(): Int{
        return points
    }

    fun restartText(): String{
        return restartText
    }

    class GameViewModel(application: Application) : AndroidViewModel(application) {
        private var prevTime = 0L
        private var lastLift = 0L

        private var lastObstacleStart = 0f
        private var lastRowEnd = 250f
        private var wasTapped = false
        private var obstacles = mutableListOf<ObstacleList>()
        private val _gameState = MutableStateFlow(GameState())
        private var trueSpeed = HorizontalSpeed
        val gameState = _gameState.asStateFlow()

        init {
            resetGame()
        }
        fun setDificulty(dificulty: Int){
            if(dificulty==0){
            trueSpeed = HorizontalSpeed
            }
            else if (dificulty==1){
                trueSpeed = HorizontalSpeed*2
            }
            else{
                trueSpeed = HorizontalSpeed*4
            }
        }
        fun home(){
            endGame()
        }

        private fun resetGame() {
            restartText=""
            points=0
            lastRowEnd= 250f
            lastObstacleStart = 0f
            obstacles.clear()
            repeat(5) {
                obstacles.add(newObstacleList())
            }
            lastObstacleStart=3200f
            _gameState.value = GameState(
                BirdState(RectF( 100f, GameScreenHeight- BirdSizeHeightModel-100, BirdSizeWidthModel+100, GameScreenHeight-100)),
                obstacles.toList(),
                GameStatus.WAITING,
            )
        }

        private fun newObstacleList(): ObstacleList{
            println(lastRowEnd)
            lastObstacleStart= 0f
            val nextRowStart = lastRowEnd
            var row = mutableListOf<ObstacleState>()
            repeat(10) {
                row.add(newObstacle(nextRowStart))
            }

            val newObstacleList = ObstacleList(row, nextRowStart)
            lastRowEnd= nextRowStart+ BirdSizeHeightModel- GapBetweenRows
            return newObstacleList
        }
  
        private fun newObstacle( top: Float ): ObstacleState {
            val gapStart = Random.nextFloat() * 100 + 50
            val nextObstacleStart = lastObstacleStart + GapBetweenObstacles
            val obstacle = ObstacleState(RectF(nextObstacleStart,top ,nextObstacleStart + gapStart, top+ BirdSizeHeightModel))

            lastObstacleStart = nextObstacleStart + gapStart
            return obstacle
        }

        fun onTap() {
            wasTapped = true
        }

        private fun handleWaiting(frameTime: Long) {
            if (wasTapped) {
                resetGame()
                _gameState.value = _gameState.value.copy(status = GameStatus.RUNNING)
                wasTapped = false

            }

        }

        private fun handleGameOver(frameTime: Long) {
            if (wasTapped) {
                handleWaiting(frameTime)
            }

        }

        private fun endGame() {
            restartText="Tap to restart"
            wasTapped = false
            _gameState.value = _gameState.value.copy(status = GameStatus.OVER)
        }

        private fun handleGameLoop(frameTime: Long, fractionalSeconds: Float) {
            val birdPosition = RectF(_gameState.value.birdState.position)

            val iteratorObstacleList = obstacles.listIterator()
            val newObstacleList = mutableListOf<ObstacleList>()
            while (iteratorObstacleList.hasNext()) {

                val iteratorObstacles= iteratorObstacleList.next().list.listIterator()
                val newObstacles = mutableListOf<ObstacleState>()

                while (iteratorObstacles.hasNext()) {
                    val obstacle = iteratorObstacles.next()
                    when {
                        obstacle.position.right < _gameState.value.distance()-100 -> {
                            lastObstacleStart=obstacle.position.left+2880
                            iteratorObstacles.remove()
                            newObstacles.add(newObstacle(obstacle.position.top))
                        }


                        obstacle.position.bottom < _gameState.value.birdState.position.top -> {
                            break
                        }

                        else -> {
                            if (RectF.intersects(
                                    obstacle.position,
                                    birdPosition
                                )
                            ) {
                                endGame()
                                return
                            }
                        }
                    }
                }
                iteratorObstacleList.previous().list.addAll(newObstacles)
                iteratorObstacleList.next()


            }
            obstacles.addAll(newObstacleList)
            if (birdPosition.bottom > GameScreenHeight) {
                endGame()
                return
            }
            birdPosition.offset(fractionalSeconds * trueSpeed, 0f)
            var newAction: BirdAction

            if (wasTapped) {
                points+=10
                for (list in obstacles){
                    list.updatePosition(BirdSizeHeightModel)
                }
                birdPosition.offset(0f, java.lang.Float.max(BirdLiftAmount, -birdPosition.top))
                wasTapped = false
                lastLift = frameTime
            }
            else {
                val millisSinceLift = frameTime - lastLift
                newAction = when {
                    millisSinceLift > MillisForFalling -> BirdAction.FALLING
                    millisSinceLift > MillisForIdle -> BirdAction.IDLE
                    else -> _gameState.value.birdState.action
                }
                if(obstacles[0].start> GameScreenHeight){
                    println(obstacles.last().start)
                    lastRowEnd= obstacles.last().start + BirdSizeHeightModel- GapBetweenRows
                    obstacles.remove(obstacles[0])

                    obstacles.add(newObstacleList())
                }
                val heightDecrease = when (newAction) {
                    else -> BirdFallingSpeed
                } * fractionalSeconds
                birdPosition.offset(0f, heightDecrease)
            }
            _gameState.value = _gameState.value.copy(
                birdState = BirdState(birdPosition, BirdAction.IDLE),
                obstacles = obstacles.toList(),

            )
        }

        fun update(time: Long) {

            val fractionalSeconds = (time - prevTime).toFloat() / 1000
            prevTime = time

            when (_gameState.value.status) {
                GameStatus.WAITING -> handleWaiting(time)
                GameStatus.RUNNING -> handleGameLoop(time, fractionalSeconds)
                GameStatus.OVER -> handleGameOver(time)
            }
        }
    }
}



