package com.example.ventas

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.ColorUtils
import androidx.navigation.NavController

@Composable
fun PersonalizarScreen(navController: NavController, viewModel: ProductoViewModel) {
    val context = LocalContext.current
    val preferences = Preferences()

    //Se recuperan los colores guardados
    val savedColors = preferences.getSavedColors(context)

    val cardColor = savedColors.first //El primer color para la card
    var cardColorState by remember { mutableStateOf(cardColor) }

    val buttonColor = savedColors.second //El segundo color para el botón
    var buttonColorState by remember { mutableStateOf(buttonColor) }

    //Para Personalizar
    Column(
        modifier = Modifier.padding(16.dp).fillMaxSize().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //Titulo
        Text(
            "Personalización",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 80.dp),
            textAlign = TextAlign.Center
        )

        //Personalizacion de colores para las cards
        Text(
            text = "Color de la Tarjeta",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        ColorPicker(currentColor = cardColorState, onColorChange = { newColor ->
            cardColorState = newColor
        })

        //Personalizacion de colores para los botones
        Text(
            text = "Color del Botón",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        ColorPicker(currentColor = buttonColorState, onColorChange = { newColor ->
            buttonColorState = newColor
        })

        Spacer(modifier = Modifier.height(32.dp))

        //Boton para guardar los cambios
        Button(
            onClick = {
                preferences.saveColors(context, cardColorState, buttonColorState)
                navController.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Guardar Cambios",
                style = MaterialTheme.typography.labelLarge,
                color = Color.White
            )
        }
    }
}


@Composable
fun ColorPicker(currentColor: Color, onColorChange: (Color) -> Unit) {
    //Convertimos el color inicial a hue
    val initialHue = remember {
        val hsv = FloatArray(3)
        ColorUtils.colorToHSL(currentColor.toArgb(), hsv)
        hsv[0]
    }

    var hue by remember { mutableStateOf(initialHue) }

    //Para poner colores claros
    val color = remember(hue) {
        Color.hsv(hue, 0.6f, 0.8f)
    }

    LaunchedEffect(hue) { onColorChange(color) }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val animatedColor by animateColorAsState(targetValue = color)
        Box(
            modifier = Modifier.width(200.dp).height(80.dp).background(animatedColor)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("Selecciona Tono")

        Slider(
            value = hue,
            onValueChange = { newHue -> hue = newHue },
            valueRange = 0f..360f,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}



