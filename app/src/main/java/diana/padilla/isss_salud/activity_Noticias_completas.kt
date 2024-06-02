package diana.padilla.isss_salud

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
        val backButton: Button = findViewById(R.id.button_back)
        val imageView: ImageView = findViewById(R.id.detail_news_image)

        titleView.text = title
        descriptionView.text = description
        dateView.text = date
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