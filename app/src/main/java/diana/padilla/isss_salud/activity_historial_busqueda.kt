package diana.padilla.isss_salud

import Modelo.ChatsDoctores
import Modelo.ClaseConexion
import RecyclerViewHelpers.AdaptadorChats
import RecyclerViewHelpers.AdaptadorDocBusqueda
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class activity_historial_busqueda : AppCompatActivity() {

    private lateinit var llHistory: LinearLayout
    private lateinit var searchHistory: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_historial_busqueda)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val rcvHistorialDoctores = findViewById<RecyclerView>(R.id.rcvHistorialDoctores)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rcvHistorialDoctores.layoutManager = layoutManager


        fun obtenerDoctores2(): List<ChatsDoctores> {
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM Doctores")!!

            val listaDoctores = mutableListOf<ChatsDoctores>()

            while (resultSet.next()) {
                val id_doctor = resultSet.getInt("id_doctor")
                val correo_doctor = resultSet.getString("correo_doctor")
                val contrasena_doctor = resultSet.getString("contrasena_doctor")
                val nombre_doctor = resultSet.getString("nombre_doctor")
                val foto_doctor = resultSet.getString("foto_doctor")
                val id_especialidad = resultSet.getInt("id_especialidad")
                val id_unidad = resultSet.getInt("id_unidad")

                val valoresJuntos = ChatsDoctores(
                    id_doctor,
                    correo_doctor,
                    contrasena_doctor,
                    nombre_doctor,
                    foto_doctor,
                    id_especialidad.toString(),
                    id_unidad.toString()
                )

                listaDoctores.add(valoresJuntos)
            }

            return listaDoctores
        }

        CoroutineScope(Dispatchers.IO).launch {
            val doctoresDB = obtenerDoctores2()
            withContext(Dispatchers.Main) {
                val adapter = AdaptadorDocBusqueda(doctoresDB)
                rcvHistorialDoctores.adapter = adapter
            }
        }

        val etSearch  = findViewById<EditText>(R.id.txtBarraBusqueda)
        val ivSearch  = findViewById<ImageView>(R.id.btnBuscar2)
        llHistory = findViewById<LinearLayout>(R.id.llHistory)

        searchHistory  = loadSearchHistory()

        ivSearch.setOnClickListener {
            val searchText = etSearch.text.toString()
            if (searchText.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    val doctoresDB = obtenerDoctores2()
                    val doctoresFiltrados = filtrarDoctores(searchText, doctoresDB)
                    withContext(Dispatchers.Main) {
                        val adapter = AdaptadorDocBusqueda(doctoresFiltrados)
                        rcvHistorialDoctores.adapter = adapter
                    }
                }

                addSearchHistory(searchText)
                etSearch.text.clear()

            }
        }

        displaySearchHistory()


        val iconHome = findViewById<ImageView>(R.id.btnNoticias)
        val modoOscuro3 = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (modoOscuro3 == Configuration.UI_MODE_NIGHT_YES) {
            iconHome.setImageResource(R.drawable.ic_home_dm)
        } else {
            iconHome.setImageResource(R.drawable.ic_home)
        }

        val iconCitas = findViewById<ImageView>(R.id.btnCitas)
        val modoOscuro4 = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (modoOscuro4 == Configuration.UI_MODE_NIGHT_YES) {
            iconCitas.setImageResource(R.drawable.ic_citas_dm)
        } else {
            iconCitas.setImageResource(R.drawable.ic_citas)
        }

        val iconChats = findViewById<ImageView>(R.id.BtnChats)
        val modoOscuro5 = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (modoOscuro5 == Configuration.UI_MODE_NIGHT_YES) {
            iconChats.setImageResource(R.drawable.ic_active_chat_dm)
        } else {
            iconChats.setImageResource(R.drawable.ic_active_chat)
        }

        val iconPerfil = findViewById<ImageView>(R.id.btnPerfil)
        val modoOscuro6 = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (modoOscuro6 == Configuration.UI_MODE_NIGHT_YES) {
            iconPerfil.setImageResource(R.drawable.ic_perfil_dm)
        } else {
            iconPerfil.setImageResource(R.drawable.ic_perfil)
        }


        iconHome.setOnClickListener {
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

        val btnBackArrow = findViewById<ImageView>(R.id.btnBackArrow)

        btnBackArrow.setOnClickListener {
            val pantallaMensajeria = Intent(this, activity_mensajeria::class.java)
            startActivity(pantallaMensajeria)
        }
    }

    private fun filtrarDoctores(nombre: String, listaDoctores: List<ChatsDoctores>): List<ChatsDoctores>{
        return listaDoctores.filter {
            it.nombre_doctor.contains(nombre, ignoreCase = true)
        }
    }

    private fun addSearchHistory(searchText: String){
        searchHistory.add(searchText)
        saveSearchHistory()
        addHistoryView(searchText)
    }

    private fun addHistoryView(searchText: String) {
        val newItemLayout  = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            val icon = ImageView(context).apply {
                setImageResource(android.R.drawable.ic_menu_recent_history)
                setPadding(8, 8, 15, 8)
            }
            val textView = TextView(context).apply {
                text = searchText
                setPadding(8, 15, 10, 8)
                setOnClickListener{
                    showDeleteDialog(searchText)
                }
            }
            addView(icon)
            addView(textView)
        }
        llHistory.addView(newItemLayout)
    }

    private fun showDeleteDialog(searchText: String){
        AlertDialog.Builder(this)
            .setMessage("¿Deseas eliminar esta búsqueda del historial?")
            .setPositiveButton("Aceptar"){_, _ ->
                searchHistory.remove(searchText)
                saveSearchHistory()
                displaySearchHistory()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun displaySearchHistory(){
        llHistory.removeAllViews()
        searchHistory.forEach {searchText ->
            addHistoryView(searchText)
        }
    }

    private fun loadSearchHistory(): MutableList<String>{
        val sharedPreferences = getSharedPreferences("search_prefs", Context.MODE_PRIVATE)
        val historySet = sharedPreferences.getStringSet("search_history", setOf()) ?: setOf()
        return historySet.toMutableList()
    }

    private fun saveSearchHistory(){
        val sharedPreferences = getSharedPreferences("search_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("search_history", searchHistory.toSet())
        editor.apply()
    }
}