package diana.padilla.isss_salud

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class activity_contrasena_enlace : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contrasena_enlace)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val logoISSS = findViewById<ImageView>(R.id.IvLogoIsss)
        val modoOscuro = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(modoOscuro == Configuration.UI_MODE_NIGHT_YES){
            logoISSS.setImageResource(R.drawable.ic_modo_oscuro_logo)
        }else{
            logoISSS.setImageResource(R.drawable.id_logo_isss)
        }

        val fondoCirculo = findViewById<ConstraintLayout>(R.id.clFondoCirculo)
        val modoOscuro2 = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(modoOscuro2 == Configuration.UI_MODE_NIGHT_YES){
            fondoCirculo.setBackgroundResource(R.drawable.ic_circulo_oscuro)
        }else{
            fondoCirculo.setBackgroundResource(R.drawable.ic_circulo)
        }

        val fondoIcon = findViewById<ConstraintLayout>(R.id.clLock)
        val modoOscuro3 = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(modoOscuro3 == Configuration.UI_MODE_NIGHT_YES){
            fondoIcon.setBackgroundResource(R.drawable.ic_dark_lock)
        }else{
            fondoIcon.setBackgroundResource(R.drawable.ic_lock)
        }

        val orLines = findViewById<ImageView>(R.id.ivOrLine)
        val modoOscuro4 = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(modoOscuro4 == Configuration.UI_MODE_NIGHT_YES){
            orLines.setImageResource(R.drawable.or_dark_line)
        }else{
            orLines.setImageResource(R.drawable.or_lines)
        }

        val btnEnvioCorreo = findViewById<Button>(R.id.btnEnvioCorreo)

        btnEnvioCorreo.setOnClickListener {
            val pantallaEnvioCorreo = Intent(this, activity_envio_correo::class.java)
            startActivity(pantallaEnvioCorreo)
        }

        val btnVolverInicioSesion = findViewById<ConstraintLayout>(R.id.btnVolverInicioSesion)
        val txtVolverInicioSesion = findViewById<TextView>(R.id.txtVolverInicioSesion)

        btnVolverInicioSesion.setOnClickListener {
            val pantallaInicioSesion = Intent(this, activity_ingreso::class.java)
            startActivity(pantallaInicioSesion)
        }

        txtVolverInicioSesion.setOnClickListener {
            val pantallaInicioSesion2 = Intent(this, activity_ingreso::class.java)
            startActivity(pantallaInicioSesion2)
        }
    }
}