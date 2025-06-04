package com.coplanin.terrainfo.data.local.entity

data class PredioEntity(
    val codigoOrip: String?,
    val matriculaInmobiliaria: String?,
    val areaCatastralTerreno: String?,
    val numeroPredialNacional: String?,
    val tipo: String?,
    val condicionPredio: String?,
    val destinacionEconomica: String?,
    val areaRegistral: String?,
    val tipoReferenciaFmiAntiguo: String?
)