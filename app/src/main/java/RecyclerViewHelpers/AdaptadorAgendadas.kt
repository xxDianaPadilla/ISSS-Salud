package RecyclerViewHelpers

import Modelo.CitasAgendadas
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import diana.padilla.isss_salud.R

class AdaptadorAgendadas(var Datos: List<CitasAgendadas>): RecyclerView.Adapter<ViewHolderAgendadas>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAgendadas {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_card_citas, parent, false)
        return ViewHolderAgendadas(vista)
    }

    override fun getItemCount(): Int = Datos.size

    override fun onBindViewHolder(holder: ViewHolderAgendadas, position: Int) {
        val agendadas = Datos[position]
        holder.nombre.text = "Paciente: ${agendadas.nomPaciente}"
        holder.FechaCita.text = "Fecha: ${agendadas.fechaCita}"
        holder.horaCita.text = "Hora: ${agendadas.horaCita}"
        holder.doctorCita.text = "Doctor: ${agendadas.doctorCita}"
    }
}