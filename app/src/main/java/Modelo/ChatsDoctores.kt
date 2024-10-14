package Modelo

data class ChatsDoctores(
    val id_doctor: Int,
    val correo_doctor: String,
    val contrasena_doctor: String,
    val nombre_doctor: String,
    val foto_doctor_url: String,
    val especialidad: String,
    val unidad_medica: String,
    var tieneMensajesNuevos: Boolean = false
)
