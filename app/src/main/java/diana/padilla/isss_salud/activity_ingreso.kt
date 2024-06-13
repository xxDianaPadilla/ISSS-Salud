package diana.padilla.isss_salud

import Modelo.ClaseConexion
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

class activity_ingreso : AppCompatActivity() {
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
        val correoPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()


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
                Log.i("Test de credenciales", "Correo: $correo y Contraseña: $contrasena")
            }
        }

        btnLogin.setOnClickListener {
            val pantallaNoticias = Intent(this, activity_noticias::class.java)
            CoroutineScope(Dispatchers.IO).launch {

                val objConexion = ClaseConexion().cadenaConexion()

                val comprobarUsuario =
                    objConexion?.prepareStatement("SELECT * FROM Usuarios WHERE correo_electronico = ? AND contrasena = ?")!!
                    comprobarUsuario.setString(1, txtCorreo.text.toString())
                    comprobarUsuario.setString(2, txtContrasena.text.toString())

                    val resultado = comprobarUsuario.executeQuery()
                    if (resultado.next()) {
                        startActivity(pantallaNoticias)
                    } else {
                        println("Usuario no encontrado, verifique las credenciales")
                    }
            }
        }

        if(modoOscuro == Configuration.UI_MODE_NIGHT_YES){
            logoISSS.setImageResource(R.drawable.ic_modo_oscuro_logo)
        }else{
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