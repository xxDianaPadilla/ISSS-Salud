package RecyclerViewHelpers

import Modelo.NoticiasNuevas
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import diana.padilla.isss_salud.R
import diana.padilla.isss_salud.activity_Noticias_completas

class AdaptadorNoticias(private val context: Context, private val newsList: List<NoticiasNuevas>) :
    RecyclerView.Adapter<ViewHolderNoticias>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNoticias {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_card_noticias, parent, false)
        return ViewHolderNoticias(view)
    }

    override fun onBindViewHolder(holder: ViewHolderNoticias, position: Int) {
        val news = newsList[position]
        holder.title.text = news.title
        holder.description.text = news.description
        holder.date.text = news.date
        Glide.with(context).load(news.image).into(holder.image)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, activity_Noticias_completas::class.java).apply {
                putExtra("news_title", news.title)
                putExtra("news_description", news.description)
                putExtra("news_date", news.date)
                putExtra("news_image", news.image)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = newsList.size
}