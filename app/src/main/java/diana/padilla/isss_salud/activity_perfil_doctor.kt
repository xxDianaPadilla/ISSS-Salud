package diana.padilla.isss_salud

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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