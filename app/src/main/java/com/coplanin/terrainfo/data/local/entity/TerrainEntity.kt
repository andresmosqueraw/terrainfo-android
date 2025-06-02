package com.coplanin.terrainfo.data.local.entity

/**
 * Representa UNA sola fila de la tabla cr_terreno
 * (solo los campos que realmente necesitamos en UI).
 */
data class TerrainEntity(
    val idOperacionPredio: String?,   // columna id_operacion_predio
    val etiqueta: String?,             // columna etiqueta
    val ilcPredio: String?
)