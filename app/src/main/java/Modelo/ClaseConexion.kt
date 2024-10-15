package Modelo

import java.sql.Connection
import java.sql.DriverManager

class ClaseConexion {

    fun cadenaConexion(): Connection? {
        try {
            val url = "jdbc:oracle:thin:@192.168.0.13:1521:xe"
            val usuario = "RicardoPTC"
            val contrasena ="PTC1"

            val connection = DriverManager.getConnection(url, usuario, contrasena)
            return connection
        } catch (e: Exception) {
            println("error: $e")
            return null
        }
    }
}