package Modelo

import java.sql.Date

data class ExpedienteMedico(
    val nombreUsuario: String,
    val dui: String,
    val sexo: String,
    val edad: String,
    val tipoSangre: String,
    val telefono: String,
    val correoElectronico: String,
    val antecedentesFamiliares: String,
    val problemasSaludPreexistentes: String,
    val alergias: String,
    val saludActual: String,
    val resultadosExamenesLaboratorio: String,
    val fichaIngreso: String
)
