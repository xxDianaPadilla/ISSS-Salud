package RecyclerViewHelpers

import Modelo.ChatsDoctores
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import diana.padilla.isss_salud.R

class AdaptadorDocBusqueda(var Datos: List<ChatsDoctores>): RecyclerView.Adapter<ViewHolderDocBusqueda>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDocBusqueda {
        val vista =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_item_doctor, parent, false)
        return ViewHolderDocBusqueda(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolderDocBusqueda, position: Int) {
        val item = Datos[position]
        holder.txtDoctorName.text = item.nombre_doctor
        Glide.with(holder.ImageDoctor.context)
            .load(item.foto_doctor_url)
            .into(holder.ImageDoctor)
    }

}