package com.coplanin.terrainfo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "commondata")
data class CommonDataEntity(
    @PrimaryKey val id: Int,
    val activityName: String,
    val activityCode: String,
    val address: String,
    val cityCode: String,
    val cityDesc: String,
    val captureDate: String,
    val captureX: String,
    val captureY: String,
    val eventUserName: String,
    val createDate: String,
    val createUserName: String,
    val eventDate: String,
    val eventX: Double,
    val eventY: Double
)