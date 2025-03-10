package com.example.interfaces

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { UIPrincipal() }
    }
}

@Composable
/*fun UIPrincipal() {

    var cadTxtNombre by remember { mutableStateOf("")}
    var cadLblMensaje by remember { mutableStateOf("")}

    fun btnSaludar_click(){
        cadLblMensaje = "Hola $cadTxtNombre"
    }

    Column(Modifier.fillMaxSize().padding(16.dp), Arrangement.Top, Alignment.Start){
        Text("Como te llamas?")
        TextField(value = cadTxtNombre, onValueChange = {cadTxtNombre = it})
        Button(onClick = {btnSaludar_click()}){
            Text("Saludar")
        }
        Text(cadLblMensaje)
    }
}*/
fun UIPrincipal(){

    var cadTxtOp1 by remember { mutableStateOf("") }
    var cadTxtOp2 by remember { mutableStateOf("") }
    var cadTxtRes by remember { mutableStateOf("") }

    fun btnLimpiar_click(){
        cadTxtOp1 = ""
        cadTxtOp2 = ""
        cadTxtRes = ""
    }
    fun btnSumar_click(){
        val op1=cadTxtOp1.toIntOrNull() ?: 0 //convierte a entero, si es nulo devuelvo cero
        val op2=cadTxtOp2.toIntOrNull() ?: 0
        cadTxtRes=(op1 + op2).toString()

    }
    Column(Modifier.fillMaxSize().padding(16.dp), Arrangement.Top, Alignment.CenterHorizontally){
        Row(Modifier.fillMaxWidth().padding(16.dp), Arrangement.SpaceAround){
            Text("Número 1", Modifier.align(Alignment.CenterVertically))
            Text("Número 2", Modifier.align(Alignment.CenterVertically))
            Text("Resultado", Modifier.align(Alignment.CenterVertically))
        }
        Row(Modifier.fillMaxWidth().padding(16.dp), Arrangement.SpaceAround){
            TextField(value = cadTxtOp1, onValueChange = {cadTxtOp1 = it}, Modifier.weight(1f))
            Text("+", Modifier.padding(horizontal = 8.dp).align(Alignment.CenterVertically))
            TextField(value = cadTxtOp2, onValueChange = {cadTxtOp2 = it}, Modifier.weight(1f))
            Text("=", Modifier.padding(horizontal = 8.dp).align(Alignment.CenterVertically))
            TextField(value = cadTxtRes, onValueChange = {cadTxtRes = it}, Modifier.weight(1f))
        }
        Row(Modifier.fillMaxWidth().padding(16.dp), Arrangement.SpaceAround){
            Button(onClick = {btnLimpiar_click()}, Modifier.weight(1.5f).padding(16.dp)) {
                Text("Limpiar")
            }
            Button(onClick = {btnSumar_click()}, Modifier.weight(1.5f).padding(16.dp)) {
                Text("Sumar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Previsualizacion() {
    UIPrincipal()
}