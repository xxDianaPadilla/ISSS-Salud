package diana.padilla.isss_salud

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

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val newsList = listOf(
            NoticiasNuevas("Científicos advierten que la gripe podría causar la próxima pandemia", "Una encuesta realizada entre 187 científicos de alto nivel revela que una cepa del virus de la gripe será la causa de la próxima pandemia; por su parte, la OMS indica que la última crisis sanitaria mundial (covid 19) evidenció lo mal preparado que estaba el mundo para enfrentar una emergencia como esa.", "01-01-2024", "https://www.laprensagrafica.com/__export/1713984859692/sites/prensagrafica/img/2024/04/24/pandemia-1654083868.jpg_554688467.jpg"),
            NoticiasNuevas("Title 2", "Description 2", "02-01-2024", "ic_logo_isss_small"),
            NoticiasNuevas("Title 3", "Description 3", "03-01-2024", "ic_logo_isss_small"),
            NoticiasNuevas("Title 4", "Description 4", "04-01-2024", "ic_logo_isss_small"),
            NoticiasNuevas("Title 5", "Description 5", "05-01-2024", "ic_logo_isss_small"),
            NoticiasNuevas("Title 6", "Description 6", "06-01-2024", "ic_logo_isss_small"),
            NoticiasNuevas("Title 7", "Description 7", "07-01-2024", "ic_logo_isss_small"),
            NoticiasNuevas("Title 8", "Description 8", "08-01-2024", "ic_logo_isss_small")
        )

        val adapter = AdaptadorNoticias(this, newsList)
        recyclerView.adapter = adapter

        val logoIsssSmall = findViewById<ImageView>(R.id.ivSmallLogo)
        val modoOscuro = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(modoOscuro == Configuration.UI_MODE_NIGHT_YES){
            logoIsssSmall.setImageResource(R.drawable.ic_logo_isss_dark)
        }else{
            logoIsssSmall.setImageResource(R.drawable.ic_logo_isss_small)
        }

        val iconHome = findViewById<ImageView>(R.id.ivHome)
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

        val iconCitas = findViewById<ImageView>(R.id.ivCitas)
        val modoOscuro4 = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(modoOscuro4 == Configuration.UI_MODE_NIGHT_YES){
            iconCitas.setImageResource(R.drawable.ic_citas_dm)
        }else{
            iconCitas.setImageResource(R.drawable.ic_citas)
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