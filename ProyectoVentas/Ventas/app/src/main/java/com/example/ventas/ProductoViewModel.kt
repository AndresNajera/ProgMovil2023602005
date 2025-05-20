package com.example.ventas

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ProductoViewModelFactory(private val dbHelper: DBHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductoViewModel::class.java)) {
            return ProductoViewModel(dbHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class ProductoViewModel(private val dbHelper: DBHelper) : ViewModel() {

    // lista de productos
    private val _productos = mutableStateOf<List<Producto>>(emptyList())
    val productos: State<List<Producto>> = _productos

    //Cargas productos desde la base
    init {
        cargarProductos()
    }

    //Cargas productos desde la base
    private fun cargarProductos() {
        viewModelScope.launch {
            //Obtienes los productos de la base de datos
            val productosDeDb = dbHelper.getAllProductos()

            //Actualizas el estado con los productos de la base
            _productos.value = productosDeDb
        }
    }

    //Agregar Producto
    fun agregarProducto(producto: Producto) {
        viewModelScope.launch {
            dbHelper.insertProducto(producto)
            _productos.value = dbHelper.getAllProductos()
        }
    }

    //Ediatr Producto
    fun editarProducto(producto: Producto) {
        viewModelScope.launch {
            dbHelper.updateProducto(producto)
            _productos.value = dbHelper.getAllProductos()
        }
    }

    //Eliminas el Producto
    fun eliminarProducto(id: Int) {
        viewModelScope.launch {
            //Eliminas el producto de la base de datos
            val rowsDeleted = dbHelper.deleteProducto(id)

            //Se actualiza la lista
            if (rowsDeleted > 0) {
                _productos.value = _productos.value.filter { it.id != id }
            }
        }
    }
}
