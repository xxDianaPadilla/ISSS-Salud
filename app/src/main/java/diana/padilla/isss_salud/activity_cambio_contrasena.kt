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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.sql.PreparedStatement
import java.sql.ResultSet

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


        fun hashSHA256(contrasenaEncriptada: String): String {
            val bytes =
                MessageDigest.getInstance("SHA-256").digest(contrasenaEncriptada.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }

        fun actualizarContrasena(nuevaContrasena: String) {
            CoroutineScope(Dispatchers.IO).launch {
                val objConexion = ClaseConexion().cadenaConexion()
                val correoRecu =
                    activity_correo_para_codigo.variablesGlobalesCorreoparacodigo.correoRecu
                val contrasenaEncriptacion = hashSHA256(nuevaContrasena)

                var verificarCorreo: PreparedStatement? = null
                var actualizarContrasena: PreparedStatement? = null
                var resultSet: ResultSet? = null

                try {
                    // Verifica si el correo existe
                    verificarCorreo = objConexion?.prepareStatement("SELECT * FROM Usuarios WHERE correo_electronico = ?")
                    verificarCorreo?.setString(1, correoRecu)
                    resultSet = verificarCorreo?.executeQuery()

                    if (resultSet != null && resultSet.next() && resultSet.getInt(1) > 0) {
                        actualizarContrasena = objConexion?.prepareStatement("UPDATE Usuarios SET contrasena = ? WHERE correo_electronico = ?")!!
                        actualizarContrasena?.setString(1, contrasenaEncriptacion)
                        actualizarContrasena?.setString(2, correoRecu)
                        val rowsUpdated = actualizarContrasena.executeUpdate()
                        if (rowsUpdated > 0) {
                            runOnUiThread {
                                Toast.makeText(this@activity_cambio_contrasena, "Contraseña actualizada exitosamente.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@activity_cambio_contrasena, "Error, el correo no existe.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread {
                        println("este es el error: $e")
                        Toast.makeText(this@activity_cambio_contrasena, "Error al actualizar la contraseña.", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }

        cambiarContrasena.setOnClickListener {
            if (nuevaContrasena.text.toString() == confirmarContrasena.text.toString()) {
                actualizarContrasena(nuevaContrasena.text.toString())
            } else {
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

    }
}