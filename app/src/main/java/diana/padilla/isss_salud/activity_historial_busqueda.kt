package diana.padilla.isss_salud

import Modelo.ChatsDoctores
import Modelo.ClaseConexion
import RecyclerViewHelpers.AdaptadorChats
import RecyclerViewHelpers.AdaptadorDocBusqueda
import android.graphics.Rect
import android.os.Bundle
import android.view.View
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

class activity_historial_busqueda : AppCompatActivity() {
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


        fun obtenerDoctores2(): List<ChatsDoctores>{
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM Doctores")!!

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

        CoroutineScope(Dispatchers.IO).launch {
            val doctoresDB = obtenerDoctores2()
            withContext(Dispatchers.Main){
                val adapter = AdaptadorDocBusqueda(doctoresDB)
                rcvHistorialDoctores.adapter = adapter
            }
        }
    }

}