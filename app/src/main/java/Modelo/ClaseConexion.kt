package Modelo

import java.sql.Connection
import java.sql.DriverManager

class ClaseConexion {

    fun cadenaConexion(): Connection? {
        try {
<<<<<<< HEAD
            val url = "jdbc:oracle:thin:@192.168.0.10:1521:xe"
            val usuario = "Ricardo_developer"
            val contrasena ="Ricardo2023."
=======
            val url = "jdbc:oracle:thin:@192.168.0.4:1521:xe"
            val usuario = "DIANA_DEVELOPER"
            val contrasena = "Diana#2006"
>>>>>>> 810ad44d40aa043741546475dc0dfd38eaaaf046

            val connection = DriverManager.getConnection(url, usuario, contrasena)
            return connection
        } catch (e: Exception) {
            println("error: $e")
            return null
        }
    }
}