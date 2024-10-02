package diana.padilla.isss_salud

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

class activity_Noticias_completas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_noticias_completas)


        val title = intent.getStringExtra("news_title")
        val description = intent.getStringExtra("news_description")
        val date = intent.getStringExtra("news_date")
        val image = intent.getStringExtra("news_image")

        val titleView: TextView = findViewById(R.id.detail_news_title)
        val descriptionView: TextView = findViewById(R.id.detail_news_description)
        val dateView: TextView = findViewById(R.id.detail_news_date)
        val backButton: ImageView = findViewById(R.id.btnBack)
        val imageView: ImageView = findViewById(R.id.detail_news_image)

        val modoOscuro = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(modoOscuro == Configuration.UI_MODE_NIGHT_YES){
            backButton.setImageResource(R.drawable.ic_regresar_dm)
        }else{
            backButton.setImageResource(R.drawable.ic_regresar)
        }

        val logoIsssSmall = findViewById<ImageView>(R.id.ivSmallLogo)

        if(modoOscuro == Configuration.UI_MODE_NIGHT_YES){
            logoIsssSmall.setImageResource(R.drawable.ic_logo_isss_dark)
        }else{
            logoIsssSmall.setImageResource(R.drawable.ic_logo_isss_small)
        }

        titleView.text = title
        descriptionView.text = description
        dateView.text = "Fecha de publicación: " + date
        if (image != null) {
            Glide.with(this)
                .load(image)
                .into(imageView)
        }

        backButton.setOnClickListener {
          finish()
        }
    }
}