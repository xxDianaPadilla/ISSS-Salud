package diana.padilla.isss_salud

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

class activity_ingreso : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ingreso)
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

        val btnRegistrarse = findViewById<Button>(R.id.btnRegistrarse)

        btnRegistrarse.setOnClickListener {
            val pantallaRegistrarse = Intent(this, activity_registrarse::class.java)
            startActivity(pantallaRegistrarse)
        }

        val txtOlvidoContrasena = findViewById<TextView>(R.id.txtForgotPassword)

        txtOlvidoContrasena.setOnClickListener {
            val pantallaOlvidoContrasena = Intent(this, activity_contrasena_enlace::class.java)
            startActivity(pantallaOlvidoContrasena)
        }
    }
}