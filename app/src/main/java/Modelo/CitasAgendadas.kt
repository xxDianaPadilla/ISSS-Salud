package Modelo

data class CitasAgendadas (
    val idCita: Int,
    val fechaCita: String,
    val horaCita: String,
    val nomPaciente: String,
    val doctorCita: String
)