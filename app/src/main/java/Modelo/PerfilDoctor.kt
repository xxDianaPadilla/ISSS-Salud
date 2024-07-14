package Modelo

data class PerfilDoctor(
    val id_doctor: Int,
    val foto_doctor_url: String,
    val correo_doctor: String,
    val nombre_doctor: String,
    val especialidad: String,
    val unidad_medica: String
)
