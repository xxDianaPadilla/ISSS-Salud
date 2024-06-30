package diana.padilla.isss_salud

import Modelo.CitasAgendadas
import Modelo.ClaseConexion
import Modelo.NoticiasNuevas
import RecyclerViewHelpers.AdaptadorAgendadas
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class activity_citas_agendadas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_citas_agendadas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView: RecyclerView = findViewById(R.id.rcvPacientes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fun obtenerCitasAgendadas(): List<CitasAgendadas>{
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM CitasMedicas")!!

            val listaCitasAgendadas = mutableListOf<CitasAgendadas>()

            while (resultSet.next()){
                val id_cita = resultSet.getInt("id_cita")
                val fecha_cita = resultSet.getDate("fecha_cita")
                val hora_cita = resultSet.getString("hora_cita")
                val id_usuario = resultSet.getInt("id_usuario")
                val id_doctor = resultSet.getInt("id_doctor")

                val valoresJuntos = CitasAgendadas(id_cita, fecha_cita.toString(), hora_cita, id_usuario.toString(), id_doctor.toString())

                listaCitasAgendadas.add(valoresJuntos)
            }

            return listaCitasAgendadas
        }

        CoroutineScope(Dispatchers.IO).launch {
            val citasAgendadas = obtenerCitasAgendadas()
            withContext(Dispatchers.Main){
                val adapter = AdaptadorAgendadas(citasAgendadas)
                recyclerView.adapter = adapter
            }
        }


        val logoIsssSmall = findViewById<ImageView>(R.id.ivSmallLogo)
        val modoOscuro = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(modoOscuro == Configuration.UI_MODE_NIGHT_YES){
            logoIsssSmall.setImageResource(R.drawable.ic_logo_isss_dark)
        }else{
            logoIsssSmall.setImageResource(R.drawable.ic_logo_isss_small)
        }

        val iconNotificaciones = findViewById<ImageView>(R.id.ivNotificaciones)
        val modoOscuro2 = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(modoOscuro2 == Configuration.UI_MODE_NIGHT_YES){
            iconNotificaciones.setImageResource(R.drawable.ic_darkmode_notificaciones)
        }else{
            iconNotificaciones.setImageResource(R.drawable.ic_notificaciones)
        }

        val iconHome = findViewById<ImageView>(R.id.ivHome)
        val modoOscuro3 = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(modoOscuro3 == Configuration.UI_MODE_NIGHT_YES){
            iconHome.setImageResource(R.drawable.ic_home_dm)
        }else{
            iconHome.setImageResource(R.drawable.ic_home)
        }

        val iconCitas = findViewById<ImageView>(R.id.ivCitas)
        val modoOscuro4 = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(modoOscuro4 == Configuration.UI_MODE_NIGHT_YES){
            iconCitas.setImageResource(R.drawable.ic_file_menu_dm_24)
        }else{
            iconCitas.setImageResource(R.drawable.ic_active_file)
        }

        val iconChats = findViewById<ImageView>(R.id.ivChats)
        val modoOscuro5 = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(modoOscuro5 == Configuration.UI_MODE_NIGHT_YES){
            iconChats.setImageResource(R.drawable.ic_chats_dm)
        }else{
            iconChats.setImageResource(R.drawable.ic_chat)
        }

        val iconPerfil = findViewById<ImageView>(R.id.ivPerfil)
        val modoOscuro6 = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(modoOscuro6 == Configuration.UI_MODE_NIGHT_YES){
            iconPerfil.setImageResource(R.drawable.ic_perfil_dm)
        }else{
            iconPerfil.setImageResource(R.drawable.ic_perfil)
        }

        iconHome.setOnClickListener{
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

        val btnFormulario = findViewById<Button>(R.id.btnFormulario)

        btnFormulario.setOnClickListener {
            val pantallaFormulario = Intent(this, activity_citas_medicas::class.java)
            startActivity(pantallaFormulario)
        }
    }
}