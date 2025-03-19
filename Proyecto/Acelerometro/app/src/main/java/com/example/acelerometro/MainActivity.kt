package com.example.acelerometro

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import android.util.Log




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { NavigationComponent() }
    }
}

@Composable
fun NavigationComponent(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "UIPrincipal") {
        composable("UIPrincipal") {
            UIPrincipal(navController = navController)
        }
        composable("PantallaBaloncesto") {
            PantallaBaloncesto()
        }
        composable("PantallaTenis") {
            PantallaTenis()
        }
        composable("PantallaGolf") {
            PantallaGolf()
        }
        composable("PantallaBeisbol") {
            PantallaBeisbol()
        }
    }
}

class SensorManagerUtil(private val context: Context) : SensorEventListener {

    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    // para la detección del golpe (Boolean)
    private val threshold = 12.0f
    private var sensorListener: (Boolean) -> Unit = { _ -> }

    // para el procesamiento de la aceleración (Float), este para baseball
    private var currentAcceleration = 0.0f
    private var previousAcceleration = 0.0f
    private val accelerationThreshold = 3.0f
    private val homeRunThreshold = 50.0f
    var accelerationCallback: ((Float) -> Unit)? = null

    // boolean para todas menos baseball
    fun startListeningBoolean(listener: (Boolean) -> Unit) {
        sensorListener = listener
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    // float para baseball
    fun startListeningFloat(callback: (Float) -> Unit) {
        accelerationCallback = callback
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    // detener el sensor
    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val x = it.values[0]
                val y = it.values[1]
                val z = it.values[2]
                val acceleration = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()

                // detecta golpe (Boolean)
                if (acceleration > threshold) {
                    sensorListener(true)
                }

                // notifica el valor de aceleración (Float)
                currentAcceleration = acceleration
                if (Math.abs(currentAcceleration - previousAcceleration) > accelerationThreshold) {
                    previousAcceleration = currentAcceleration
                    accelerationCallback?.invoke(currentAcceleration)
                }
            }
        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}

@Composable
fun UIPrincipal(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { navController.navigate("PantallaBaloncesto") }) {
            Text(text = "Baloncesto")
        }
        Button(onClick = { navController.navigate("PantallaTenis") }) {
            Text(text = "Tenis")
        }
        Button(onClick = { navController.navigate("PantallaGolf") }) {
            Text(text = "Golf")
        }
        Button(onClick = { navController.navigate("PantallaBeisbol") }) {
            Text(text = "Béisbol")
        }
    }
}


@Composable
fun PantallaBaloncesto(){
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.balonbasquet),
            contentDescription = "Basquetball",
            modifier = Modifier.size(400.dp)  // aumenta el tamaño de la imagen
        )
    }
    val context = LocalContext.current
    val sensorManager = remember { SensorManagerUtil(context) }
    var golpeDetected by remember { mutableStateOf(false) }


    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.basketball) }

    // empieza la escucha del acelerometro
    LaunchedEffect(true) {
        sensorManager.startListeningBoolean { golpe ->
            golpeDetected = golpe

            // reproduce el sonido cuando detecta golpe
            if (golpe) {
                if (!mediaPlayer.isPlaying) {
                    mediaPlayer.start()
                }
            }
        }
    }

    DisposableEffect(Unit) { //detiene sonido cuando se sale de la pantalla, cuando se destruye el composable
        onDispose {
            sensorManager.stopListening()
            mediaPlayer.release()
        }
    }
}

@Composable
fun PantallaTenis(){
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.raqueta),
            contentDescription = "Tenis",
            modifier = Modifier.size(400.dp)
        )
    }
    val context = LocalContext.current
    val sensorManager = remember { SensorManagerUtil(context) }
    var golpeDetected by remember { mutableStateOf(false) }


    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.tenis) }

    LaunchedEffect(true) {
        sensorManager.startListeningBoolean { golpe ->
            golpeDetected = golpe

            if (golpe) {
                mediaPlayer.start()
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            sensorManager.stopListening()
            mediaPlayer.release()
        }
    }
}

@Composable
fun PantallaGolf(){
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.golf),
            contentDescription = "Golf",
            modifier = Modifier.size(400.dp)
        )
    }
    val context = LocalContext.current
    val sensorManager = remember { SensorManagerUtil(context) }
    var golpeDetected by remember { mutableStateOf(false) }

    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.golf) }

    LaunchedEffect(true) {
        sensorManager.startListeningBoolean { golpe ->
            golpeDetected = golpe

            if (golpe) {
                mediaPlayer.start()
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            sensorManager.stopListening()
            mediaPlayer.release()
        }
    }
}


@Composable
fun PantallaBeisbol(){
    val context = LocalContext.current
    val sensorManager = remember { SensorManagerUtil(context) }

    var golpeDetected by remember { mutableStateOf(false) }
    var homeRunDetected by remember { mutableStateOf(false) }


    val mediaPlayerSwing = remember { MediaPlayer.create(context, R.raw.bateo1) }
    val mediaPlayerHomeRun = remember { MediaPlayer.create(context, R.raw.baseball) }

    val swingThreshold = 15.0f // para el golpe normal
    val homeRunThreshold = 40.0f // para el home run

// recordar el valor de aceleración actualizado
    var currentAcceleration by remember { mutableStateOf(0.0f) }

    LaunchedEffect(true) {
        var hasSwingOccurred = false
        sensorManager.startListeningFloat { acceleration ->
            currentAcceleration = acceleration

            // detecta un golpe normal (swing)
            if (acceleration > swingThreshold && !golpeDetected && !homeRunDetected) {
                golpeDetected = true
                hasSwingOccurred = true // Se registra que ocurrió un swing
                if (!mediaPlayerSwing.isPlaying) {
                    mediaPlayerSwing.start()
                }

            }

            // detecta un home run después de un swing
            if (acceleration > homeRunThreshold && !homeRunDetected && hasSwingOccurred) {
                homeRunDetected = true
                golpeDetected = false
                hasSwingOccurred = false
                if (!mediaPlayerHomeRun.isPlaying) {
                    mediaPlayerHomeRun.start()
                }
            }

            // resetea si no se cumplen umbrales de golpe normal o home run
            if (acceleration <= swingThreshold) {
                golpeDetected = false
            }
            if (acceleration <= homeRunThreshold) {
                homeRunDetected = false
            }

            Log.d("Swing", "Aceleración detectada: $acceleration")
            Log.d("Swing", "Swing previo: $hasSwingOccurred, Golpe: $golpeDetected, HomeRun: $homeRunDetected")
        }
    }


    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.bate),
            contentDescription = "Baseball",
            modifier = Modifier.size(400.dp)
        )
    }


    DisposableEffect(Unit) {
        onDispose {
            sensorManager.stopListening()
            mediaPlayerSwing.release()
            mediaPlayerHomeRun.release()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Previsualizacion() {
    NavigationComponent()
}