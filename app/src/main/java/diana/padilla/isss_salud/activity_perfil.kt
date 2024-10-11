package diana.padilla.isss_salud

import Modelo.ClaseConexion
import Modelo.Perfil
import Modelo.ReportGenerator
import Modelo.openGeneratedPDF
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class activity_perfil : AppCompatActivity() {

    lateinit var imagenPerfil: ImageView
    lateinit var correoPerfilE: EditText
    lateinit var telefonoPerfilE: EditText
    lateinit var duiPerfilE: EditText
    lateinit var tipoSangreE: EditText
    lateinit var descargarPDF: Button
    lateinit var etDUI: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        descargarPDF = findViewById(R.id.btnDescargarExpediente)
        etDUI = findViewById(R.id.etDUI)

        try {
            descargarPDF.setOnClickListener {
                val dui = etDUI.text.toString()

                // Generar el reporte
                val reportGenerator = ReportGenerator(this)
                val generatedFile: File? = reportGenerator.generateReport(dui)

                // Abrir el archivo PDF generado
                generatedFile?.let {
                    openGeneratedPDF(this, it)
                }
            }
        }catch (e: Exception){
            println(e)
        }

        cargarDatosPerfilEnPantalla()


        imagenPerfil = findViewById<ImageView>(R.id.ImgPerfilSinCargar)
        correoPerfilE = findViewById<EditText>(R.id.txtViewCorreoPerfilE)
        telefonoPerfilE = findViewById<EditText>(R.id.txtViewTelefonoPerfilE)
        duiPerfilE = findViewById<EditText>(R.id.txtViewDuiPerfilE)
        tipoSangreE = findViewById<EditText>(R.id.txtViewTipoSangreE)


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

        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)

        btnCerrarSesion.setOnClickListener {
            val pantallaIngreso = Intent(this, activity_ingreso::class.java)
            startActivity(pantallaIngreso)
            finish()
        }

        val btnEditarPerfil = findViewById<Button>(R.id.btnCargarImagen)

        btnEditarPerfil.setOnClickListener {
            val pantallaEditarPerfil = Intent(this, activity_editar_perfil::class.java)
            startActivity(pantallaEditarPerfil)
            overridePendingTransition(0,0)
        }
    }

    suspend fun obtenerDatosPerfil(correoDeLaVariableGlobal: String): List<Perfil>{
        val perfil = mutableListOf<Perfil>()

        withContext(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()
            val obtenerPerfil =
                objConexion?.prepareStatement("SELECT foto_usuario, dui, correo_electronico, telefono, tipo_sangre FROM Usuarios WHERE correo_electronico = ?")!!
            obtenerPerfil.setString(1, correoDeLaVariableGlobal)
            val resultSet = obtenerPerfil.executeQuery()

            while (resultSet.next()){
                val foto_usuario: String = resultSet.getString("foto_usuario")
                val correo_electronico: String = resultSet.getString("correo_electronico")
                val telefono: String = resultSet.getString("telefono")
                val dui: String = resultSet.getString("dui")
                val tipo_sangre: String = resultSet.getString("tipo_sangre")

                val perfilCompleto = Perfil(foto_usuario, correo_electronico, telefono, dui, tipo_sangre)
                println(perfilCompleto)
                perfil.add(perfilCompleto)
            }
        }

        return perfil
    }

    fun cargarDatosPerfilEnPantalla() {
        CoroutineScope(Dispatchers.Main).launch {
            val correoDeLaVariableGlobal = activity_ingreso.variablesGlobales.miMorreo
            val perfiles = obtenerDatosPerfil(correoDeLaVariableGlobal)

            if (perfiles.isNotEmpty()) {
                val miFoto = perfiles[0].foto_usuario
                Glide.with(this@activity_perfil)
                    .load(miFoto)
                    .into(imagenPerfil)
                val miDui = perfiles[0].dui
                duiPerfilE.hint = miDui
                val miCorreo = perfiles[0].correo_electronico
                correoPerfilE.hint = miCorreo
                val miTelefono = perfiles[0].telefono
                telefonoPerfilE.hint = miTelefono
                val miTipoSangre = perfiles[0].tipo_sangre
                tipoSangreE.hint = miTipoSangre
            }
        }
    }
}