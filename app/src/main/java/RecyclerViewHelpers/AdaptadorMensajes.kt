package RecyclerViewHelpers

import Modelo.Mensajes
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import diana.padilla.isss_salud.R

class AdaptadorMensajes(private val listaMensajes: List<Mensajes>, private val idUsuario: Int): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_PACIENTE = 1
    private val VIEW_TYPE_DOCTOR = 2

    override fun getItemViewType(position: Int): Int {
        val mensaje = listaMensajes[position]
        return if(mensaje.idRemitente == idUsuario && mensaje.tipoRemitente == "PACIENTE"){
            VIEW_TYPE_PACIENTE
        }else{
            VIEW_TYPE_DOCTOR
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == VIEW_TYPE_PACIENTE){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_card_mensajes, parent, false)
            ViewHolderMensajes(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_card_mensajes_doc, parent, false)
            ViewHolderMensajesDoc(view)
        }
    }

    override fun getItemCount(): Int = listaMensajes.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mensaje = listaMensajes[position]
        if(holder is ViewHolderMensajes){
            holder.mensajePaciente.text = mensaje.mensaje
        }else if (holder is ViewHolderMensajesDoc){
            holder.mensajeDoctor.text = mensaje.mensaje
        }
    }
}