package com.example.ventas

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "baseproyecto.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        //Crear tabla de Productos
        val query1 = """
        CREATE TABLE Productos (
            ID_Producto INTEGER PRIMARY KEY AUTOINCREMENT,
            Nombre TEXT NOT NULL,
            Artista TEXT NOT NULL,
            Precio DOUBLE NOT NULL,
            Descripcion TEXT,
            Imagen TEXT
        );
        """

        //Cadena en base54

        //Insertamos Datos
        val query2 = """
        INSERT INTO Productos (Nombre, Artista, Precio, Descripcion, Imagen)
        VALUES 
        ('Vessel', 'Twenty One Pilots', 299.99, 'Explora lasprofundidades de la mente humana con una fusión de indie pop, rap y rock.', ''),
        ('GNX', 'Kendrick Lamar', 349.99, 'Una obra maestra del hip-hop moderno, que combina letras introspectivas y una producción sonora única.', ''),
        ('The New Abnormal', 'The Strokes', 379.99, 'Un regreso brillante para la banda, con sonidos frescos y letras cargadas de melancolía.', ''),
        ('Circles', 'Mac Miller', 319.99, 'Álbum póstumo profundamente personal que combina elementos de hip-hop y jazz.', ''),
        ('Call Me If You Get Lost', 'Tyler, The Creator', 399.99, 'Un álbum conceptual vibrante que transporta a los oyentes a un viaje sonoro lleno de creatividad.', ''),
        ('Future Nostalgia', 'Dua Lipa', 399.99, 'Un álbum pop con un toque retro y futurista, lleno de himnos bailables y vibrantes.', ''),
        ('21', 'Adele', 349.99, 'La obra maestra que capturó corazones, con baladas cargadas de emoción y una voz incomparable.', ''),
        ('King of Kings', 'Don Omar', 299.99, 'Un álbum icónico que marcó un hito en el reguetón con letras profundas y ritmos pegajosos.', ''),
        ('Anti (Deluxe)', 'Rihanna', 379.99, 'Una fusión audaz de géneros que destaca la versatilidad artística de Rihanna.', ''),
        ('In the Zone', 'Britney Spears', 319.99, 'Un disco innovador que mezcla pop, electrónica y R&B, mostrando una nueva faceta de Britney.', '');
        """

        //Ejecuta Consultas
        db?.execSQL(query1)
        db?.execSQL(query2)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //Modificaciones cuando se Actualiza
        db?.execSQL("DROP TABLE IF EXISTS Productos")
        onCreate(db)
    }

    //Obtenemos todos los Producto
    fun getAllProductos(): List<Producto> {
        val productos = mutableListOf<Producto>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Productos", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("ID_Producto"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nombre"))
                val artista = cursor.getString(cursor.getColumnIndexOrThrow("Artista"))
                val precio = cursor.getDouble(cursor.getColumnIndexOrThrow("Precio"))
                val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("Descripcion"))
                val imagen = cursor.getString(cursor.getColumnIndexOrThrow("Imagen"))

                //Se usa la clase Producto (com.example.conexion.Producto)
                productos.add(Producto(id, nombre, artista, precio, descripcion, imagen))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return productos
    }

    //Insertamos un Producto
    fun insertProducto(producto: Producto): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("Nombre", producto.nombre)
            put("Artista", producto.artista)
            put("Precio", producto.precio)
            put("Descripcion", producto.descripcion)
            put("Imagen", producto.imagenBase64)
        }
        return db.insert("Productos", null, values)
    }

    //Actualizamos un Producto
    fun updateProducto(producto: Producto): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("Nombre", producto.nombre)
            put("Artista", producto.artista)
            put("Precio", producto.precio)
            put("Descripcion", producto.descripcion)
            put("Imagen", producto.imagenBase64)
        }
        return db.update("Productos", values, "ID_Producto = ?", arrayOf(producto.id.toString()))
    }

    //Se Elimina por Id
    fun deleteProducto(id: Int): Int {
        val db = this.writableDatabase
        return db.delete("Productos", "ID_Producto = ?", arrayOf(id.toString()))
    }
}



