package RecyclerViewHelpers

import Modelo.ChatsDoctores
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import diana.padilla.isss_salud.R
import diana.padilla.isss_salud.activity_bandeja_chat

class AdaptadorDocBusqueda(var Datos: List<ChatsDoctores>): RecyclerView.Adapter<ViewHolderDocBusqueda>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDocBusqueda {
        val vista =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_item_doctor, parent, false)
        return ViewHolderDocBusqueda(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolderDocBusqueda, position: Int) {
        val item = Datos[position]
        val context = holder.itemView.context
        holder.txtDoctorName.text = item.nombre_doctor

        if(item.foto_doctor_url.contains("C:") || item.foto_doctor_url.contains("\\")){
            Glide.with(holder.ImageDoctor.context)
                .load("https://i.pinimg.com/236x/2a/2e/7f/2a2e7f0f60b750dfb36c15c268d0118d.jpg")
                .into(holder.ImageDoctor)
        }else{
            Glide.with(holder.ImageDoctor.context)
                .load(item.foto_doctor_url)
                .error("https://i.pinimg.com/236x/2a/2e/7f/2a2e7f0f60b750dfb36c15c268d0118d.jpg")
                .into(holder.ImageDoctor)
        }

        holder.itemView.setOnClickListener {
            val pantallaBandejaChat = Intent(context, activity_bandeja_chat::class.java)

            pantallaBandejaChat.putExtra("doctor_image", item.foto_doctor_url)
            pantallaBandejaChat.putExtra("nombre_doctor", item.nombre_doctor)
            pantallaBandejaChat.putExtra("id_doctor", item.id_doctor)

            context.startActivity(pantallaBandejaChat)
        }
    }

}