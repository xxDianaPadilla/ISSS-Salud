package diana.padilla.isss_salud

import Modelo.ClaseConexion
import Modelo.NoticiasNuevas
import RecyclerViewHelpers.AdaptadorNoticias
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
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

class activity_noticias : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_noticias)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val ivMenuCircle = findViewById<ImageView>(R.id.ivMenuCircle)

        /*ivMenuCircle.setOnClickListener {
            ivMenuCircle.animate().apply {
                duration = 1000
            }
        }*/

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fun obtenerNoticias(): List<NoticiasNuevas>{
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM NoticiasMedicas")!!

            val listaNoticias = mutableListOf<NoticiasNuevas>()

            while(resultSet.next()){
                val id_noticia = resultSet.getInt("id_noticia")
                val imagen_noticia = resultSet.getString("imagen_noticia")
                val titulo_noticia = resultSet.getString("titulo_noticia")
                val descripcion_noticia = resultSet.getString("descripcion_noticia")
                val fecha_noticia = resultSet.getDate("fecha_noticia")

                val valoresJuntos = NoticiasNuevas(id_noticia, imagen_noticia, titulo_noticia, descripcion_noticia, fecha_noticia.toString())

                listaNoticias.add(valoresJuntos)
            }

            return listaNoticias
        }

        CoroutineScope(Dispatchers.IO).launch {
            val noticiasNuevas = obtenerNoticias()
            withContext(Dispatchers.Main){
                val adapter = AdaptadorNoticias(noticiasNuevas)
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

        val iconHome = findViewById<ImageView>(R.id.btnNoticias)
        val modoOscuro3 = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(modoOscuro3 == Configuration.UI_MODE_NIGHT_YES){
            iconHome.setImageResource(R.drawable.ic_home_menu_dm)
        }else{
            iconHome.setImageResource(R.drawable.ic_active_home)
        }

        val iconNotificaciones = findViewById<ImageView>(R.id.ivNotificaciones)
        val modoOscuro2 = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(modoOscuro2 == Configuration.UI_MODE_NIGHT_YES){
            iconNotificaciones.setImageResource(R.drawable.ic_darkmode_notificaciones)
        }else{
            iconNotificaciones.setImageResource(R.drawable.ic_notificaciones)
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
            iconPerfil.setImageResource(R.drawable.ic_perfil_dm)
        }else{
            iconPerfil.setImageResource(R.drawable.ic_perfil)
        }

        iconCitas.setOnClickListener{
            val pantallaCitasMedicas = Intent(this, activity_citas_medicas::class.java)
            startActivity(pantallaCitasMedicas)
        }

        iconChats.setOnClickListener {
            val pantallaMensajeria = Intent(this, activity_mensajeria::class.java)
            startActivity(pantallaMensajeria)
        }

        iconPerfil.setOnClickListener {
            val pantallaPerfil = Intent(this, activity_perfil::class.java)
            startActivity(pantallaPerfil)
        }
    }
}