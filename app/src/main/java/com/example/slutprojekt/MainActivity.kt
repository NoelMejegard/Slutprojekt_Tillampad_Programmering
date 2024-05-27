package com.example.slutprojekt

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.text.Layout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.slutprojekt.ui.theme.SlutprojektTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val mp: MediaPlayer = MediaPlayer.create(this, R.raw.audio)
        super.onCreate(savedInstanceState)
        setContent {
            SlutprojektTheme {
                SlutprojektTheme {
                    var dificulty by remember {
                        mutableStateOf(0)
                    }
                    var i by remember {
                        mutableStateOf(0)
                    }

                    if (i == 0) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = Color.Cyan
                        )

                        {


                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                Arrangement.SpaceEvenly,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Text(
                                    text = "Flappy Bird x Frogger",
                                    fontSize = 50.sp,
                                    lineHeight = 90.sp,
                                    textAlign = TextAlign.Center,
                                    color = Color.Black,
                                    modifier = Modifier.background(Color.White)
                                )
                                Button(
                                    onClick = {
                                        dificulty = 0;i = 1
                                    },
                                    modifier = Modifier.size(175.dp, 100.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        Color.White
                                    )
                                ) {
                                    Text(
                                        text = "Långsamt",
                                        fontSize = 25.sp,
                                        color = Color.Black
                                    )
                                }
                                Button(
                                    onClick = {
                                        dificulty = 1;i = 1
                                    },
                                    modifier = Modifier.size(175.dp, 100.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        Color.White
                                    )
                                ) {
                                    Text(
                                        text = "Medium",
                                        fontSize = 25.sp,
                                        color = Color.Black
                                    )
                                }
                                Button(
                                    onClick = {
                                        dificulty = 2;i = 1
                                    },
                                    modifier = Modifier.size(175.dp, 100.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        Color.White
                                    )
                                ) {
                                    Text(
                                        text = "Snabbt",
                                        fontSize = 25.sp,
                                        color = Color.Black
                                    )
                                }
                                Button(
                                    onClick = {
                                        i = 2
                                    },
                                    modifier = Modifier.size(175.dp, 100.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        Color.White
                                    )
                                ) {
                                    Text(
                                        text = "Info",
                                        fontSize = 25.sp,
                                        color = Color.Black
                                    )
                                }
                                Button(
                                    onClick = {
                                        if (mp.isPlaying){
                                            mp.pause()
                                        }else{
                                            mp.start()
                                            mp.setLooping(true)
                                        }
                                    },
                                    modifier = Modifier.size(175.dp, 100.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        Color.White
                                    )
                                ) {
                                    Text(
                                        text = "Mute/Un Mute",
                                        fontSize = 25.sp,
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }
                    else if (i == 1) {
                        Column(
                            modifier = Modifier
                                .zIndex(1f)

                        ) {
                            Button(
                                onClick = { i = 0 },
                                modifier = Modifier.size(100.dp, 33.dp),
                                colors = ButtonDefaults.buttonColors(
                                    Color.White
                                )
                            ) {
                                Text(
                                    text = "Hem",
                                    fontSize = 15.sp,
                                    color = Color.Black
                                )
                            }
                        }
                        GameScreen(dificulty = dificulty)

                    } else if (i == 2) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = Color.Cyan
                        )

                        {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                Arrangement.SpaceEvenly,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Text(
                                    text = "Flappy Bird x Frogger går ut på att du trycker på skärmen så att fågeln flyger uppåt, men när du inte trycker faller fågeln. Sedan måste du undvika att träffa molnen. Nå så högt upp som möjligt.",
                                    fontSize = 25.sp,
                                    lineHeight = 45.sp,
                                    textAlign = TextAlign.Center,
                                    color = Color.Black,
                                    modifier = Modifier.background(Color.White)
                                )
                                Button(
                                    onClick = {
                                        i = 0
                                    },
                                    modifier = Modifier.size(175.dp, 100.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        Color.White
                                    )
                                ) {
                                    Text(
                                        text = "Hem",
                                        fontSize = 25.sp,
                                        color = Color.Black
                                    )
                                }
                            }
                        }

                        // A surface container using the 'background' color from the theme

                    }
                }
            }

        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SlutprojektTheme {
        Greeting("Android")
    }
}