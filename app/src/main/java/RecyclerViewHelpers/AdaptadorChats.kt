package RecyclerViewHelpers

import Modelo.ChatsDoctores
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import diana.padilla.isss_salud.R
import diana.padilla.isss_salud.activity_perfil_doctor

class AdaptadorChats(var Datos: List<ChatsDoctores>): RecyclerView.Adapter<ViewHolderChats>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderChats {
        val vista =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_card_chats, parent, false)
        return ViewHolderChats(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolderChats, position: Int) {
        val item = Datos[position]
        val context = holder.itemView.context
        holder.txtDoctorNombre.text = item.nombre_doctor
        Glide.with(holder.ivDoctorFoto.context)
            .load(item.foto_doctor_url)
            .into(holder.ivDoctorFoto)

        holder.ivDoctorFoto.setOnClickListener {
            val pantallaPerfilDoctor = Intent(context, activity_perfil_doctor::class.java).apply {
            }
            context.startActivity(pantallaPerfilDoctor)
        }
    }

}