package RecyclerViewHelpers

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import diana.padilla.isss_salud.R

class ViewHolderChats(view: View): RecyclerView.ViewHolder(view) {

    val txtDoctorNombre = view.findViewById<TextView>(R.id.txtDoctorNombre)
    val ivDoctorFoto = view.findViewById<ImageView>(R.id.ImgDoctorPerfil)
}