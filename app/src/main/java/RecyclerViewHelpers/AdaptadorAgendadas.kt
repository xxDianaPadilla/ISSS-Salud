package RecyclerViewHelpers

import Modelo.CitasAgendadas
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import diana.padilla.isss_salud.R

class AdaptadorAgendadas(private val context: Context, private var newsList: List<CitasAgendadas>) :
    RecyclerView.Adapter<ViewHolderAgendadas>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAgendadas {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_card_agendadas, parent, false)
        return ViewHolderAgendadas(view)
    }

    override fun onBindViewHolder(holder: ViewHolderAgendadas, position: Int) {
        val agendadas = newsList[position]
        holder.nombre.text = agendadas.nomPaciente
        holder.descripcionCita.text = agendadas.descripcionCita
        Glide.with(context).load(agendadas.imgPaciente).into(holder.imgPaciente)
    }

    override fun getItemCount(): Int = newsList.size

    fun actualizarLista(nuevaLista: List<CitasAgendadas>) {
        newsList = nuevaLista
        notifyDataSetChanged()
    }
}