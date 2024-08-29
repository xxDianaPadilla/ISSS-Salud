package diana.padilla.isss_salud

import Modelo.ClaseConexion
import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.MessageDigest

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

        val btnIniciarSesion = findViewById<Button>(R.id.btnIniciarSesionIngresoInterfaz)

        btnIniciarSesion.setOnClickListener {
            val pantallaIniciarSesion = Intent(this, activity_ingreso::class.java)
            startActivity(pantallaIniciarSesion)
            overridePendingTransition(0, 0)
        }

        val logoISSS = findViewById<ImageView>(R.id.IvLogoIsss)
        val modoOscuro = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (modoOscuro == Configuration.UI_MODE_NIGHT_YES) {
            logoISSS.setImageResource(R.drawable.ic_modo_oscuro_logo)
        } else {
            logoISSS.setImageResource(R.drawable.id_logo_isss)
        }

        val txtDUI = findViewById<EditText>(R.id.txtDUI)
        val txtTelefono = findViewById<EditText>(R.id.txtTelefono)
        val txtCorreo1 = findViewById<EditText>(R.id.txtCorreoRegistro)
        val txtContrasena1 = findViewById<EditText>(R.id.txtContrasena1)
        val btnCrearRegistro: Button = findViewById(R.id.btnCrearRegistro)

        var isPasswordVisible = false
        val passwordview = txtContrasena1
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

        val spTipoSangre = findViewById<Spinner>(R.id.spTipoSangre)
        val listaTipoSangre = listOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")
        val adapterTipoSangre =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaTipoSangre)
        spTipoSangre.adapter = adapterTipoSangre



        fun hashSHA256(contrasenaEncriptada: String): String {
            val bytes =
                MessageDigest.getInstance("SHA-256").digest(contrasenaEncriptada.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }

        btnCrearRegistro.setOnClickListener {

            val dui = txtDUI.text.toString()
            val tel = txtTelefono.text.toString()
            val correo = txtCorreo1.text.toString().trim()
            val contrasena = txtContrasena1.text.toString()
            val duiRegex = Regex("^\\d{8}-\\d\$")
            val telefonoRegex = Regex("^\\d{4}-\\d{4}\$")
            val correoPattern = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
            val contrasenaRegex = Regex("^(?=.*[0-9!@#\$%^&*()-_=+\\|\\[{\\]};:'\",<.>/?]).{6,}\$")

            var valid = true

            if (dui.isEmpty()) {
                txtDUI.setError("El DUI no puede estar vacío")
                valid = false
            } else if (!duiRegex.matches(dui)) {
                txtDUI.setError("Error, el DUI no es valido. Debe tener el formato adecuado, por ejemplo, 12345678-9.")
                valid = false
            }

            if (tel.isEmpty()) {
                txtTelefono.setError("El teléfono no puede estar vacío")
                valid = false
            } else if (!telefonoRegex.matches(tel)) {
                txtTelefono.setError("Error, el número de teléfono no es válido. Debe tener el formato adecuado, por ejemplo, 1234-5678.")
                valid = false
            }

            if (correo.isEmpty()) {
                txtCorreo1.setError("El correo no puede estar vacío")
                valid = false
            } else if (!correoPattern.matches(correo)) {
                txtCorreo1.setError("Error, el correo eléctronico no es válido. Debe contener el formato adecuado, por ejemplo, ejemplo@gmail.com.")
                valid = false
            }

            if (contrasena.isEmpty()) {
                txtContrasena1.setError("La contraseña no puede estar vacía")
                valid = false
            } else if (!contrasenaRegex.matches(contrasena)) {
                txtContrasena1.setError("Error la contraseña debe contener al menos un caracter especial y tener más de 6 caracteres.")
                valid = false
            }

            if (valid) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val objConexion = ClaseConexion().cadenaConexion()

                        val checkDuplicados =
                            objConexion?.prepareStatement("SELECT * FROM Usuarios WHERE dui = ? OR telefono = ?")
                        checkDuplicados?.setString(1, dui)
                        checkDuplicados?.setString(2, tel)
                        val resultSet = checkDuplicados?.executeQuery()

                        if (resultSet != null && resultSet.next()) {
                            withContext(Dispatchers.Main) {
                                val duiExistente = resultSet.getString("dui")
                                val telefonoExistente = resultSet.getString("telefono")
                                if (duiExistente == dui) {
                                    AlertDialog.Builder(this@activity_registrarse)
                                        .setTitle("Error de registro")
                                        .setMessage("El DUI ya existe. Por favor, ingrese otro.")
                                        .setPositiveButton("Aceptar", null)
                                        .show()
                                }
                                if (telefonoExistente == tel) {
                                    AlertDialog.Builder(this@activity_registrarse)
                                        .setTitle("Error de registro")
                                        .setMessage("El teléfono ya ha sido empleado. Por favor, elige otro.")
                                        .setPositiveButton("Aceptar", null)
                                        .show()
                                }
                            }
                        } else {
                            CoroutineScope(Dispatchers.IO).launch {

                                try {

                                    val objConexion = ClaseConexion().cadenaConexion()

                                    val contrasenaEncriptacion =
                                        hashSHA256(txtContrasena1.text.toString())

                                    val addUsuarios =
                                        objConexion?.prepareStatement("insert into Usuarios (dui, tipo_sangre, telefono, correo_electronico, contrasena) values (?, ?, ?, ?, ?)")!!
                                    addUsuarios.setString(1, txtDUI.text.toString())
                                    addUsuarios.setString(2, spTipoSangre.selectedItem.toString())
                                    addUsuarios.setString(3, txtTelefono.text.toString())
                                    addUsuarios.setString(4, txtCorreo1.text.toString())
                                    addUsuarios.setString(5, contrasenaEncriptacion)

                                    addUsuarios.executeUpdate()

                                    withContext(Dispatchers.Main) {
                                        AlertDialog.Builder(this@activity_registrarse)
                                            .setTitle("Registro exitoso")
                                            .setMessage("La cuenta se ha creado exitosamente.")
                                            .setPositiveButton("Aceptar", null)
                                            .show()
                                        txtDUI.setText("")
                                        txtTelefono.setText("")
                                        txtCorreo1.setText("")
                                        txtContrasena1.setText("")
                                    }
                                } catch (e: java.sql.SQLIntegrityConstraintViolationException) {
                                    withContext(Dispatchers.Main) {
                                        AlertDialog.Builder(this@activity_registrarse)
                                            .setTitle("Error de registro")
                                            .setMessage("El correo electronico ya ha sido empleada. Por favor, elige otro.")
                                            .setPositiveButton("Aceptar", null)
                                            .show()
                                    }
                                }
                            }
                        }
                    }catch (e: Exception){
                        println(e)
                    }
                }
            }
        }
    }
}
