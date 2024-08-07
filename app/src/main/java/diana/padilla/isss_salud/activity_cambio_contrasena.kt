package diana.padilla.isss_salud

import Modelo.ClaseConexion
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
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
        val contrasenaRegex = Regex("^(?=.*[0-9!@#\$%^&*()-_=+\\|\\[{\\]};:'\",<.>/?]).{6,}\$")

        var isPasswordVisible = false
        val passwordview = nuevaContrasena
        val passwordview2 = confirmarContrasena
        val togglePasswordVisibility2 = findViewById<ImageView>(R.id.togglePasswordVisibility2)
        val togglePasswordVisibility = findViewById<ImageView>(R.id.togglePasswordVisibility)

        togglePasswordVisibility2.setOnClickListener {
            if (isPasswordVisible) {
                // Ocultar contraseña
                passwordview2.transformationMethod = PasswordTransformationMethod.getInstance()
                togglePasswordVisibility.setImageResource(R.drawable.ic_visibility_off)
            } else {
                // Mostrar contraseña
                passwordview2.transformationMethod = HideReturnsTransformationMethod.getInstance()
                togglePasswordVisibility.setImageResource(R.drawable.ic_visibility)
            }
            isPasswordVisible = !isPasswordVisible
            passwordview.setSelection(passwordview.text.length)
        }

        togglePasswordVisibility.setOnClickListener {
            if (isPasswordVisible) {
                // Ocultar contraseña
                passwordview.transformationMethod = PasswordTransformationMethod.getInstance()
                togglePasswordVisibility.setImageResource(R.drawable.ic_visibility_off)
            } else {
                // Mostrar contraseña
                passwordview.transformationMethod = HideReturnsTransformationMethod.getInstance()
                togglePasswordVisibility.setImageResource(R.drawable.ic_visibility)
            }
            isPasswordVisible = !isPasswordVisible
            passwordview.setSelection(passwordview.text.length)
        }

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
                            val pantallaNuevaContrasena = Intent(this@activity_cambio_contrasena, activity_nueva_contrasena::class.java)
                            startActivity(pantallaNuevaContrasena)
                            finish()
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
                        Toast.makeText(this@activity_cambio_contrasena, "Error al actualizar la contraseña, prueba con otra contraseña", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }

        cambiarContrasena.setOnClickListener {

            val password = nuevaContrasena.text.toString()
            val confirmPassword = confirmarContrasena.text.toString()

            if (password.isEmpty() || confirmPassword.isEmpty()){
                Toast.makeText(
                    this@activity_cambio_contrasena,
                    "Error, para cambiar la contraseña debes llenar todas las casillas.",
                    Toast.LENGTH_SHORT
                ).show()
            } else if(!contrasenaRegex.matches(password)){
                Toast.makeText(
                    this@activity_cambio_contrasena,
                    "Error, la contraseña debe contener al menos un caracter especial y tener más de 6 caracteres.",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (nuevaContrasena.text.toString() == confirmarContrasena.text.toString()) {
                actualizarContrasena(nuevaContrasena.text.toString())
            }else {
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