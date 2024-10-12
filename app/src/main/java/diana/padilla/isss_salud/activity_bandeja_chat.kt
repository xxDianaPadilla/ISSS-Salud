package diana.padilla.isss_salud

import Modelo.ClaseConexion
import Modelo.Mensajes
import RecyclerViewHelpers.AdaptadorMensajes
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class activity_bandeja_chat : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bandeja_chat)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val image = intent.getStringExtra("doctor_image")
        val nombreDoctor = intent.getStringExtra("nombre_doctor")

        val id_usuario = activity_ingreso.variablesGlobales.idUsuarioGlobal
        val idDoctor = intent.getIntExtra("id_doctor", 0)

        val rcvMensajes = findViewById<RecyclerView>(R.id.rcvMensajes)

        val listaMensajes = mutableListOf<Mensajes>()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true // Esta propiedad asegura que el scroll empiece desde el final, mostrando los mensajes más recientes.

        val adapter = AdaptadorMensajes(listaMensajes, id_usuario)
        rcvMensajes.adapter = adapter
        rcvMensajes.layoutManager = layoutManager

        CoroutineScope(Dispatchers.IO).launch {
            val conexion = ClaseConexion().cadenaConexion()
            val statement = conexion?.prepareStatement("SELECT * FROM MensajesChat WHERE (id_remitente = ? AND id_destinatario = ?) OR (id_remitente = ? AND id_destinatario = ?) ORDER BY id_mensaje ASC")!!

            statement.setInt(1, id_usuario)
            statement.setInt(2, idDoctor)
            statement.setInt(3, idDoctor)
            statement.setInt(4, id_usuario)

            val resultSet = statement.executeQuery()

            while (resultSet.next() == true){
                val idRemitente = resultSet.getInt("id_remitente")
                val tipoRemitente = resultSet.getString("tipo_remitente")
                val idDestinatario = resultSet.getInt("id_destinatario")
                val tipoDestinatario = resultSet.getString("tipo_destinatario")
                val mensaje = resultSet.getString("mensaje")

                listaMensajes.add(Mensajes(idRemitente, tipoRemitente, idDestinatario, tipoDestinatario, mensaje))
            }

            withContext(Dispatchers.Main){
                adapter.notifyDataSetChanged()
            }
        }

        val ivFotoDoctorC: ImageView = findViewById(R.id.ivFotoDoctorC)
        val txtNombreDoctor: TextView = findViewById(R.id.txtNombreDoctor)

        txtNombreDoctor.text = nombreDoctor
        if(image != null){
            Glide.with(this)
                .load(image)
                .error("https://i.pinimg.com/236x/2a/2e/7f/2a2e7f0f60b750dfb36c15c268d0118d.jpg")
                .into(ivFotoDoctorC)
        }

        val modoOscuro = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val btnBackC = findViewById<ImageView>(R.id.btnBackC)
        val btnEnviarMensaje = findViewById<ImageView>(R.id.btnEnviarMensaje)
        val txtMensaje = findViewById<EditText>(R.id.txtMensaje)

        btnEnviarMensaje.setOnClickListener {
            val mensaje = txtMensaje.text.toString()
            val idUsuario = activity_ingreso.variablesGlobales.idUsuarioGlobal

            if(mensaje.isNotEmpty()){
                CoroutineScope(Dispatchers.IO).launch {
                    val conexion = ClaseConexion().cadenaConexion()
                    val statement = conexion?.prepareStatement("INSERT INTO MensajesChat (id_remitente, tipo_remitente, id_destinatario, tipo_destinatario, mensaje) VALUES (?, 'PACIENTE', ?, 'DOCTOR', ?)")!!

                    statement.setInt(1, idUsuario)
                    statement.setInt(2, idDoctor)
                    statement.setString(3, mensaje)

                    statement.executeUpdate()

                    withContext(Dispatchers.Main) {
                        // Agregar el mensaje al RecyclerView
                        val nuevoMensaje = Mensajes(idUsuario, "PACIENTE", idDoctor, "DOCTOR", mensaje)
                        listaMensajes.add(nuevoMensaje)
                        adapter.notifyDataSetChanged()

                        // Limpiar el campo de texto después de enviar el mensaje
                        txtMensaje.text.clear()
                    }
                }
            }
        }

        if(modoOscuro == Configuration.UI_MODE_NIGHT_YES){
            btnBackC.setImageResource(R.drawable.ic_regresar_dm)
        }else{
            btnBackC.setImageResource(R.drawable.ic_regresar)
        }

        btnBackC.setOnClickListener {
            finish()
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
    }
}