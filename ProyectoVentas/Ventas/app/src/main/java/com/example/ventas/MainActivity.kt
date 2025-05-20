package com.example.ventas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.asImageBitmap
import android.graphics.Bitmap
import android.util.Base64
import android.graphics.BitmapFactory
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private val dbHelper = DBHelper(this)
    private val viewModel: ProductoViewModel by viewModels {
        ProductoViewModelFactory(dbHelper)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            val scope = rememberCoroutineScope()

            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    AppDrawer(navController, drawerState, scope)
                }
            ) {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text("Productos Disponibles", color = Color.White) },
                            navigationIcon = {
                                IconButton(
                                    onClick = { scope.launch { drawerState.open() } },
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.menu), //Img del Drawable
                                        contentDescription = "Menú",
                                        modifier = Modifier.size(28.dp),
                                        tint = Color.White
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = Color(0xFF003366), //Solo para la topBar
                                titleContentColor = Color.White
                            )
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                navController.navigate("agregar") {
                                    popUpTo("main") { inclusive = false }
                                }
                            },
                            containerColor = Color(0xFF003366)//Solo para el boton flotante
                        ) {
                            Text("+", color = Color.White)
                        }
                    }
                ) { paddingValues ->
                    NavHost(navController = navController, startDestination = "main") {
                        //Son los composable para navegar
                        composable("main") {
                            UIPrincipal(viewModel = viewModel, navController = navController, padding = paddingValues)
                        }
                        composable("agregar") {
                            AgregarProductoScreen(navController, viewModel)
                        }
                        composable("personalizar") {
                            PersonalizarScreen(navController, viewModel)
                        }
                        composable("ayuda") {
                            AyudaScreen(navController, viewModel)
                        }
                        composable("acerca") {
                            AcercaScreen(navController, viewModel)
                        }
                        composable("editar/{productoId}") { backStackEntry ->
                            val productoId = backStackEntry.arguments?.getString("productoId")?.toInt() ?: 0
                            EditarProductoScreen(navController, viewModel, productoId)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UIPrincipal(viewModel: ProductoViewModel, navController: NavHostController, padding: PaddingValues) {
    val productos = viewModel.productos.value

    var productoAEditar by remember { mutableStateOf<Producto?>(null) }

    //Recibe los productos para que puedas editar o eliminar
    Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
        productos.forEach { producto ->
            ProductoCard(
                producto = producto,
                onEdit = {
                    productoAEditar = producto
                    navController.navigate("editar/${producto.id}")
                },
                onDelete = { viewModel.eliminarProducto(producto.id) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ProductoCard(producto: Producto, onEdit: () -> Unit, onDelete: () -> Unit) {
    //Dialogo de Confirmacion
    var mostrarDialogo by remember { mutableStateOf(false) }

    // Recuperar los colores guardados de las preferencias
    val context = LocalContext.current
    val preferences = Preferences()
    val savedColors = preferences.getSavedColors(context)

    // Obtiene los colores del state
    val cardColor = savedColors.first

    //Si es Vdd
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false }, //Nombre del card que eliminas
            title = { Text(text = producto.nombre) },
            text = { Text("¿Estás seguro de eliminar este producto?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete()
                    mostrarDialogo = false
                }) {
                    Text("Si")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    //Card de Producto
    Card(
        colors = CardDefaults.cardColors(containerColor = cardColor), //Se actualiza el color
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            val bitmap = producto.imagenBase64?.let { decodeBase64ToBitmap(it) }

            //Mostrar la Img
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Imagen del Producto",
                    modifier = Modifier.size(80.dp)
                )
            } else {
                Box(modifier = Modifier.size(80.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.imgdefecto),//Pusimos una img default
                        contentDescription = "Imagen Default"
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            //Detalles del producto
            Column(modifier = Modifier.weight(1f)) {
                //Mostrar el nombre, artista, precio y descripción del producto
                //Se puede mostrar el id pero no tiene caso
                //Text(producto.id.toString(), fontSize = 12.sp)
                Text(producto.nombre, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(producto.artista, fontSize = 12.sp)
                Text("$${producto.precio}", fontSize = 14.sp)
                Text(producto.descripcion, fontSize = 12.sp)
            }

            //Botones de Editar y Eliminar
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                //Editar
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(48.dp)//Tamano del icono
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.editar), //Icono de editar
                        contentDescription = "Editar",
                        modifier = Modifier.size(28.dp), //Tamano de la img
                        tint = Color.Unspecified
                    )
                }

                //Eliminar
                IconButton(
                    onClick = { mostrarDialogo = true }, //Dialogo de confirmacion
                    modifier = Modifier.size(48.dp) //Tamano del icono
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.eliminar),
                        contentDescription = "Eliminar",
                        modifier = Modifier.size(28.dp), //Tamano de la img
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}

//Convertimos la img codificada en base
fun decodeBase64ToBitmap(base64String: String): Bitmap? {
    val decodedString = Base64.decode(base64String, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
}



