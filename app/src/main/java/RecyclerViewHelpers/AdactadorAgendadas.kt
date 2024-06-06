package RecyclerViewHelpers

import Modelo.CitasAgendadas
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import diana.padilla.isss_salud.R

class AdaptadorAgendadas(private val context: Context, private val newsList: List<CitasAgendadas>) :
    RecyclerView.Adapter<AdaptadorAgendadas.AgendadasViewHolder>() {

    class AgendadasViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val Nombre = view.findViewById<TextView>(R.id.PacienteNombre)
        val DescripcionCita = view.findViewById<TextView>(R.id.CitaDescripcion)
        val ImgPaciente = view.findViewById<ImageView>(R.id.ImgCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgendadasViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_card_agendadas, parent, false)
        return AgendadasViewHolder(view)
    }

    override fun onBindViewHolder(holder: AgendadasViewHolder, position: Int) {
        val Agendadas = newsList[position]
        holder.Nombre.text = Agendadas.nomPaciente
        holder.DescripcionCita.text = Agendadas.descripcionCita
        Glide.with(context).load(Agendadas.imgPaciente).into(holder.ImgPaciente)
    }
    override fun getItemCount(): Int = newsList.size
}