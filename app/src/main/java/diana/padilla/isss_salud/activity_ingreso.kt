package diana.padilla.isss_salud

import Modelo.ClaseConexion
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Application
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
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

companion object variablesGlobales{
     lateinit var miMorreo: String
     var idUsuarioGlobal: Int = 0
}

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

        val logoISSS = findViewById<ImageView>(R.id.IvLogoIsss)
        val modoOscuro = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (modoOscuro == Configuration.UI_MODE_NIGHT_YES) {
            logoISSS.setImageResource(R.drawable.ic_modo_oscuro_logo)
        } else {
            logoISSS.setImageResource(R.drawable.id_logo_isss)
        }

        val txtOlvidoContrasena = findViewById<TextView>(R.id.btnCambioContraseñaIngreso)

        txtOlvidoContrasena.setOnClickListener {
            val pantallaOlvidoContrasena = Intent(this, activity_correo_para_codigo::class.java)
            startActivity(pantallaOlvidoContrasena)
            finish()
        }

        val btnRegistrarse = findViewById<Button>(R.id.btnRegistrarseIngresoInterfaz)

        btnRegistrarse.setOnClickListener{
            val pantallaRegistrarse = Intent(this, activity_registrarse::class.java)
            startActivity(pantallaRegistrarse)
            overridePendingTransition(0, 0)
        }

        val txtCorreo = findViewById<EditText>(R.id.txtCorreoIngreso)
        val txtContrasena = findViewById<EditText>(R.id.txtContrasenaIngreso)
        val btnLogin = findViewById<Button>(R.id.btnIniciarSesionIngreso)
        val correoPattern = Regex ("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")

        var isPasswordVisible = false
        val passwordview = txtContrasena
        val togglePasswordVisibility = findViewById<ImageView>(R.id.togglePasswordVisibility)

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

        fun hashSHA256(contrasenaEncriptada: String): String{
            val bytes = MessageDigest.getInstance("SHA-256").digest(contrasenaEncriptada.toByteArray())
            return bytes.joinToString(""){ "%02x".format(it) }
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

                CoroutineScope(Dispatchers.IO).launch {

                    val objConexion = ClaseConexion().cadenaConexion()

                    val contrasenaEncriptacion = hashSHA256(txtContrasena.text.toString())

                    val comprobarUsuario = objConexion?.prepareStatement("SELECT id_usuario, id_rol FROM Usuarios WHERE correo_electronico = ? AND contrasena = ?")!!
                    comprobarUsuario.setString(1, txtCorreo.text.toString())
                    comprobarUsuario.setString(2, contrasenaEncriptacion)
                    val resultado = comprobarUsuario.executeQuery()

                    if (resultado.next()) {
                        val idRol = resultado.getInt("id_rol")
                        val idUsuario = resultado.getInt("id_usuario")
                        if(idRol == 2){
                            val pantallaNoticias = Intent(this@activity_ingreso, activity_noticias::class.java)
                            pantallaNoticias.putExtra("correoIng", txtCorreo.text.toString())
                            miMorreo = txtCorreo.text.toString()
                            variablesGlobales.idUsuarioGlobal = idUsuario

                            runOnUiThread{
                                Toast.makeText(this@activity_ingreso, "Bienvenid@!", Toast.LENGTH_SHORT).show()
                            }

                            startActivity(pantallaNoticias)
                            finish()
                        }else{
                            runOnUiThread{
                                val alertDialog = AlertDialog.Builder(this@activity_ingreso).apply {
                                    setTitle("Acceso denegado")
                                    setMessage("Credenciales inválidas, aplicación exclusiva para usuarios pacientes del ISSS")
                                    setPositiveButton("Aceptar"){ dialog, _ ->
                                        dialog.dismiss()
                                    }
                                }.create()
                                alertDialog.show()

                                txtCorreo.setText("");
                                txtContrasena.setText("");
                            }
                        }

                    } else {
                        runOnUiThread{
                            Toast.makeText(this@activity_ingreso, "Usuario no encontrado, verifique las credenciales", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this@activity_ingreso, "Has salido de la aplicación", Toast.LENGTH_SHORT).show()
        finishAffinity()
    }
}