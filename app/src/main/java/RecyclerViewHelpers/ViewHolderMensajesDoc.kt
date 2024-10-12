package RecyclerViewHelpers

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import diana.padilla.isss_salud.R

class ViewHolderMensajesDoc(view: View): RecyclerView.ViewHolder(view) {
    val mensajeDoctor: TextView = itemView.findViewById(R.id.mensaje_doctor)
}