package Modelo

data class Mensajes(
    val idRemitente: Int,
    val tipoRemitente: String,
    val idDestinatario: Int,
    val tipoDestinatario: String,
    val mensaje: String
)
