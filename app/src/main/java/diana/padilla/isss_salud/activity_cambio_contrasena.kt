package diana.padilla.isss_salud

import Modelo.ClaseConexion
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.security.MessageDigest

class activity_cambio_contrasena : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cambio_contrasena)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nuevaContrasena = findViewById<EditText>(R.id.txtNuevaContraseña)
        val confirmarContrasena = findViewById<EditText>(R.id.txtConfirmarContraseña)
        val cambiarContrasena = findViewById<Button>(R.id.btnCambiarContraseña)

        val correoRecu = activity_correo_para_codigo.variablesGlobalesCorreoparacodigo.correoRecu


        fun hashSHA256(contrasenaEncriptada: String): String{
            val bytes = MessageDigest.getInstance("SHA-256").digest(contrasenaEncriptada.toByteArray())
            return bytes.joinToString(""){ "%02x".format(it) }
        }

        cambiarContrasena.setOnClickListener {
        if (nuevaContrasena == confirmarContrasena){
            fun actualizarContrasena(nuevaContrasena: String){
                GlobalScope.launch(Dispatchers.IO){
                    val objConexion = ClaseConexion().cadenaConexion()
                    val contrasenaEncriptacion = hashSHA256(nuevaContrasena.toString())

                    val actualizarContrasena =objConexion?.prepareStatement("UPDATE Usuarios SET contrasena = ? WHERE correo_electronico = ?")!!
                    actualizarContrasena.setString(1, contrasenaEncriptacion)
                    actualizarContrasena.setString(2, correoRecu)
                    actualizarContrasena.executeUpdate()
                }
            }
        } else{
            Toast.makeText(
                this@activity_cambio_contrasena,
                "Error, las contraseñas no coinciden.",
                Toast.LENGTH_SHORT
            ).show()
        }
            }


        val logoISSS = findViewById<ImageView>(R.id.IvLogoIsss)
        val modoOscuro = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(modoOscuro == Configuration.UI_MODE_NIGHT_YES){
            logoISSS.setImageResource(R.drawable.ic_modo_oscuro_logo)
        }else{
            logoISSS.setImageResource(R.drawable.id_logo_isss)
        }

        val fondoCirculo = findViewById<ConstraintLayout>(R.id.clFondoCirculo)
        val modoOscuro2 = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(modoOscuro2 == Configuration.UI_MODE_NIGHT_YES){
            fondoCirculo.setBackgroundResource(R.drawable.ic_circulo_oscuro)
        }else{
            fondoCirculo.setBackgroundResource(R.drawable.ic_circulo)
        }

        val fondoIcon = findViewById<ConstraintLayout>(R.id.clLock)
        val modoOscuro3 = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(modoOscuro3 == Configuration.UI_MODE_NIGHT_YES){
            fondoIcon.setBackgroundResource(R.drawable.ic_dark_lock)
        }else{
            fondoIcon.setBackgroundResource(R.drawable.ic_lock)
        }

        val btnCambiarPassword = findViewById<Button>(R.id.btnCambiarContraseña)

        btnCambiarPassword.setOnClickListener {
            val pantallaMensajeContrasena = Intent(this, activity_nueva_contrasena::class.java)
            startActivity(pantallaMensajeContrasena)
        }

    }
}