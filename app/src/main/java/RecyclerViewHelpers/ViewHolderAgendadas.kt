package RecyclerViewHelpers

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import diana.padilla.isss_salud.R

class ViewHolderAgendadas(view: View) : RecyclerView.ViewHolder(view) {
    val nombre: TextView = view.findViewById(R.id.PacienteNombre)
    val FechaCita: TextView = view.findViewById(R.id.FechaCita)
    val horaCita: TextView = view.findViewById(R.id.HoraCita)
    val doctorCita: TextView = view.findViewById(R.id.DoctorCita)
}