package diana.padilla.isss_salud

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.util.Patterns


class activity_correo_para_codigo : AppCompatActivity() {

    companion object variablesGlobalesCorreoparacodigo{
        lateinit var codigoRecuperacion: String
        lateinit var correoRecu: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_correo_para_codigo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnCorreoRecuperacion = findViewById<Button>(R.id.btnEnvioCorreoRecuperacion)
        val txtCorreoRecuperacion = findViewById<EditText>(R.id.txtCorreoRecuperacion)

        btnCorreoRecuperacion.setOnClickListener {
            correoRecu = txtCorreoRecuperacion.text.toString()

            if (correoRecu.isEmpty()) {
                Toast.makeText(
                    this@activity_correo_para_codigo,
                    "Por favor, ingrese un correo",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(correoRecu)
                    .matches() || !correoRecu.endsWith("@gmail.com")
            ) {
                Toast.makeText(
                    this@activity_correo_para_codigo,
                    "Por favor, ingrese un correo válido (@gmail.com)",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                try {
                    CoroutineScope(Dispatchers.Main).launch {
                        codigoRecuperacion = (100000..999999).random().toString()
                        println("correo $correoRecu")
                        Toast.makeText(
                            this@activity_correo_para_codigo,
                            "Código de recuperación enviado a $correoRecu",
                            Toast.LENGTH_SHORT
                        ).show()
                        enviarCorreo(
                            correoRecu,
                            "Recuperación de contraseña",
                            "Su código de recuperación es: $codigoRecuperacion"
                        )
                    }
                    val pantallaCodigo = Intent(this, activity_codigo::class.java)
                    startActivity(pantallaCodigo)
                } catch (e: Exception) {
                    println("eeeeeeeeeerro $e")
                }
            }
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

        val txtCrearCuenta = findViewById<TextView>(R.id.txtCrearCuentaNueva)

        txtCrearCuenta.setOnClickListener {
            val pantallaRegistrarse = Intent(this, activity_registrarse::class.java)
            startActivity(pantallaRegistrarse)
        }
    }
}