package com.example.ventas

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@Composable
fun RequestCameraPermission() {
    val context = LocalContext.current

    //Lanza la solicitud
    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(context, "Permiso de Cámara otorgado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Permiso de Cámara denegado", Toast.LENGTH_SHORT).show()
            }
        }

    //Verifica si tienes el permiso
    val cameraPermissionStatus = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    )

    //Si no existe lo solicita
    LaunchedEffect(cameraPermissionStatus) {
        if (cameraPermissionStatus != PackageManager.PERMISSION_GRANTED) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    /*
    Column {
        // Puedes colocar el botón para que al pulsarlo active la solicitud
        Button(onClick = {
            // Llama a la solicitud del permiso si no está concedido
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }) {
            Text("Solicitar Permiso de Cámara")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Si el permiso ya ha sido concedido, puedes tomar una foto.")
    }
     */
}
