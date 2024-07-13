package diana.padilla.isss_salud

import Modelo.CitasAgendadas
import Modelo.ClaseConexion
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
            val listaCitasAgendadas = mutableListOf<CitasAgendadas>()

            if (objConexion != null) {
                val statement = objConexion.createStatement()
                val query = """
            SELECT 
                cm.id_cita, 
                cm.fecha_cita, 
                cm.hora_cita, 
                u.correo_electronico AS solicitante, 
                d.nombre_doctor AS doctor 
            FROM 
                CitasMedicas cm 
            INNER JOIN 
                Usuarios u ON cm.id_usuario = u.id_usuario 
            INNER JOIN 
                Doctores d ON cm.id_doctor = d.id_doctor
        """

                val resultSet = statement.executeQuery(query)

                while (resultSet.next()) {
                    val idCita = resultSet.getString("id_cita").toInt()
                    val fechaCita = resultSet.getDate("fecha_cita").toString() // Convierte la fecha a string
                    val horaCita = resultSet.getString("hora_cita")
                    val solicitante = resultSet.getString("solicitante")
                    val doctor = resultSet.getString("doctor")

                    val citaAgendada = CitasAgendadas(
                        idCita,
                        fechaCita,
                        horaCita,
                        solicitante,
                        doctor
                    )

                    listaCitasAgendadas.add(citaAgendada)
                }

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
            iconCitas.setImageResource(R.drawable.ic_file_menu_dm_24)
        }else{
            iconCitas.setImageResource(R.drawable.ic_active_file)
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
            iconPerfil.setImageResource(R.drawable.ic_perfil_dm)
        }else{
            iconPerfil.setImageResource(R.drawable.ic_perfil)
        }

        iconHome.setOnClickListener{
            val pantallaNoticias = Intent(this, activity_noticias::class.java)
            startActivity(pantallaNoticias)
            overridePendingTransition(0, 0)
        }

        iconChats.setOnClickListener {
            val pantallaMensajeria = Intent(this, activity_mensajeria::class.java)
            startActivity(pantallaMensajeria)
            overridePendingTransition(0, 0)
        }

        iconPerfil.setOnClickListener {
            val pantallaPerfil = Intent(this, activity_perfil::class.java)
            startActivity(pantallaPerfil)
            overridePendingTransition(0, 0)
        }

        val btnFormulario = findViewById<Button>(R.id.btnFormularioCitas)

        btnFormulario.setOnClickListener {
            val pantallaFormulario = Intent(this, activity_citas_medicas::class.java)
            startActivity(pantallaFormulario)
            overridePendingTransition(0, 0)
        }
    }
}