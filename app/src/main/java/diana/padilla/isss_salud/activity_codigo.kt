package diana.padilla.isss_salud

import android.widget.Toast
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

class activity_codigo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_codigo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val num1 = findViewById<EditText>(R.id.txtNum1)
        val num2 = findViewById<EditText>(R.id.txtNum2)
        val num3 = findViewById<EditText>(R.id.txtNum3)
        val num4 = findViewById<EditText>(R.id.txtNum4)
        val num5 = findViewById<EditText>(R.id.txtNum5)
        val num6 = findViewById<EditText>(R.id.txtNum6)
        val btnConfirmarCodigo = findViewById<TextView>(R.id.btnConfirmarCodigo)

        val codigoQueEnvie = activity_correo_para_codigo.variablesGlobalesCorreoparacodigo.codigoRecuperacion

        val codigoRecu = "${num1.text}${num2.text}${num3.text}${num4.text}${num5.text}${num6.text}"
        println("Codigo correcto $codigoRecu $codigoQueEnvie")
        btnConfirmarCodigo.setOnClickListener {
        if (codigoRecu == codigoQueEnvie){
            val PantallaCambioContrasena = Intent(this, activity_cambio_contrasena::class.java)
            startActivity(PantallaCambioContrasena)
        }
        else{
            Toast.makeText(this, "Codigo incorrecto", Toast.LENGTH_SHORT).show()
        }
        }
        val logoISSS = findViewById<ImageView>(R.id.IvLogoIsss)
        val modoOscuro = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(modoOscuro == Configuration.UI_MODE_NIGHT_YES){
            logoISSS.setImageResource(R.drawable.ic_modo_oscuro_logo)
        }else{
            logoISSS.setImageResource(R.drawable.id_logo_isss)
        }


    }
}