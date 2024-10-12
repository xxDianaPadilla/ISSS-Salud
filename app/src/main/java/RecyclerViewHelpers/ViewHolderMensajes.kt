package RecyclerViewHelpers

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import diana.padilla.isss_salud.R

class ViewHolderMensajes(view: View): RecyclerView.ViewHolder(view) {
   val mensajePaciente: TextView = itemView.findViewById(R.id.mensaje_paciente)
}