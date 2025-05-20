package com.example.ventas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppDrawer(navController: NavController, drawerState: DrawerState, scope: CoroutineScope) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Text("Menú", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            //Inicio
            TextButton(onClick = {
                scope.launch { drawerState.close() }
                navController.navigate("main") {
                    popUpTo("main") { inclusive = true } //Hace que no recargue la pantalla anterior
                }
            }) {
                Text(
                    text = "Inicio",
                    color = Color.Black
                )
            }

            //Personalizar
            TextButton(onClick = {
                scope.launch { drawerState.close() }
                navController.navigate("personalizar") {
                    popUpTo("main") { inclusive = false }
                    launchSingleTop = true //Hace que no recargue la pantalla anterior
                }
            }) {
                Text(
                    text = "Personalizar",
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            //Ayuda
            TextButton(onClick = {
                scope.launch { drawerState.close() }
                navController.navigate("ayuda") {
                    popUpTo("main") { inclusive = false }
                    launchSingleTop = true //Hace que no recargue la pantalla anterior
                }
            }) {
                Text(
                    text = "Ayuda",
                    color = Color.Black
                )
            }

            //Acerca
            TextButton(onClick = {
                scope.launch { drawerState.close() }
                navController.navigate("acerca") {
                    popUpTo("main") { inclusive = false }
                    launchSingleTop = true //Hace que no recargue la pantalla anterior
                }
            }) {
                Text(
                    text = "Acerca de la App",
                    color = Color.Black
                )
            }
        }
    }
}

