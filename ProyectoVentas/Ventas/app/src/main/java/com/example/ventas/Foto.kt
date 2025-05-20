package com.example.ventas

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.compose.ui.layout.ContentScale
import java.io.InputStream

@Composable
fun TakePhotoWithPermission() {
    val context = LocalContext.current

    //Para permisos
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    var hasPermission by remember { mutableStateOf(false) }

    //Se verifica si ya tiene permiso de camara
    LaunchedEffect(Unit) {
        hasPermission = checkCameraPermission(context)
    }

    //Si no lo tiene se muestra para pedirlo
    if (!hasPermission) {
        RequestCameraPermission()
    } else {
        //Muestra resultado para tomar la foto
        val takePictureLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) { isTaken ->
            if (isTaken && imageUri != null) {
                //Convierte el URI de la img a un Bitmap
                val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri!!)
                bitmap = BitmapFactory.decodeStream(inputStream)
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                //Obtiene una URI donde almacena la foto
                val photoUri = getImageUri(context)
                imageUri = photoUri
                takePictureLauncher.launch(photoUri)
            }) {
                Text("Tomar Foto")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Si tiene un Bitmap lo muestra
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Imagen tomada",
                    modifier = Modifier.height(200.dp).width(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

//Metodo para verifiacra que hay permiso de camara
fun checkCameraPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED
}

//Metodo para crear la URI que almacena la foto
fun getImageUri(context: Context): Uri {
    val contentValues = ContentValues().apply {
        put(android.provider.MediaStore.Images.Media.TITLE, "new_photo")
        put(android.provider.MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    }
    return context.contentResolver.insert(
        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
    ) ?: throw Exception("No se pudo obtener la URI de la imagen")
}
