package diana.padilla.isss_salud

import Modelo.ClaseConexion
import Modelo.Usuarios
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
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
import java.sql.ResultSet

class activity_citas_medicas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_citas_medicas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dui = findViewById<EditText>(R.id.txtDuiSolicitud)
        val correo = findViewById<EditText>(R.id.txtCorreoSolicitud)
        val telefono = findViewById<EditText>(R.id.txtTelefonoSolicitud)
        val tipoSangre = findViewById<EditText>(R.id.txtTipoSangreSolicitud)

        fun obtenerCosas(): List<Usuarios> {
            val usuario = mutableListOf<Usuarios>()

            CoroutineScope(Dispatchers.IO).launch {
                val objConexion = ClaseConexion().cadenaConexion()
                val correoDeLaVariableGlobal = activity_ingreso.variablesGlobales.miMorreo
                val obtenerUsuario =
                    objConexion?.prepareStatement("SELECT dui, correo_electronico, telefono, tipo_sangre FROM Usuarios WHERE correo_electronico = ?")!!
                obtenerUsuario.setString(1, correoDeLaVariableGlobal)
                val resultSet = obtenerUsuario.executeQuery()

                while (resultSet.next()) {
                    var dui: String = resultSet.getString("dui")
                    var correo_electronico: String = resultSet.getString("correo_electronico")
                    var telefono: String = resultSet.getString("telefono")
                    var tipo_sangre: String = resultSet.getString("tipo_sangre")

                    val usuarioCompleto = Usuarios(dui, correo_electronico, telefono, tipo_sangre)
                    usuario.add(usuarioCompleto)
                }
            }
            return usuario
        }

        CoroutineScope(Dispatchers.Main).launch {
            val usuarios = withContext(Dispatchers.IO) { obtenerCosas() }
            if (usuarios.isNotEmpty()) {
                val miDui = usuarios[0].dui
                dui.hint = miDui


            }
        }


        val logoIsssSmall = findViewById<ImageView>(R.id.ivSmallLogo)
        val modoOscuro = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (modoOscuro == Configuration.UI_MODE_NIGHT_YES) {
            logoIsssSmall.setImageResource(R.drawable.ic_logo_isss_dark)
        } else {
            logoIsssSmall.setImageResource(R.drawable.ic_logo_isss_small)
        }

        val iconHome = findViewById<ImageView>(R.id.ivHome)
        val modoOscuro3 = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (modoOscuro3 == Configuration.UI_MODE_NIGHT_YES) {
            iconHome.setImageResource(R.drawable.ic_home_dm)
        } else {
            iconHome.setImageResource(R.drawable.ic_home)
        }

        val iconCitas = findViewById<ImageView>(R.id.ivCitas)
        val modoOscuro4 = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (modoOscuro4 == Configuration.UI_MODE_NIGHT_YES) {
            iconCitas.setImageResource(R.drawable.ic_file_menu_dm_24)
        } else {
            iconCitas.setImageResource(R.drawable.ic_active_file)
        }

        val iconChats = findViewById<ImageView>(R.id.ivChats)
        val modoOscuro5 = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (modoOscuro5 == Configuration.UI_MODE_NIGHT_YES) {
            iconChats.setImageResource(R.drawable.ic_chats_dm)
        } else {
            iconChats.setImageResource(R.drawable.ic_chat)
        }

        val iconPerfil = findViewById<ImageView>(R.id.ivPerfil)
        val modoOscuro6 = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (modoOscuro6 == Configuration.UI_MODE_NIGHT_YES) {
            iconPerfil.setImageResource(R.drawable.ic_perfil_dm)
        } else {
            iconPerfil.setImageResource(R.drawable.ic_perfil)
        }

        iconHome.setOnClickListener {
            val pantallaNoticias = Intent(this, activity_noticias::class.java)
            startActivity(pantallaNoticias)
        }

        iconChats.setOnClickListener {
            val pantallaMensajeria = Intent(this, activity_mensajeria::class.java)
            startActivity(pantallaMensajeria)
        }

        iconPerfil.setOnClickListener {
            val pantallaPerfil = Intent(this, activity_perfil::class.java)
            startActivity(pantallaPerfil)
        }

        val btnCitasAgendadas = findViewById<Button>(R.id.btnCitasAgendadas)

        btnCitasAgendadas.setOnClickListener {
            val pantallaCitasAgendadas = Intent(this, activity_citas_agendadas::class.java)
            startActivity(pantallaCitasAgendadas)
        }
    }
}