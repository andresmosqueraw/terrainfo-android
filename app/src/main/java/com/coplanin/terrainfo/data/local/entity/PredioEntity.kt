package com.coplanin.terrainfo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "predioentity")
data class PredioEntity(
    @PrimaryKey val numeroPredial: String,
    val codigoOrip: String?,
    val matricula: String?,
    val areaTerreno: String?,
    val tipo: String?,
    val condicion: String?,
    val destino: String?,
    val areaRegistral: String?
)