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
const val ViewPortOffset = BirdSizeHeightModel*3
const val DistanceBetweenObstacles = BirdSizeWidthModel*7
const val ObstacleWidth = BirdSizeWidthModel*1.5f
const val GapBetweenObstacles = BirdSizeWidthModel*8
const val GapBetweenRows = BirdSizeHeightModel*3
const val HorizontalSpeed = DistanceBetweenObstacles

data class GameState (
    val birdState: BirdState = BirdState(RectF()),

    // Ska vara flera rader med listor av obstacles inte bara ett par.
    val obstacles: List<ObstacleList> = emptyList(),
    val status: GameStatus = GameStatus.WAITING
) {
    fun distance(): Float {
        return birdState.position.left
    }
    class GameViewModel(application: Application) : AndroidViewModel(application) {
        private var prevTime = 0L // time of last frame update

        //enbart relevant om den ska falla
        private var lastLift = 0L


        private var lastObstacleStart = 0f
        private var lastRowEnd = 0f
        private var wasTapped = false
        private var obstacles = mutableListOf<ObstacleList>()
        private val _gameState = MutableStateFlow(GameState())
        val gameState = _gameState.asStateFlow()

        init {
            resetGame()
        }

        private fun resetGame() {
            lastObstacleStart = 0f
            obstacles.clear()
            repeat(5) {
                obstacles.add(newObstacleList())
            }
            _gameState.value = GameState(
                BirdState(RectF(0f, 200f, BirdSizeWidthModel, 200 + BirdSizeHeightModel)),
                obstacles.toList(),
                GameStatus.WAITING,
            )
        }

        private fun newObstacleList(): ObstacleList{
            val nextRowStart = lastRowEnd + GapBetweenRows
            var row = mutableListOf<ObstacleState>()
            repeat(10) {
                row.add(newObstacle(nextRowStart))
            }
            println(nextRowStart)
            val newObstacleList = ObstacleList(row, nextRowStart)
            lastRowEnd= nextRowStart+ BirdSizeHeightModel
            return newObstacleList
        }
  
        private fun newObstacle( top: Float ): ObstacleState {
            // between 100 and 300
            //gapStart blir för sidan istället för botten
            val gapStart = Random.nextFloat() * 200 + 50
            val nextObstacleStart = lastObstacleStart + GapBetweenObstacles
            //Kanske är -BirdSizeHeightModel
            val obstacle = ObstacleState(RectF(nextObstacleStart,top , gapStart, top+ BirdSizeHeightModel))
            /*val pair = ObstaclePair(
                ObstacleState(
                    RectF(
                        nextObstacleStart,
                        0f,
                        nextObstacleStart + ObstacleWidth,
                        gapStart
                    )
                ),
                ObstacleState(
                    RectF(
                        nextObstacleStart,
                        gapStart + GapBetweenObstacles,
                        nextObstacleStart + ObstacleWidth,
                        GameScreenHeight
                    )
                )
            )*/
            lastObstacleStart = nextObstacleStart
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

                //tror den endast behövs iffal fågeln ska falla
                //lastLift = frameTime - MillisForIdle
            }

        }

        private fun handleGameOver(frameTime: Long) {
            if (wasTapped) {
                handleWaiting(frameTime)
            }
        }

        private fun endGame() {
            wasTapped = false
            _gameState.value = _gameState.value.copy(status = GameStatus.OVER)
        }

        private fun handleGameLoop(frameTime: Long, fractionalSeconds: Float) {
            val birdPosition = RectF(_gameState.value.birdState.position)


            //kolla upp iterator
            val iteratorObstacleList = obstacles.listIterator()
            val newObstacleList = mutableListOf<ObstacleList>()
            while (iteratorObstacleList.hasNext()) {
                val iteratorObstacles= iteratorObstacleList.next().list.listIterator()
                val newObstacles = mutableListOf<ObstacleState>()
                while (iteratorObstacles.hasNext()) {
                    val obstacle = iteratorObstacles.next()

                    //kommer användas för enskilda obstacles istellet för flera samtidigt
                    when {
                        obstacle.position.right < _gameState.value.distance() -> {
                            iteratorObstacles.remove()
                            newObstacles.add(newObstacle(obstacle.position.bottom))
                        }

                        obstacle.position.top < _gameState.value.distance() -> {
                            iteratorObstacleList.remove()
                            newObstacleList.add(newObstacleList())

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
                iteratorObstacleList.next().list.addAll(newObstacles)

            }
            obstacles.addAll(newObstacleList)
            /*if (birdPosition.bottom > GameScreenHeight) {
                endGame()
                return
            }*/
            /*birdPosition.offset(fractionalSeconds * HorizontalSpeed, 0f)
            var newAction: BirdAction
            */
            //Vad som händer vid tryck
            /*if (wasTapped) {

                //Här ska vi ha att alla hinder flyttar sig neråt med en enhet av fågelns längd
               birdPosition.offset(0f, java.lang.Float.max(BirdLiftAmount, -birdPosition.top))
                //newAction = BirdAction.FLAPPING
                wasTapped = false
                lastLift = frameTime
            }
            //kanske inte nödvändig eller kommer åtminstone behöva göras om
            else {
                val millisSinceLift = frameTime - lastLift
                newAction = when {
                    millisSinceLift > MillisForFastFall -> BirdAction.FAST_FALLING
                    millisSinceLift > MillisForFalling -> BirdAction.FALLING
                    millisSinceLift > MillisForIdle -> BirdAction.IDLE
                    else -> _gameState.value.birdState.action
                }
                val heightDecrease = when (newAction) {
                    BirdAction.FAST_FALLING -> BirdFastFallingSpeed
                    else -> BirdFallingSpeed
                } * fractionalSeconds
                birdPosition.offset(0f, heightDecrease)
            }*/
            _gameState.value = _gameState.value.copy(
               // birdState = BirdState(birdPosition, newAction),
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



