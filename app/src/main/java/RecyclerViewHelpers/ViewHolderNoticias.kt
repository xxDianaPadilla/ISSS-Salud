package RecyclerViewHelpers

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import diana.padilla.isss_salud.R

class ViewHolderNoticias(view: View) : RecyclerView.ViewHolder(view) {
    val image: ImageView = view.findViewById(R.id.ImagenCard)
    val title: TextView = view.findViewById(R.id.TituloCard)
    val description: TextView = view.findViewById(R.id.DescripcionCard)
    val date: TextView = view.findViewById(R.id.FechaCard)
}