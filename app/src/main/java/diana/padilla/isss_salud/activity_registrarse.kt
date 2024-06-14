package diana.padilla.isss_salud

import Modelo.ClaseConexion
import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class activity_registrarse : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrarse)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtDUI = findViewById<EditText>(R.id.txtDUI)
        val txtSangre = findViewById<EditText>(R.id.txtSangre)
        val txtTelefono = findViewById<EditText>(R.id.txtTelefono)
        val txtCorreo1 = findViewById<EditText>(R.id.txtCorreo1)
        val txtContrasena1 = findViewById<EditText>(R.id.txtContrasena1)
        val btnCrearRegistro: Button = findViewById(R.id.btnCrearRegistro)

        btnCrearRegistro.setOnClickListener {

            val dui = txtDUI.text.toString()
            val tipoDeSangre = txtSangre.text.toString()
            val tel = txtTelefono.text.toString()
            val correo = txtCorreo1.text.toString()
            val contrasena = txtContrasena1.text.toString()
            val telefonoRegex = Regex("^\\d{8}\$")
            val correoPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()

            if (dui.isEmpty() || tipoDeSangre.isEmpty() || tel.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {

                Toast.makeText(
                    this,
                    "Error, para crear la cuenta debe llenar todas las casillas.",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!telefonoRegex.matches(tel)) {
                Toast.makeText(
                    this,
                    "Error, el número de teléfono debe contener 8 dígitos.",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!correoPattern.matches(correo)) {
                Toast.makeText(
                    this,
                    "Error, el correo electrónico no es válido.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Log.i("Test de credenciales", "DUI: $dui, tipo de sangre: $tipoDeSangre, Telefono: $tel,  Correo: $correo y Contraseña: $contrasena")
            }
        }

        btnCrearRegistro.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {

                val objConexion = ClaseConexion().cadenaConexion()

                val addUsuarios = objConexion?.prepareStatement("insert into Usuarios (uuid_usuario, dui, tipo_sangre, telefono, correo_electronico, contrasena) values (?, ?, ?, ?, ?, ?)")!!
                addUsuarios.setString(1, UUID.randomUUID().toString())
                addUsuarios.setString(2, txtDUI.text.toString())
                addUsuarios.setString(3, txtSangre.text.toString())
                addUsuarios.setString(4, txtTelefono.text.toString())
                addUsuarios.setString(5, txtCorreo1.text.toString())
                addUsuarios.setString(3, txtContrasena1.text.toString())

                addUsuarios.executeUpdate()

                withContext(Dispatchers.Main) {
                    AlertDialog.Builder(this@activity_registrarse)
                        .setTitle("Registro exitoso")
                        .setMessage("La cuenta se ha creado exitosamente.")
                        .setPositiveButton("Aceptar", null)
                        .show()
                        txtDUI.setText("")
                        txtSangre.setText("")
                        txtTelefono.setText("")
                        txtCorreo1.setText("")
                        txtContrasena1.setText("")
                    }

                    val logoISSS = findViewById<ImageView>(R.id.IvLogoIsss)
                    val modoOscuro = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

                    if(modoOscuro == Configuration.UI_MODE_NIGHT_YES){
                    logoISSS.setImageResource(R.drawable.ic_modo_oscuro_logo)
                    }else{
                    logoISSS.setImageResource(R.drawable.id_logo_isss)
                    }
            }
        }
    }
}