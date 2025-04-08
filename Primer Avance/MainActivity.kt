package com.example.conexion

import android.database.Cursor
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.util.Base64
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { UIPrincipal() }
    }
}

@Composable
fun UIPrincipal() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Productos Disponibles", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Button(onClick = { /* Acción de agregar producto */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF003366))) {
                Text("+")
            }
        }

        val auxSQLite = DBHelper(LocalContext.current)
        val base = auxSQLite.readableDatabase
        val cursor: Cursor = base.rawQuery("SELECT * FROM Productos", null)
        val lista = mutableListOf<Producto>()

        if (cursor.count > 0) {
            while (cursor.moveToNext()) {
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nombre"))
                val precio = cursor.getDouble(cursor.getColumnIndexOrThrow("Precio"))
                val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("Descripcion"))
                val imagenBase64 = cursor.getString(cursor.getColumnIndexOrThrow("Imagen"))

                lista.add(Producto(nombre, precio, descripcion, imagenBase64))
            }
        }

        cursor.close()
        base.close()

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            lista.forEach { producto ->
                ProductoCard(producto)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

fun decodeBase64ToBitmap(base64String: String): Bitmap? {
    val decodedString = Base64.decode(base64String, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
}

data class Producto(val nombre: String, val precio: Double, val descripcion: String, val imagenBase64: String?)

@Composable
fun ProductoCard(producto: Producto) {
    Card( colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            val bitmap = producto.imagenBase64?.let { decodeBase64ToBitmap(it) }

            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Imagen del Producto",
                    modifier = Modifier.size(80.dp)
                )
            } else {
                Box(modifier = Modifier.size(80.dp)) {
                    Image(
                        painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                        contentDescription = "Imagen Default"
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(producto.nombre, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("$${producto.precio}", fontSize = 14.sp)
                Text(producto.descripcion, fontSize = 12.sp)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = { /* Acción editar */ }) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_edit),
                        contentDescription = "Editar"
                    )
                }
                IconButton(onClick = { /* Acción eliminar */ }) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_delete),
                        contentDescription = "Eliminar"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUI() {
    UIPrincipal()
}