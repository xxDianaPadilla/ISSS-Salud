package diana.padilla.isss_salud

import Modelo.ChatsDoctores
import Modelo.CitasAgendadas
import Modelo.ClaseConexion
import Modelo.PerfilDoctor
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class activity_perfil_doctor : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil_doctor)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val ImgDoctorPerfil2 = findViewById<ImageView>(R.id.ImgDoctorPerfil2)
        val txtViewCorreo = findViewById<TextView>(R.id.txtViewCorreo)
        val txtViewNombreDoctorP = findViewById<TextView>(R.id.txtViewNombreDoctorP)
        val txtViewEspecialidadDoctor = findViewById<TextView>(R.id.txtViewEspecialidadDoctor)
        val txtViewUnidadMedicaDoctor = findViewById<TextView>(R.id.txtViewUnidadMedicaDoctor)

        val fotoDoctor = intent.getStringExtra("foto_doctor")

        fun obtenerDoctorPorFoto(foto: String): PerfilDoctor? {
            var doctor: PerfilDoctor? = null

            CoroutineScope(Dispatchers.IO).launch {
                val objConexion = ClaseConexion().cadenaConexion()
                val obtenerDoctor =
                    objConexion?.prepareStatement("""
        SELECT 
            d.id_doctor,
            d.foto_doctor,
            d.correo_doctor,
            d.nombre_doctor,
            ed.especialidad_doctor AS Especialidad_Medica,
            um.nombre_unidad AS Unidad_Medica
        FROM 
            Doctores d
        INNER JOIN 
            EspecialidadDoctores ed ON d.id_especialidad = ed.id_especialidad
        INNER JOIN 
            UnidadesMedicas um ON d.id_unidad = um.id_unidad
        WHERE 
            d.foto_doctor = ?
    """)!!
                obtenerDoctor.setString(1, foto)
                val resultSet = obtenerDoctor.executeQuery()

                while (resultSet.next()){
                    var id_doctor = resultSet.getInt("id_doctor")
                    var foto_doctor: String = resultSet.getString("foto_doctor")
                    var correo_doctor: String = resultSet.getString("correo_doctor")
                    var nombre_doctor: String = resultSet.getString("nombre_doctor")
                    var Especialidad_Medica: String = resultSet.getString("Especialidad_Medica")
                    var Unidad_Medica: String = resultSet.getString("Unidad_Medica")

                    doctor = PerfilDoctor(id_doctor, foto_doctor, correo_doctor, nombre_doctor, Especialidad_Medica, Unidad_Medica)
                    println(doctor)
                }
            }

            return doctor
        }

        CoroutineScope(Dispatchers.Main).launch {
            val doctor = withContext(Dispatchers.IO) {obtenerDoctorPorFoto(fotoDoctor?: "")}
            if (doctor != null){
                Glide.with(this@activity_perfil_doctor)
                    .load(doctor.foto_doctor_url)
                    .into(ImgDoctorPerfil2)
                txtViewCorreo.text = doctor.correo_doctor
                txtViewNombreDoctorP.text = doctor.nombre_doctor
                txtViewEspecialidadDoctor.text = doctor.especialidad
                txtViewUnidadMedicaDoctor.text = doctor.unidad_medica
            }
        }

        val btnRegresar = findViewById<ImageView>(R.id.btnRegresar)
        val modoOscuro = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(modoOscuro == Configuration.UI_MODE_NIGHT_YES){
            btnRegresar.setImageResource(R.drawable.ic_regresar_dm)
        }else{
            btnRegresar.setImageResource(R.drawable.ic_regresar)
        }

        btnRegresar.setOnClickListener {
            val pantallaMensajeria2 = Intent(this, activity_mensajeria::class.java)
            startActivity(pantallaMensajeria2)
        }

        val ivCorreoP = findViewById<ImageView>(R.id.ivCorreoP)

        if (modoOscuro == Configuration.UI_MODE_NIGHT_YES) {
            ivCorreoP.setImageResource(R.drawable.ic_correo_dm)
        } else {
            ivCorreoP.setImageResource(R.drawable.ic_correo)
        }

        val ivBotiquinP = findViewById<ImageView>(R.id.ivBotiquinP)

        if (modoOscuro == Configuration.UI_MODE_NIGHT_YES) {
            ivBotiquinP.setImageResource(R.drawable.ic_botiquin_dm)
        } else {
            ivCorreoP.setImageResource(R.drawable.ic_botiquin)
        }

        val ivEspecialidadP = findViewById<ImageView>(R.id.ivEspecialidadP)

        if (modoOscuro == Configuration.UI_MODE_NIGHT_YES) {
            ivEspecialidadP.setImageResource(R.drawable.ic_especialidad_dm)
        } else {
            ivCorreoP.setImageResource(R.drawable.ic_especialidad)
        }

        val ivHospitalP = findViewById<ImageView>(R.id.ivHospitalP)

        if (modoOscuro == Configuration.UI_MODE_NIGHT_YES) {
            ivHospitalP.setImageResource(R.drawable.ic_hospital_dm)
        } else {
            ivCorreoP.setImageResource(R.drawable.ic_hospital)
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