package RecyclerViewHelpers

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import diana.padilla.isss_salud.R

class ViewHolderDocBusqueda(view: View): RecyclerView.ViewHolder(view) {
    val ImageDoctor = view.findViewById<ImageView>(R.id.ImageDoctor)
    val txtDoctorName = view.findViewById<TextView>(R.id.txtDoctorName)
}