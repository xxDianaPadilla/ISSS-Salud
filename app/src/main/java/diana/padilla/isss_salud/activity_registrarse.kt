package diana.padilla.isss_salud

import Modelo.ClaseConexion
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.Configuration
import android.icu.util.Calendar
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
        val txtEdad = findViewById<EditText>(R.id.txtEdad)
        val txtNombre = findViewById<EditText>(R.id.txtNombreU)
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
        val spSexo = findViewById<Spinner>(R.id.spSexo)
        val listaTipoSangre = listOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")
        val listaSexo = listOf("Masculino", "Femenino")
        val adapterTipoSangre =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaTipoSangre)
        spTipoSangre.adapter = adapterTipoSangre
        val adapterSexo =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaSexo)
        spSexo.adapter = adapterSexo

        txtEdad.setOnClickListener{
            val calendario = Calendar.getInstance()
            val anio = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)

            val calendarioMayorEdad = Calendar.getInstance()
            calendarioMayorEdad.set(anio - 18, mes, dia)

            val datePickerDialog = DatePickerDialog(
                this,
                { view, anioSeleccionado, mesSeleccionado, diaSeleccionado ->
                    val calendarioSeleccionado = Calendar.getInstance()
                    calendarioSeleccionado.set(anioSeleccionado, mesSeleccionado, diaSeleccionado)

                    if(calendarioSeleccionado.after(calendarioMayorEdad)){
                        AlertDialog.Builder(this)
                            .setTitle("Fecha inválida")
                            .setMessage("Debe seleccionar una fecha que demuestre que es mayor de edad")
                            .setPositiveButton("OK"){ dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                        txtEdad.setText("");
                    }else{
                        val fechaSeleccionada = "$diaSeleccionado/${mesSeleccionado + 1}/$anioSeleccionado"
                        txtEdad.setText(fechaSeleccionada)
                    }
                },
                anio, mes, dia
            )

            datePickerDialog.datePicker.maxDate = calendario.timeInMillis

            datePickerDialog.show()
        }


        fun hashSHA256(contrasenaEncriptada: String): String {
            val bytes =
                MessageDigest.getInstance("SHA-256").digest(contrasenaEncriptada.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }

        btnCrearRegistro.setOnClickListener {

            val dui = txtDUI.text.toString()
            val edad = txtEdad.text.toString()
            val tel = txtTelefono.text.toString()
            val correo = txtCorreo1.text.toString().trim()
            val contrasena = txtContrasena1.text.toString()
            val nombre = txtNombre.text.toString()
            val duiRegex = Regex("^\\d{8}-\\d\$")
            val telefonoRegex = Regex("^\\d{4}-\\d{4}\$")
            val correoPattern = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
            val contrasenaRegex = Regex("^(?=.*[0-9!@#\$%^&*()-_=+\\|\\[{\\]};:'\",<.>/?]).{6,}\$")
            val nombreRegex = Regex("^[\\p{L}\\s.,;:!?áéíóúÁÉÍÓÚñÑ]+$")

            var valid = true

            if (dui.isEmpty()) {
                txtDUI.setError("El DUI no puede estar vacío")
                valid = false
            } else if (!duiRegex.matches(dui)) {
                txtDUI.setError("Error, el DUI no es valido. Debe tener el formato adecuado, por ejemplo, 12345678-9.")
                valid = false
            }

            if(nombre.isEmpty()){
                txtNombre.setError("El nombre de usuario no puede estar vacío")
                valid = false
            }else if(!nombreRegex.matches(nombre)){
                txtNombre.setError("Ingrese un nombre válido")
                valid = false
            }

            if(edad.isEmpty()){
                AlertDialog.Builder(this)
                    .setTitle("Campo vacío")
                    .setMessage("Debe seleccionar una fecha de nacimiento para poder registrarse")
                    .setPositiveButton("OK"){ dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
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
                                        objConexion?.prepareStatement("insert into Usuarios (dui, tipo_sangre, telefono, correo_electronico, contrasena, sexo, edad, nombre_usuario) values (?, ?, ?, ?, ?, ?, ?, ?)")!!
                                    addUsuarios.setString(1, txtDUI.text.toString())
                                    addUsuarios.setString(2, spTipoSangre.selectedItem.toString())
                                    addUsuarios.setString(3, txtTelefono.text.toString())
                                    addUsuarios.setString(4, txtCorreo1.text.toString())
                                    addUsuarios.setString(5, contrasenaEncriptacion)
                                    addUsuarios.setString(6, spSexo.selectedItem.toString())
                                    addUsuarios.setString(7, txtEdad.text.toString())
                                    addUsuarios.setString(8, txtNombre.text.toString())

                                    addUsuarios.executeUpdate()

                                    withContext(Dispatchers.Main) {
                                        AlertDialog.Builder(this@activity_registrarse)
                                            .setTitle("Registro exitoso")
                                            .setMessage("La cuenta se ha creado exitosamente.")
                                            .setPositiveButton("Aceptar", null)
                                            .show()
                                        txtNombre.setText("")
                                        txtDUI.setText("")
                                        txtTelefono.setText("")
                                        txtCorreo1.setText("")
                                        txtEdad.setText("")
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
