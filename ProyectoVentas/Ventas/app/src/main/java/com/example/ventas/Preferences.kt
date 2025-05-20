package com.example.ventas

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

class Preferences {

    //Fun para guardar los colores
    fun saveColors(context: Context, cardColor: Color, buttonColor: Color) {
        val sharedPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        //Se convierten a int los colores con representacion ARGB
        editor.putInt("cardColor", cardColor.toArgb())
        editor.putInt("buttonColor", buttonColor.toArgb())

        editor.apply()
    }

    //Funcion para recuperar los colores
    fun getSavedColors(context: Context): Pair<Color, Color> {
        val sharedPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)

        //Recupera los colores como int y convierte al color nuevo
        val cardColorInt = sharedPreferences.getInt("cardColor", 0xFFEEEEEE.toInt()) //Valor por defecto
        val buttonColorInt = sharedPreferences.getInt("buttonColor", 0xFF4CAF50.toInt()) //Valor por defecto

        //Devuelve los colores a como estaban
        return Pair(Color(cardColorInt), Color(buttonColorInt)) //Conviert el Int a Color
    }
}

//Convierte el color de string a hexa
fun Color.toHex(): String {
    return String.format("#%02X%02X%02X", red.toInt() * 255, green.toInt() * 255, blue.toInt() * 255)
}

//Convierte el color de string a hexa
fun String.toColor(): Color {
    return Color(android.graphics.Color.parseColor(this))
}
