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
import kotlinx.coroutines.*

class activity_bandeja_chat : AppCompatActivity() {

    private var idRemitente: Int = 0
    private var idDestinatario: Int = 0
    private lateinit var adapter: AdaptadorMensajes
    private lateinit var rcvMensajes: RecyclerView // Asegúrate de usar esta variable global
    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bandeja_chat)

        // Ajustar los padding de la vista principal para respetar los insets del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val image = intent.getStringExtra("doctor_image")
        val nombreDoctor = intent.getStringExtra("nombre_doctor")

        // Asignar los valores de idRemitente y idDestinatario
        idRemitente = activity_ingreso.variablesGlobales.idUsuarioGlobal
        idDestinatario = intent.getIntExtra("id_doctor", 0)

        // Asigna la vista de RecyclerView a la variable global
        rcvMensajes = findViewById(R.id.rcvMensajes)
        val listaMensajes = mutableListOf<Mensajes>()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true

        adapter = AdaptadorMensajes(listaMensajes, idRemitente)
        rcvMensajes.adapter = adapter
        rcvMensajes.layoutManager = layoutManager

        iniciarActualizacionPeriodica()

        // Mostrar la información del doctor
        val ivFotoDoctorC: ImageView = findViewById(R.id.ivFotoDoctorC)
        val txtNombreDoctor: TextView = findViewById(R.id.txtNombreDoctor)

        txtNombreDoctor.text = nombreDoctor
        if (image != null) {
            Glide.with(this)
                .load(image)
                .error("https://i.pinimg.com/236x/2a/2e/7f/2a2e7f0f60b750dfb36c15c268d0118d.jpg")
                .into(ivFotoDoctorC)
        }

        val btnEnviarMensaje = findViewById<ImageView>(R.id.btnEnviarMensaje)
        val txtMensaje = findViewById<EditText>(R.id.txtMensaje)

        // Lógica para enviar el mensaje
        btnEnviarMensaje.setOnClickListener {
            val mensaje = txtMensaje.text.toString()

            if (mensaje.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    val conexion = ClaseConexion().cadenaConexion()
                    val statement = conexion?.prepareStatement(
                        "INSERT INTO MensajesChat (id_remitente, tipo_remitente, id_destinatario, tipo_destinatario, mensaje) " +
                                "VALUES (?, 'PACIENTE', ?, 'DOCTOR', ?)"
                    )!!

                    statement.setInt(1, idRemitente)
                    statement.setInt(2, idDestinatario)
                    statement.setString(3, mensaje)

                    statement.executeUpdate()

                    withContext(Dispatchers.Main) {
                        val nuevoMensaje = Mensajes(idRemitente, "PACIENTE", idDestinatario, "DOCTOR", mensaje)
                        listaMensajes.add(nuevoMensaje)
                        adapter.notifyDataSetChanged()

                        // Limpiar el campo de texto después de enviar el mensaje
                        txtMensaje.text.clear()
                    }
                }
            }
        }

        // Configuración de íconos y modo oscuro
        val iconHome = findViewById<ImageView>(R.id.btnNoticias)
        val modoOscuro3 = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (modoOscuro3 == Configuration.UI_MODE_NIGHT_YES) {
            iconHome.setImageResource(R.drawable.ic_home_dm)
        } else {
            iconHome.setImageResource(R.drawable.ic_home)
        }

        val iconCitas = findViewById<ImageView>(R.id.btnCitas)
        val iconChats = findViewById<ImageView>(R.id.BtnChats)
        val iconPerfil = findViewById<ImageView>(R.id.btnPerfil)

        if (modoOscuro3 == Configuration.UI_MODE_NIGHT_YES) {
            iconCitas.setImageResource(R.drawable.ic_citas_dm)
            iconChats.setImageResource(R.drawable.ic_active_chat_dm)
            iconPerfil.setImageResource(R.drawable.ic_perfil_dm)
        } else {
            iconCitas.setImageResource(R.drawable.ic_citas)
            iconChats.setImageResource(R.drawable.ic_active_chat)
            iconPerfil.setImageResource(R.drawable.ic_perfil)
        }

        iconHome.setOnClickListener {
            startActivity(Intent(this, activity_noticias::class.java))
            overridePendingTransition(0, 0)
        }

        iconCitas.setOnClickListener {
            startActivity(Intent(this, activity_citas_medicas::class.java))
            overridePendingTransition(0, 0)
        }

        iconPerfil.setOnClickListener {
            startActivity(Intent(this, activity_perfil::class.java))
            overridePendingTransition(0, 0)
        }
    }

    // Función para iniciar la actualización periódica de mensajes usando corrutinas
    fun iniciarActualizacionPeriodica() {
        coroutineScope.launch {
            while (isActive) {
                actualizarMensajes()
                delay(2000)
            }
        }
    }

    // Función suspendida que actualiza los mensajes
    suspend fun actualizarMensajes() {
        if (idRemitente == 0 || idDestinatario == 0) {
            println("Error: ID de remitente o destinatario es 0 o vacío")
            return
        }

        val mensajes = obtenerMensajes(idRemitente, idDestinatario)
        withContext(Dispatchers.Main) {
            adapter.actualizarMensajes(mensajes)
            rcvMensajes.scrollToPosition(adapter.itemCount - 1)
        }
    }

    // Función suspendida para obtener los mensajes de la base de datos
    suspend fun obtenerMensajes(idRemitente: Int, idDestinatario: Int): List<Mensajes> {
        return withContext(Dispatchers.IO) {
            val listaMensajes = mutableListOf<Mensajes>()
            val conexion = ClaseConexion().cadenaConexion()
            conexion?.use { conexion ->
                val statement = conexion.prepareStatement(
                    "SELECT * FROM MensajesChat WHERE (id_remitente = ? AND id_destinatario = ?) OR " +
                            "(id_remitente = ? AND id_destinatario = ?) ORDER BY id_mensaje ASC"
                )

                statement.setInt(1, idRemitente)
                statement.setInt(2, idDestinatario)
                statement.setInt(3, idDestinatario)
                statement.setInt(4, idRemitente)

                val resultSet = statement.executeQuery()
                while (resultSet.next()) {
                    val idRemitenteDB = resultSet.getInt("id_remitente")
                    val tipoRemitente = resultSet.getString("tipo_remitente")
                    val idDestinatarioDB = resultSet.getInt("id_destinatario")
                    val tipoDestinatario = resultSet.getString("tipo_destinatario")
                    val mensaje = resultSet.getString("mensaje")

                    listaMensajes.add(Mensajes(idRemitenteDB, tipoRemitente, idDestinatarioDB, tipoDestinatario, mensaje))
                }
            }
            listaMensajes
        }
    }
}
