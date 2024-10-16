package diana.padilla.isss_salud

import Modelo.ClaseConexion
import Modelo.ExpedienteMedico
import Modelo.Perfil
import android.app.Activity
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class activity_perfil : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE = 100
    }

    lateinit var imagenPerfil: ImageView
    lateinit var correoPerfilE: EditText
    lateinit var telefonoPerfilE: EditText
    lateinit var duiPerfilE: EditText
    lateinit var tipoSangreE: EditText
    lateinit var openExpediente: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        openExpediente = findViewById(R.id.btnDescargarExpediente)

        openExpediente.setOnClickListener {
            generarExpedientePDF()
        }

        cargarDatosPerfilEnPantalla()

        imagenPerfil = findViewById<ImageView>(R.id.ImgPerfilSinCargar)
        correoPerfilE = findViewById<EditText>(R.id.txtViewCorreoPerfilE)
        telefonoPerfilE = findViewById<EditText>(R.id.txtViewTelefonoPerfilE)
        duiPerfilE = findViewById<EditText>(R.id.txtViewDuiPerfilE)
        tipoSangreE = findViewById<EditText>(R.id.txtViewTipoSangreE)


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
            iconChats.setImageResource(R.drawable.ic_chats_dm)
        }else{
            iconChats.setImageResource(R.drawable.ic_chat)
        }

        val iconPerfil = findViewById<ImageView>(R.id.btnPerfil)
        val modoOscuro6 = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(modoOscuro6 == Configuration.UI_MODE_NIGHT_YES){
            iconPerfil.setImageResource(R.drawable.ic_active_profile_dm)
        }else{
            iconPerfil.setImageResource(R.drawable.ic_active_profile)
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

        iconChats.setOnClickListener {
            val pantallaMensajeria = Intent(this, activity_mensajeria::class.java)
            startActivity(pantallaMensajeria)
            overridePendingTransition(0, 0)
        }

        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)

        btnCerrarSesion.setOnClickListener {
            val pantallaIngreso = Intent(this, activity_ingreso::class.java)
            startActivity(pantallaIngreso)
            finish()
        }

        val btnEditarPerfil = findViewById<Button>(R.id.btnCargarImagen)

        btnEditarPerfil.setOnClickListener {
            val pantallaEditarPerfil = Intent(this, activity_editar_perfil::class.java)
            startActivityForResult(pantallaEditarPerfil, REQUEST_CODE)  // Cambia a startActivityForResult
            overridePendingTransition(0, 0)
        }
    }

    suspend fun obtenerDatosPerfil(idDeLaVariableGlobal: Int): List<Perfil>{
        val perfil = mutableListOf<Perfil>()

        withContext(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()
            val obtenerPerfil =
                objConexion?.prepareStatement("SELECT foto_usuario, dui, correo_electronico, telefono, tipo_sangre FROM Usuarios WHERE id_usuario = ?")!!
            obtenerPerfil.setInt(1, idDeLaVariableGlobal)
            val resultSet = obtenerPerfil.executeQuery()

            while (resultSet.next()){
                val foto_usuario: String = resultSet.getString("foto_usuario")
                val correo_electronico: String = resultSet.getString("correo_electronico")
                val telefono: String = resultSet.getString("telefono")
                val dui: String = resultSet.getString("dui")
                val tipo_sangre: String = resultSet.getString("tipo_sangre")

                val perfilCompleto = Perfil(foto_usuario, correo_electronico, telefono, dui, tipo_sangre)
                println(perfilCompleto)
                perfil.add(perfilCompleto)
            }
        }

        return perfil
    }

    suspend fun obtenerExpedienteMedico(idUsuario: Int): ExpedienteMedico? {
        var expediente: ExpedienteMedico? = null
        withContext(Dispatchers.IO) {
            val objConexion = ClaseConexion().cadenaConexion()
            val obtenerExpediente = objConexion?.prepareStatement("""
            SELECT u.nombre_usuario, u.dui, u.sexo, u.edad, u.tipo_sangre, u.telefono, u.correo_electronico, 
                   e.antecedentes_familiares, e.problemas_salud_preexistentes, e.alergias, 
                   e.salud_actual, e.resultados_examenes_laboratorio, e.ficha_ingreso
            FROM Usuarios u
            INNER JOIN ExpedientesMedicos e ON u.id_usuario = e.id_usuario
            WHERE u.id_usuario = ?
        """)
            obtenerExpediente?.setInt(1, idUsuario)
            val resultSet = obtenerExpediente?.executeQuery()

            if (resultSet?.next() == true) {
                expediente = ExpedienteMedico(
                    resultSet.getString("nombre_usuario"),
                    resultSet.getString("dui"),
                    resultSet.getString("sexo"),
                    resultSet.getString("edad"),
                    resultSet.getString("tipo_sangre"),
                    resultSet.getString("telefono"),
                    resultSet.getString("correo_electronico"),
                    resultSet.getString("antecedentes_familiares"),
                    resultSet.getString("problemas_salud_preexistentes"),
                    resultSet.getString("alergias"),
                    resultSet.getString("salud_actual"),
                    resultSet.getString("resultados_examenes_laboratorio"),
                    resultSet.getString("ficha_ingreso")
                )
            }
        }
        return expediente
    }

    fun cargarDatosPerfilEnPantalla() {
        CoroutineScope(Dispatchers.Main).launch {
            val idDeLaVariableGlobal = activity_ingreso.variablesGlobales.idUsuarioGlobal
            val perfiles = obtenerDatosPerfil(idDeLaVariableGlobal)

            if (perfiles.isNotEmpty()) {
                val miFoto = perfiles[0].foto_usuario
                Glide.with(this@activity_perfil)
                    .load(miFoto)
                    .into(imagenPerfil)
                val miDui = perfiles[0].dui
                duiPerfilE.hint = miDui
                val miCorreo = perfiles[0].correo_electronico
                correoPerfilE.hint = miCorreo
                val miTelefono = perfiles[0].telefono
                telefonoPerfilE.hint = miTelefono // Solo actualiza si el hint está vacío
                val miTipoSangre = perfiles[0].tipo_sangre
                tipoSangreE.hint = miTipoSangre
            }
        }
    }

    private fun generarExpedientePDF() {
        CoroutineScope(Dispatchers.Main).launch {
            val idUsuario = activity_ingreso.variablesGlobales.idUsuarioGlobal // Obtener el id_usuario de la variable global
            val expediente = obtenerExpedienteMedico(idUsuario)

            if (expediente != null) {
                val pdfDocument = PdfDocument()
                val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
                val page = pdfDocument.startPage(pageInfo)

                val canvas = page.canvas
                val paint = Paint()
                paint.textSize = 16f

                // Agrega datos al PDF
                canvas.drawText("Nombre: ${expediente.nombreUsuario}", 50f, 50f, paint)
                canvas.drawText("DUI: ${expediente.dui}", 50f, 80f, paint)
                canvas.drawText("Sexo: ${expediente.sexo}", 50f, 110f, paint)
                canvas.drawText("Edad: ${expediente.edad}", 50f, 140f, paint)
                canvas.drawText("Tipo de sangre: ${expediente.tipoSangre}", 50f, 170f, paint)
                canvas.drawText("Teléfono: ${expediente.telefono}", 50f, 200f, paint)
                canvas.drawText("Correo: ${expediente.correoElectronico}", 50f, 230f, paint)
                canvas.drawText("Antecedentes familiares: ${expediente.antecedentesFamiliares}", 50f, 260f, paint)
                canvas.drawText("Problemas de salud preexistentes: ${expediente.problemasSaludPreexistentes}", 50f, 290f, paint)
                canvas.drawText("Alergias: ${expediente.alergias}", 50f, 320f, paint)
                canvas.drawText("Salud actual: ${expediente.saludActual}", 50f, 350f, paint)
                canvas.drawText("Resultados de exámenes: ${expediente.resultadosExamenesLaboratorio}", 50f, 380f, paint)
                canvas.drawText("Ficha de ingreso: ${expediente.fichaIngreso}", 50f, 410f, paint)

                pdfDocument.finishPage(page)

                // Guardar PDF en el almacenamiento del dispositivo
                val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Expediente_Medico_$idUsuario.pdf")
                pdfDocument.writeTo(FileOutputStream(file))
                pdfDocument.close()

                mostrarNotificacion(file)

                // Mostrar un AlertDialog de éxito
                AlertDialog.Builder(this@activity_perfil)
                    .setTitle("Descarga exitosa")
                    .setMessage("El expediente médico ha sido descargado correctamente.")
                    .setPositiveButton("OK", null)
                    .show()
            } else {
                Toast.makeText(this@activity_perfil, "No se pudo obtener el expediente médico.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun mostrarNotificacion(file: File) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "mi_canal"

        // Crear el canal de notificación (si no se ha creado anteriormente)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Nombre del canal", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // Crea un intent para abrir el archivo PDF
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(FileProvider.getUriForFile(this@activity_perfil, "diana.padilla.isss_salud.fileprovider", file), "application/pdf")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION // Permitir el acceso de lectura al archivo
        }

        // Crear el PendingIntent con FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        // Construir la notificación
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_darkmode_notificaciones) // Cambia esto por tu ícono de notificación
            .setContentTitle("Descarga completada")
            .setContentText("Tu expediente médico ha sido descargado.")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // La notificación se eliminará al tocarla
            .build()

        // Mostrar la notificación
        notificationManager.notify(1, notification)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            // Verificar si el permiso fue concedido
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permiso concedido, generar el PDF
                generarExpedientePDF()
            } else {
                // Permiso denegado, mostrar mensaje
                Toast.makeText(this, "Permiso de escritura denegado", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val nuevoTelefono = data?.getStringExtra("nuevoTelefono")
            val nuevoCorreo = data?.getStringExtra("nuevoCorreo")
            if (nuevoTelefono != null) {
                if (telefonoPerfilE.hint != nuevoTelefono) {
                    telefonoPerfilE.hint = nuevoTelefono
                }
            }
            if (nuevoCorreo != null) {
                if (correoPerfilE.hint != nuevoCorreo) {
                    correoPerfilE.hint = nuevoCorreo
                }
            }
        }
    }
}