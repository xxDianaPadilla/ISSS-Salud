package RecyclerViewHelpers

import Modelo.NoticiasNuevas
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import diana.padilla.isss_salud.R
import diana.padilla.isss_salud.activity_Noticias_completas

class AdaptadorNoticias(private val context: Context, private val newsList: List<NoticiasNuevas>) :
    RecyclerView.Adapter<AdaptadorNoticias.NewsViewHolder>() {

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.TituloCard)
        val description = view.findViewById<TextView>(R.id.DescripcionCard)
        val date = view.findViewById<TextView>(R.id.FechaCard)
        val image = view.findViewById<ImageView>(R.id.ImagenCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_card_noticias, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]
        holder.title.text = news.title
        holder.description.text = news.description
        holder.date.text = news.date
        Glide.with(context).load(news.image).into(holder.image)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, activity_Noticias_completas::class.java).apply{
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