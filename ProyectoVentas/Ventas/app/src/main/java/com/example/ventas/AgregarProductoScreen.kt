package com.example.ventas

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.graphics.Bitmap
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.core.content.FileProvider
import java.io.File
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Build
import androidx.compose.ui.graphics.Color
import android.Manifest
import android.content.ContentValues
import android.os.Environment
import android.provider.MediaStore

@Composable
fun AgregarProductoScreen(navController: NavController, viewModel: ProductoViewModel) {
    val context = LocalContext.current

    //Variables para Agregar
    var nombre by remember { mutableStateOf("") }
    var artista by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imagenBase64 by remember { mutableStateOf<String?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    //Preferencias de Colores para mandarlos a llamar
    val preferences = Preferences()
    val savedColors = preferences.getSavedColors(context)
    val buttonColor = savedColors.second

    //Permisos para la Camara y Galeria
    var permisoCamara by remember { mutableStateOf(false) }
    var permisoGaleria by remember { mutableStateOf(false) }

    //Para tomar foto
    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess && imageUri != null) {
            imagenBase64 = uriToBase64(context, imageUri!!)

            //Se guarda la foto en la galeria con mediastore
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "producto_${System.currentTimeMillis()}.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES) //Para que se vaya a lacarpeta Pictures
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
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imageUri = it
            imagenBase64 = uriToBase64(context, it)
        }
    }

    //Se crea el archivo temporal para la imagen
    val photoFile = remember { File(context.cacheDir, "photo.jpg") }
    val photoUri: Uri = FileProvider.getUriForFile(
        context, "com.example.ventas.fileprovider",  //El provider se configuro en el androidmanifest
        photoFile
    )

    //Para solicitar permiso de Camara
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permisoCamara = isGranted
    }

    //Para solicitar permiso de galeria
    val galleryPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permisoGaleria = isGranted
    }

    //Se verifican ambos permiso de camara y galeria
    LaunchedEffect(Unit) {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            permisoCamara = true
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                permisoGaleria = true
            } else {
                galleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            permisoGaleria = true
        }
    }


    Column(
        modifier = Modifier.padding(16.dp).fillMaxSize().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Agregar Producto", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 80.dp))

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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), //Teclado numerico
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
            enabled = permisoCamara, //Habilita solo si tiene permiso
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)//Se manda a llamar para que cambie el color
        ) {
            Text("Tomar Foto")
        }

        //Boton para selecionar de galeria
        Button(
            onClick = {
                galleryLauncher.launch("image/*")
            },
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)//Se manda a llamar para que cambie el color
        ) {
            Text("Seleccionar de Galería")
        }

        //Para mostrar la imagen ya sea si fue de camara o se selecciono foto
        imagenBase64?.let {
            decodeBase64ToBitmap(it)?.let { bitmap ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Imagen Seleccionada",
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

        //Boton para guardar el producto
        Button(
            onClick = {
                val precioDouble = precio.toDoubleOrNull()
                if (nombre.isNotBlank() && precioDouble != null) {
                    val producto = Producto(
                        id = 0,
                        nombre = nombre,
                        artista = artista,
                        precio = precioDouble,
                        descripcion = descripcion,
                        imagenBase64 = imagenBase64 //Null o asi
                    )
                    viewModel.agregarProducto(producto)

                    navController.navigate("main") {
                        popUpTo("main") { inclusive = true }
                        launchSingleTop = true //Hace que no recargue la pantalla anterior
                    }
                } else {
                    Toast.makeText(context, "Completa los Campos Obligatorios", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)//Se manda a llamar para que cambie el color
        ) {
            Text("Guardar Producto")
        }
    }
}

//Para convertir la imagen a base64
fun uriToBase64(context: Context, uri: Uri): String? {
    return try {
        val inputStream1 = context.contentResolver.openInputStream(uri) ?: return null //Lee la imagen
        val inputStream2 = context.contentResolver.openInputStream(uri) ?: return null  //Lee la orientacion de la img

        val bitmap = BitmapFactory.decodeStream(inputStream1)//Convierte la img

        val exifInterface = ExifInterface(inputStream2)//Lee la img
        val rotationAngle = getRotationAngle(exifInterface)//Obtiene le angulo con la que fue tomada
        val rotatedBitmap = rotateBitmap(bitmap, rotationAngle)//Para ver si rota la img si es necesario

        val resized = Bitmap.createScaledBitmap(rotatedBitmap, 300, 300, true)//Tamano
        val outputStream = java.io.ByteArrayOutputStream()
        resized.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)//Comprime la img con calidad de 80
        Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)//Convierte la img en cadena base64
    } catch (e: Exception) {
        Log.e("uriToBase64", "Error al convertir URI a Base64: ${e.message}")
        e.printStackTrace()
        null
    }
}


//Fun para obtener el angulo que fue tomada la img
fun getRotationAngle(exifInterface: ExifInterface): Float {
    val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
    Log.d("ExifRotation", "Orientation: $orientation")
    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90f
        ExifInterface.ORIENTATION_ROTATE_180 -> 180f
        ExifInterface.ORIENTATION_ROTATE_270 -> 270f
        else -> 0f
    }
}

//Rotar la img en memoria
fun rotateBitmap(source: Bitmap, angle: Float): Bitmap {
    Log.d("Rotate", "Rotating image by $angle degrees")
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
}

