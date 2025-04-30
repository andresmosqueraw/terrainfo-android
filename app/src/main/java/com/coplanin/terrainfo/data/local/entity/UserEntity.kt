package com.coplanin.terrainfo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val dateJoined: String,
    val municipios: String,           // JSON string
    val permissions: String,          // JSON string
    val loginTimestamp: Long,
    val lat: Double,
    val lon: Double
)
