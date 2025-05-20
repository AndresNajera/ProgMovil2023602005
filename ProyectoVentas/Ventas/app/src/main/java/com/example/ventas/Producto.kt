package com.example.ventas

data class Producto(
    val id: Int,
    val nombre: String,
    val artista: String,
    val precio: Double,
    val descripcion: String,
    val imagenBase64: String? = null
)