package com.example.ventas

import android.content.Intent
import android.content.pm.PackageManager
import androidx.navigation.NavController
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import java.lang.Exception
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import android.Manifest
import android.content.ContentValues
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.ui.graphics.Color

@Composable
fun EditarProductoScreen(navController: NavController, viewModel: ProductoViewModel, productoId: Int) {
    val context = LocalContext.current
    val producto = viewModel.productos.value.find { it.id == productoId }

    //Variables para Editar
    var nombre by remember { mutableStateOf(producto?.nombre ?: "") }
    var artista by remember { mutableStateOf(producto?.artista ?: "") }
    var precio by remember { mutableStateOf(producto?.precio?.toString() ?: "") }
    var descripcion by remember { mutableStateOf(producto?.descripcion ?: "") }
    var imagenBase64 by remember { mutableStateOf(producto?.imagenBase64) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    //Preferencias de Colores para mandarlos a llamar
    val preferences = Preferences()
    val savedColors = preferences.getSavedColors(context)
    val buttonColor = savedColors.second

    //Permisos para la Camara y Galeria
    var permisoCamara by remember { mutableStateOf(false) }
    var permisoGaleria by remember { mutableStateOf(false) }

    //Crea un archivo tempo para la img
    val photoFile = remember { File(context.cacheDir, "photo_edit.jpg") }
    val photoUri: Uri = FileProvider.getUriForFile(
        context, "com.example.ventas.fileprovider", photoFile
    )

    //Para tomar foto
    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess && imageUri != null) {
            imagenBase64 = uriToBase64(context, imageUri!!)

            //Se guarda la foto en la galeria con mediastore
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "producto_${System.currentTimeMillis()}.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES) // Coloca la imagen en la carpeta "Pictures"
            }

            val resolver = context.contentResolver
            val savedUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            savedUri?.let { uri ->
                resolver.openOutputStream(uri).use { outputStream ->
                    context.contentResolver.openInputStream(imageUri!!)?.copyTo(outputStream!!)
                }
            }
        } else {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    //Para usar galería con SAF
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            imagenBase64 = uriToBase64(context, it)
        }
    }

    //Para solicitar permiso de Camara
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permisoCamara = isGranted
    }

    //Solicita permiso por si no hay
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            permisoCamara = true
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxSize().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Editar Producto", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 80.dp))

        //Combos para agregar el Producto
        //Nombre
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Nombre")
                    Text("*", style = MaterialTheme.typography.bodyLarge)
                }
            },
            modifier = Modifier.fillMaxWidth(0.9f),
        )
        //Artista
        OutlinedTextField(
            value = artista,
            onValueChange = { artista = it },
            label = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Artista")
                    Text("*",  style = MaterialTheme.typography.bodyLarge)
                }
            },
            modifier = Modifier.fillMaxWidth(0.9f),

            )
        //Precio
        OutlinedTextField(
            value = precio,
            onValueChange = { precio = it },
            label = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Precio")
                    Text("*",  style = MaterialTheme.typography.bodyLarge)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(0.9f),

            )
        //Descripcion
        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(0.9f)
        )

        //Boton para tomar la foto, se habilita si se concede permiso
        Button(
            onClick = {
                imageUri = photoUri
                takePictureLauncher.launch(photoUri)
            },
            enabled = permisoCamara,
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)//Se manda a llamar para que cambie el color
        ) {
            Text("Tomar Foto")
        }

        //Boton para selecionar de galeria
        Button(
            onClick = { galleryLauncher.launch("image/*") },
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)//Se manda a llamar para que cambie el color
        ) {
            Text("Seleccionar Imagen")
        }

        //Para mostrar la imagen ya sea si fue de camara o se selecciono foto
        imagenBase64?.let {
            decodeBase64ToBitmap(it)?.let { bitmap ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Imagen seleccionada",
                        modifier = Modifier.fillMaxWidth().height(180.dp).padding(top = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    //Boton para eliminar la foto
                    Button(
                        onClick = {
                            imagenBase64 = null
                            imageUri = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Eliminar Imagen", color = Color.White)
                    }
                }
            }
        }

        //Boton para guardar el producto editado
        Button(onClick = {
            try {
                val precioDouble = precio.toDoubleOrNull()
                if (nombre.isNotBlank() && precioDouble != null) {
                    val productoEditado = Producto(
                        id = productoId,
                        nombre = nombre,
                        artista = artista,
                        precio = precioDouble,
                        descripcion = descripcion,
                        imagenBase64 = imagenBase64 //Null o asi
                    )
                    viewModel.editarProducto(productoEditado)

                    navController.navigate("main") {
                        popUpTo("main") { inclusive = true }
                        launchSingleTop = true //Hace que no recargue la pantalla anterior
                    }
                } else {
                    Toast.makeText(context, "Completa los Campos Obligatorios", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error al Editar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }, colors = ButtonDefaults.buttonColors(containerColor = buttonColor)//Se manda a llamar para que cambie el color
        ) {
            Text("Guardar Cambios")
        }
    }
}


