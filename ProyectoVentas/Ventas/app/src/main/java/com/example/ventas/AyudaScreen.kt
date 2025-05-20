package com.example.ventas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AyudaScreen(navController: NavController, viewModel: ProductoViewModel) {
    Column(
        modifier = Modifier.padding(16.dp).fillMaxSize().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp) //Espacio entre elementos
    ) {

        //Creamos el titulo
        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Text(
                text = "¿Cómo usar la App?",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = 1.5.sp,
                ),
                color = Color.Black,
                modifier = Modifier.align(Alignment.Center).padding(top = 80.dp, start = 16.dp, end = 16.dp)
            )
        }

        //Intro
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "La App te permite Agregar, Editar y Eliminar Productos fácilmente. " +
                        "A continuacion el paso a paso:",
                style = MaterialTheme.typography.bodyLarge,  color = Color.Black,
                modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Justify
            )
        }

        // Paso 1
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "1. Agregar Producto",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Para agregar un producto, ve a la pantalla principal y toca " +
                        "el botón de '+' que se encuentra en la parte infierior izquierda. " +
                        "Completa el formulario con la información  del producto, incluyendo " +
                        "el nombre, precio, artista, descripción e imagen.",
                style = MaterialTheme.typography.bodyLarge, color = Color.Black,
                modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Justify
            )
        }

        // Paso 2
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "2. Editar Producto",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Si deseas modificar la información del producto, ubica el producto a editar y toca el icono" +
                        " en forma de lápiz, te llevará a una pantalla donde podras cambiar los datos que desees, " +
                        "RECUERDA guardar los cambios.",
                style = MaterialTheme.typography.bodyLarge,  color = Color.Black,
                modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Justify
            )
        }

        // Paso 3
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "3. Eliminar Producto",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Para eliminar un producto, ubica el producto a eliminar " +
                        "toca el icono color rojo y asegúrate de confirmar la eliminación.",
                style = MaterialTheme.typography.bodyLarge,  color = Color.Black,
                modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Justify
            )
        }
    }
}
