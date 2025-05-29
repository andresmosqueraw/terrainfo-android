package com.coplanin.terrainfo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa UNA sola fila de la tabla cr_terreno
 * (solo los campos que realmente necesitamos en UI).
 */
@Entity(tableName = "terrainentity")
data class TerrainEntity(
    @PrimaryKey val idOperacionPredio: String,
    val etiqueta: String?
)
