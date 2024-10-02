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

class AdaptadorNoticias(var Datos: List<NoticiasNuevas>): RecyclerView.Adapter<ViewHolderNoticias>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNoticias {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_card_noticias, parent, false)
        return ViewHolderNoticias(vista)
    }

    override fun onBindViewHolder(holder: ViewHolderNoticias, position: Int) {
        val item = Datos[position]
        val context = holder.itemView.context
        Glide.with(holder.image.context)
            .load(item.imageURL)
            .into(holder.image)
        holder.title.text = item.title
        holder.description.text = item.description
        holder.date.text = item.fecha

        holder.itemView.setOnClickListener{
            val pantallaNoticiasCompletas = Intent(context, activity_Noticias_completas::class.java)
            pantallaNoticiasCompletas.putExtra("news_title", item.title)
            pantallaNoticiasCompletas.putExtra("news_description", item.description)
            pantallaNoticiasCompletas.putExtra("news_date", item.fecha)
            pantallaNoticiasCompletas.putExtra("news_image", item.imageURL)

            context.startActivity(pantallaNoticiasCompletas)
        }
    }

    override fun getItemCount(): Int {
        return Datos.size
    }
}