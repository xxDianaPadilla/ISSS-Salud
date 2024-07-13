package diana.padilla.isss_salud

import android.widget.Toast
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        fun esSoloUnDigito(input: String): Boolean{
            return input.length == 1 && input[0].isDigit()
        }

        val textWatchers = listOf(num1, num2, num3, num4, num5, num6).map { editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (!esSoloUnDigito(s.toString())) {
                        editText.error = "Debe ingresar solo un número"
                    }
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }

        btnConfirmarCodigo.setOnClickListener {

            val numero1 = num1.text.toString()
            val numero2 = num2.text.toString()
            val numero3 = num3.text.toString()
            val numero4 = num4.text.toString()
            val numero5 = num5.text.toString()
            val numero6 = num6.text.toString()

            val codigoRecu = "${num1.text.toString()}${num2.text.toString()}${num3.text.toString()}${num4.text.toString()}${num5.text.toString()}${num6.text.toString()}"
            println("Codigo correcto $codigoRecu $codigoQueEnvie")
            if (numero1.isEmpty() || numero2.isEmpty() || numero3.isEmpty() || numero4.isEmpty() || numero5.isEmpty() || numero6.isEmpty()){
                Toast.makeText(
                    this@activity_codigo,
                    "Error, para cambiar la contraseña debe llenar todas las casillas.",
                    Toast.LENGTH_SHORT
                ).show()
            } else if(!esSoloUnDigito(numero1) || !esSoloUnDigito(numero2) || !esSoloUnDigito(numero3) || !esSoloUnDigito(numero4) || !esSoloUnDigito(numero5) || !esSoloUnDigito(numero6)){
                Toast.makeText(this@activity_codigo, "Debe ingresar solo un número en cada casilla", Toast.LENGTH_SHORT).show()
            }else if (codigoRecu == codigoQueEnvie){
            val PantallaCambioContrasena = Intent(this, activity_cambio_contrasena::class.java)
            startActivity(PantallaCambioContrasena)
            finish()
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