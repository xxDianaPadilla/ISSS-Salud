package diana.padilla.isss_salud

import Modelo.ClaseConexion
import Modelo.Perfil
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class activity_editar_perfil : AppCompatActivity() {

    lateinit var imagenPerfil2: ImageView
    lateinit var txtCorrePerfil: EditText
    lateinit var txtViewTelefonoPerfil: EditText
    lateinit var txtViewDuiPerfil: EditText
    lateinit var txtViewTipoSangre: EditText

    val codigo_opcion_galeria = 102
    val STORAGE_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_editar_perfil)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cargarDatosPerfil2EnPantalla()

        imagenPerfil2 = findViewById<ImageView>(R.id.ImgPerfilSinCargar2)
        txtCorrePerfil = findViewById<EditText>(R.id.txtCorrePerfil)
        txtViewTelefonoPerfil = findViewById<EditText>(R.id.txtViewTelefonoPerfil)
        txtViewDuiPerfil = findViewById<EditText>(R.id.txtViewDuiPerfil)
        txtViewTipoSangre = findViewById<EditText>(R.id.txtViewTipoSangre)
        val btnCargarImagen = findViewById<Button>(R.id.btnCargarImagen)
        val btnActualizar = findViewById<Button>(R.id.btnActualizar)

        btnCargarImagen.setOnClickListener {
            checkStoragePermissions()
        }

        btnActualizar.setOnClickListener {
            val nuevoTelefono = txtViewTelefonoPerfil.text.toString()
            val telefonoRegex = Regex("^\\d{4}-\\d{4}\$")
            if(nuevoTelefono.isEmpty()){
                txtViewTelefonoPerfil.setError("El teléfono no puede estar vacío")
            }else if (!telefonoRegex.matches(nuevoTelefono)) {
                txtViewTelefonoPerfil.setError("Error, el número de teléfono no es válido. Debe tener el formato adecuado, por ejemplo, 1234-5678.")
            }else{
                verificarTelefonoEnBaseDeDatos(nuevoTelefono) {telefonoExiste ->
                    if(telefonoExiste){
                        runOnUiThread {
                            val dialogBuilder = AlertDialog.Builder(this)
                            dialogBuilder.setMessage("El número de teléfono ya está registrado. Por favor, ingrese otro número.")
                                .setCancelable(false)
                                .setPositiveButton("OK") {dialog, _-> dialog.dismiss()}
                            val alert = dialogBuilder.create()
                            alert.setTitle("Teléfono inválido")
                            alert.show()
                        }
                    }else{
                        ActualizarTelefonoEnBaseDeDatos(nuevoTelefono)

                        runOnUiThread {
                            Toast.makeText(
                                this@activity_editar_perfil,
                                "El teléfono se ha actualizado exitosamente.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        txtViewTelefonoPerfil.setText("")
                        txtViewTelefonoPerfil.hint = nuevoTelefono
                        val intent = Intent()
                        intent.putExtra("nuevoTelefono", nuevoTelefono)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                }
            }
        }

        val logoIsssSmall = findViewById<ImageView>(R.id.ivSmallLogo)
        val modoOscuro = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(modoOscuro == Configuration.UI_MODE_NIGHT_YES){
            logoIsssSmall.setImageResource(R.drawable.ic_logo_isss_dark)
        }else{
            logoIsssSmall.setImageResource(R.drawable.ic_logo_isss_small)
        }

        val iconHome = findViewById<ImageView>(R.id.btnNoticias)
        val modoOscuro3 = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(modoOscuro3 == Configuration.UI_MODE_NIGHT_YES){
            iconHome.setImageResource(R.drawable.ic_home_dm)
        }else{
            iconHome.setImageResource(R.drawable.ic_home)
        }

        val iconCitas = findViewById<ImageView>(R.id.btnCitas)
        val modoOscuro4 = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(modoOscuro4 == Configuration.UI_MODE_NIGHT_YES){
            iconCitas.setImageResource(R.drawable.ic_citas_dm)
        }else{
            iconCitas.setImageResource(R.drawable.ic_citas)
        }

        val iconChats = findViewById<ImageView>(R.id.BtnChats)
        val modoOscuro5 = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(modoOscuro5 == Configuration.UI_MODE_NIGHT_YES){
            iconChats.setImageResource(R.drawable.ic_chats_dm)
        }else{
            iconChats.setImageResource(R.drawable.ic_chat)
        }

        val iconPerfil = findViewById<ImageView>(R.id.btnPerfil)
        val modoOscuro6 = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(modoOscuro6 == Configuration.UI_MODE_NIGHT_YES){
            iconPerfil.setImageResource(R.drawable.ic_active_profile_dm)
        }else{
            iconPerfil.setImageResource(R.drawable.ic_active_profile)
        }

        iconHome.setOnClickListener{
            val pantallaNoticias = Intent(this, activity_noticias::class.java)
            startActivity(pantallaNoticias)
            overridePendingTransition(0, 0)
        }

        iconCitas.setOnClickListener {
            val pantallaCitas = Intent(this, activity_citas_medicas::class.java)
            startActivity(pantallaCitas)
            overridePendingTransition(0, 0)
        }

        iconChats.setOnClickListener {
            val pantallaMensajeria = Intent(this, activity_mensajeria::class.java)
            startActivity(pantallaMensajeria)
            overridePendingTransition(0, 0)
        }

    }

    private fun checkStoragePermissions(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestStoragePermissions()
        }else{
            openGallery()
        }
    }

        private fun requestStoragePermissions() {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_REQUEST_CODE)
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                Toast.makeText(this, "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, codigo_opcion_galeria)
    }

         override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == codigo_opcion_galeria && resultCode == Activity.RESULT_OK) {
                val selectedImageUri = data?.data
                if(selectedImageUri != null){
                    val imagenPerfil2 = findViewById<ImageView>(R.id.ImgPerfilSinCargar2)
                    Glide.with(this).load(selectedImageUri).into(imagenPerfil2)

                    val correoDeLaVariableGlobal = activity_ingreso.variablesGlobales.miMorreo
                    updateImageUrlInDatabase(correoDeLaVariableGlobal, selectedImageUri.toString())
                }

            }
        }

    private fun ActualizarTelefonoEnBaseDeDatos(nuevoTelefono: String){
        CoroutineScope(Dispatchers.IO).launch {
            val objConexion = ClaseConexion().cadenaConexion()
            val correoDeLaVariableGlobal = activity_ingreso.variablesGlobales.miMorreo

            val updateTelefono = objConexion?.prepareStatement("UPDATE Usuarios SET telefono = ? WHERE correo_electronico = ?")
            updateTelefono?.setString(1, nuevoTelefono)
            updateTelefono?.setString(2, correoDeLaVariableGlobal)
            updateTelefono?.executeUpdate()

            /*runOnUiThread {
                Toast.makeText(this@activity_editar_perfil, "El teléfono se ha actualizado exitosamente.", Toast.LENGTH_SHORT).show()
            }*/
        }
    }

    private fun updateImageUrlInDatabase(correo: String, imageUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val objConexion = ClaseConexion().cadenaConexion()

            val updateProfileImage =
                objConexion?.prepareStatement("UPDATE Usuarios SET foto_usuario = ? WHERE correo_electronico = ?")
            updateProfileImage?.setString(1, imageUrl)
            updateProfileImage?.setString(2, correo)
            updateProfileImage?.executeUpdate()

            Toast.makeText(this@activity_editar_perfil, "Foto de perfil actualizado exitosamente!", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun datosPerfil(correoDeLaVariableGlobal: String): List<Perfil> {
        val perfil2 = mutableListOf<Perfil>()

        withContext(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()
            val obtenerPerfil2 =
                objConexion?.prepareStatement("SELECT foto_usuario, dui, correo_electronico, telefono, tipo_sangre FROM Usuarios WHERE correo_electronico = ?")!!
            obtenerPerfil2.setString(1, correoDeLaVariableGlobal)
            val resultSet = obtenerPerfil2.executeQuery()

            while (resultSet.next()) {
                var foto_usuario2: String = resultSet.getString("foto_usuario")
                var correo_electronico2: String = resultSet.getString("correo_electronico")
                var telefono2: String = resultSet.getString("telefono")
                var dui2: String = resultSet.getString("dui")
                var tipo_sangre2: String = resultSet.getString("tipo_sangre")

                val perfilCompleto2 =
                    Perfil(foto_usuario2, correo_electronico2, telefono2, dui2, tipo_sangre2)
                println(perfilCompleto2)
                perfil2.add(perfilCompleto2)
            }
        }

        return perfil2
    }

    fun cargarDatosPerfil2EnPantalla() {
        CoroutineScope(Dispatchers.Main).launch {
            val correoDeLaVariableGlobal = activity_ingreso.variablesGlobales.miMorreo
            val perfiles2 = datosPerfil(correoDeLaVariableGlobal)

            if (perfiles2.isNotEmpty()) {
                val miFoto2 = perfiles2[0].foto_usuario
                Glide.with(this@activity_editar_perfil)
                    .load(miFoto2)
                    .into(imagenPerfil2)
                val miDui2 = perfiles2[0].dui
                txtViewDuiPerfil.hint = miDui2
                val miCorreo2 = perfiles2[0].correo_electronico
                txtCorrePerfil.hint = miCorreo2
                val miTelefono2 = perfiles2[0].telefono
                txtViewTelefonoPerfil.hint = miTelefono2
                val miTipoSangre2 = perfiles2[0].tipo_sangre
                txtViewTipoSangre.hint = miTipoSangre2
            }
        }
    }

    private fun verificarTelefonoEnBaseDeDatos(nuevoTelefono: String, callback: (Boolean) -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            val objConexion = ClaseConexion().cadenaConexion()
            val consultaTelefono = objConexion?.prepareStatement("SELECT COUNT(*) FROM Usuarios WHERE telefono = ?")!!
            consultaTelefono.setString(1, nuevoTelefono)

            val resultado = consultaTelefono.executeQuery()
            if(resultado != null && resultado.next()){
                val telefonoExiste = resultado.getInt(1) > 0
                callback(telefonoExiste)
            }else{
                callback(false)
            }
        }
    }
}