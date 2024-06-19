package diana.padilla.isss_salud

import Modelo.ClaseConexion
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest

class activity_ingreso : AppCompatActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ingreso)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnRegistrarse = findViewById<Button>(R.id.btnRegistrarse)
        val txtCorreo = findViewById<EditText>(R.id.txtCorreo)
        val txtContrasena = findViewById<EditText>(R.id.txtContrasena)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val logoISSS = findViewById<ImageView>(R.id.IvLogoIsss)
        val modoOscuro = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val correoPattern = Regex ("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")

        fun hashSHA256(input: String): String {
            val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }

        btnLogin.setOnClickListener {

            val correo = txtCorreo.text.toString()
            val contrasena = txtContrasena.text.toString()

            if (correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(
                    this,
                    "Error, para acceder debes llenar todas las casillas.",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!correoPattern.matches(correo)) {
                Toast.makeText(
                    this,
                    "El correo electrónico no es valido.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val pantallaNoticias = Intent(this, activity_noticias::class.java)

                CoroutineScope(Dispatchers.IO).launch {

                    val objConexion = ClaseConexion().cadenaConexion()

                    val contraseniaEncriptada = hashSHA256(txtContrasena.text.toString())

                    val comprobarUsuario =
                        objConexion?.prepareStatement("SELECT * FROM Usuarios WHERE correo_electronico = ? AND contrasena = ?")!!
                    comprobarUsuario.setString(1, txtCorreo.text.toString())
                    comprobarUsuario.setString(2, contraseniaEncriptada)
                    val resultado = comprobarUsuario.executeQuery()

                    if (resultado.next()) {
                        startActivity(pantallaNoticias)
                    } else {
                        runOnUiThread{
                            Toast.makeText(this@activity_ingreso, "Usuario no encontrado, verifique las credenciales", Toast.LENGTH_LONG).show()
                            println("contraseña $contraseniaEncriptada")
                        }
                    }
                }
            }

            if (modoOscuro == Configuration.UI_MODE_NIGHT_YES) {
                logoISSS.setImageResource(R.drawable.ic_modo_oscuro_logo)
            } else {
                logoISSS.setImageResource(R.drawable.id_logo_isss)
            }

            btnRegistrarse.setOnClickListener {
                val pantallaRegistrarse = Intent(this, activity_registrarse::class.java)
                startActivity(pantallaRegistrarse)
            }

            val txtOlvidoContrasena = findViewById<TextView>(R.id.txtForgotPassword)

            txtOlvidoContrasena.setOnClickListener {
                val pantallaOlvidoContrasena = Intent(this, activity_contrasena_enlace::class.java)
                startActivity(pantallaOlvidoContrasena)
            }
        }
    }
}