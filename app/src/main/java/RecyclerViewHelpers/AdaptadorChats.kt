package RecyclerViewHelpers

import Modelo.ChatsDoctores
import android.content.Intent
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import diana.padilla.isss_salud.R
import diana.padilla.isss_salud.activity_bandeja_chat
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

        if(item.foto_doctor_url.contains("C:") || item.foto_doctor_url.contains("\\")){
            Glide.with(holder.ivDoctorFoto.context)
                .load("https://i.pinimg.com/236x/2a/2e/7f/2a2e7f0f60b750dfb36c15c268d0118d.jpg")
                .into(holder.ivDoctorFoto)
        }else {

            Glide.with(holder.ivDoctorFoto.context)
                .load(item.foto_doctor_url)
                .error("https://i.pinimg.com/236x/2a/2e/7f/2a2e7f0f60b750dfb36c15c268d0118d.jpg")
                .into(holder.ivDoctorFoto)
        }

        holder.ivDoctorFoto.setOnClickListener {
            val pantallaPerfilDoctor = Intent(context, activity_perfil_doctor::class.java).apply {
                putExtra("foto_doctor", item.foto_doctor_url)
            }
            context.startActivity(pantallaPerfilDoctor)
        }

        holder.itemView.setOnClickListener{

            val pantallaBandejaChat = Intent(context, activity_bandeja_chat::class.java)
            pantallaBandejaChat.putExtra("doctor_image", item.foto_doctor_url)
            pantallaBandejaChat.putExtra("nombre_doctor", item.nombre_doctor)
            pantallaBandejaChat.putExtra("id_doctor", item.id_doctor)

            context.startActivity(pantallaBandejaChat)
        }
    }
}