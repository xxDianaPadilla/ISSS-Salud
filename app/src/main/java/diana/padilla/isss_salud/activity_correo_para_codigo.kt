package diana.padilla.isss_salud

import Modelo.ClaseConexion
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
import kotlinx.coroutines.withContext
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException


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

        fun generarHTMLCorreo(codigoRecuperacion: String): String{
            return """
<html>
    <body style="font-family: Arial, sans-serif; 
                background-color: #f4f4f4; 
                margin: 0; 
                padding: 0;">
        <div class="container" style="width: 100%; 
                max-width: 600px; 
                margin: 0 auto; 
                background-color: #ffffff; 
                padding: 30px 20px; 
                border-radius: 8px; 
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);">
            
            <!-- Header -->
            <div class="header" style="text-align: center; 
                    margin-bottom: 30px; 
                    color: #0056b3;">
                <h1 style="font-size: 24px; 
                    font-weight: bold; 
                    margin: 0;">ISSS Salud</h1>
                <p style="margin: 5px 0 0; 
                    font-size: 16px; 
                    color: #666;">Recuperación de Contraseña</p>
            </div>
            
            <!-- Message -->
            <div class="message" style="text-align: center; 
                    color: #333; 
                    margin-bottom: 40px;">
                <p style="font-size: 16px;">Hola, has solicitado recuperar tu contraseña. Usa el siguiente código para restablecerla:</p>
                <div class="code" style="display: inline-block; 
                    padding: 15px 25px; 
                    font-size: 22px; 
                    font-weight: bold; 
                    color: #ffffff; 
                    background-color: #0056b3; 
                    border-radius: 5px; 
                    margin-top: 20px;">$codigoRecuperacion</div>
            </div>
            
            <!-- Info -->
            <div class="info" style="text-align: center; 
                    font-size: 14px; 
                    color: #666;">
                <p>Si no solicitaste este cambio, por favor ignora este correo.</p>
            </div>
            
            <!-- Footer -->
            <div class="footer" style="margin-top: 40px; 
                    text-align: center; 
                    font-size: 12px; 
                    color: #999;">
                <p>ISSS Salud &copy; 2024</p>
            </div>
        </div>
    </body>
    </html>
""".trimIndent()
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
                        val correoExiste = verificarCorreo(correoRecu)
                        if (correoExiste) {
                            codigoRecuperacion = (100000..999999).random().toString()
                            val htmlCorreo = generarHTMLCorreo(codigoRecuperacion)

                            enviarCorreo(correoRecu, "Recuperación de Contraseña", htmlCorreo)
                            println("correo $correoRecu")
                            Toast.makeText(
                                this@activity_correo_para_codigo,
                                "Código de recuperación enviado!",
                                Toast.LENGTH_SHORT
                            ).show()
                            txtCorreoRecuperacion.setText("")
                            val pantallaCodigo = Intent(this@activity_correo_para_codigo, activity_codigo::class.java)
                            startActivity(pantallaCodigo)
                        } else {
                            Toast.makeText(
                                this@activity_correo_para_codigo,
                                "Correo ingresado no existe en la base de datos",
                                Toast.LENGTH_SHORT
                            ).show()
                            txtCorreoRecuperacion.setText("")
                        }
                    }
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
            finish()
        }

        txtVolverInicioSesion.setOnClickListener {
            val pantallaInicioSesion2 = Intent(this, activity_ingreso::class.java)
            startActivity(pantallaInicioSesion2)
            finish()
        }

        val txtCrearCuenta = findViewById<TextView>(R.id.txtCrearCuentaNueva)

        txtCrearCuenta.setOnClickListener {
            val pantallaRegistrarse = Intent(this, activity_registrarse::class.java)
            startActivity(pantallaRegistrarse)
            finish()
        }
    }

    private suspend fun verificarCorreo(correo: String): Boolean{
        return withContext(Dispatchers.IO){
            var existe = false
            val objConexion = ClaseConexion().cadenaConexion()!!
            val statement: PreparedStatement?
            val resultSet: ResultSet?

            try{
                val query = "SELECT COUNT(*) FROM Usuarios WHERE correo_electronico = ?"
                statement = objConexion.prepareStatement(query)
                statement.setString(1, correo)

                resultSet = statement.executeQuery()
                if(resultSet.next()){
                    existe = resultSet.getInt(1) > 0
                }
            }catch (e: SQLException){
                e.printStackTrace()
            }
            existe
        }
    }
}