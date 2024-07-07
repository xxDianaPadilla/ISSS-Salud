package RecyclerViewHelpers

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import diana.padilla.isss_salud.R

class ViewHolderAgendadas(view: View) : RecyclerView.ViewHolder(view) {
    val nombre: TextView = view.findViewById(R.id.txtViewNombrePaciente)
    val FechaCita: TextView = view.findViewById(R.id.txtViewDescripcionCita)
    val horaCita: TextView = view.findViewById(R.id.txtViewHoraCita)
    val doctorCita: TextView = view.findViewById(R.id.txtViewDoctorCita)
}