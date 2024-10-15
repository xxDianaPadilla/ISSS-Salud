package diana.padilla.isss_salud

import Modelo.ChatsDoctores
import Modelo.ClaseConexion
import RecyclerViewHelpers.AdaptadorChats
import RecyclerViewHelpers.AdaptadorDocBusqueda
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.EditText
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

class activity_mensajeria : AppCompatActivity() {

    private lateinit var rcvDoctores: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mensajeria)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rcvDoctores = findViewById<RecyclerView>(R.id.rcvDoctores)
        rcvDoctores.layoutManager = LinearLayoutManager(this)

        cargarDoctores()

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
            iconChats.setImageResource(R.drawable.ic_active_chat_dm)
        }else{
            iconChats.setImageResource(R.drawable.ic_active_chat)
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

        iconCitas.setOnClickListener {
            val pantallaCitas = Intent(this, activity_citas_medicas::class.java)
            startActivity(pantallaCitas)
            overridePendingTransition(0, 0)
        }

        iconPerfil.setOnClickListener {
            val pantallaPerfil = Intent(this, activity_perfil::class.java)
            startActivity(pantallaPerfil)
            overridePendingTransition(0, 0)
        }

        val txtBuscarDoctores = findViewById<EditText>(R.id.txtBuscarDoctores)

        txtBuscarDoctores.setOnClickListener{
            val pantallaHistorial = Intent(this, activity_historial_busqueda::class.java)
            startActivity(pantallaHistorial)
            overridePendingTransition(0,0)
        }
    }

    private fun obtenerDoctores(): List<ChatsDoctores>{
        val objConexion = ClaseConexion().cadenaConexion()

        val statement = objConexion?.prepareStatement("SELECT DISTINCT D.id_doctor, D.correo_doctor, D.contrasena_doctor, D.nombre_doctor, D.foto_doctor, D.id_especialidad, D.id_unidad FROM Doctores D JOIN MensajesChat M ON D.id_doctor = M.id_destinatario WHERE M.id_remitente = ? AND M.tipo_destinatario = 'DOCTOR'")!!

        statement.setInt(1, activity_ingreso.variablesGlobales.idUsuarioGlobal)

        val resultSet = statement.executeQuery()

        val listaDoctores = mutableListOf<ChatsDoctores>()

        while (resultSet.next()){
            val id_doctor = resultSet.getInt("id_doctor")
            val correo_doctor = resultSet.getString("correo_doctor")
            val contrasena_doctor = resultSet.getString("contrasena_doctor")
            val nombre_doctor = resultSet.getString("nombre_doctor")
            val foto_doctor = resultSet.getString("foto_doctor")
            val id_especialidad = resultSet.getInt("id_especialidad")
            val id_unidad = resultSet.getInt("id_unidad")

            val valoresJuntos = ChatsDoctores(id_doctor, correo_doctor, contrasena_doctor, nombre_doctor, foto_doctor, id_especialidad.toString(), id_unidad.toString())

            listaDoctores.add(valoresJuntos)
        }

        return listaDoctores
    }


    private fun cargarDoctores() {
        CoroutineScope(Dispatchers.IO).launch {
            val doctoresDB = obtenerDoctores()
            withContext(Dispatchers.Main) {
                val adapter = AdaptadorChats(doctoresDB)
                rcvDoctores.adapter = adapter
            }
        }
    }

    override fun onResume() {
        super.onResume()
        cargarDoctores()  
    }
}