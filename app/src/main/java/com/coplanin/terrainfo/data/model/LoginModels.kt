package com.coplanin.terrainfo.data.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val username: String,
    val password: String,
    val municipio: String
)

data class LoginResponse(
    val token: String,
    val user: UserDTO            // <-- cámbialo
)

data class User(
    val id: Int,
    val username: String,
    val email: String
)

data class UserDTO(
    @SerializedName("id_usuario") val id_usuario: Int,
    @SerializedName("username") val username: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    val email: String,
    @SerializedName("date_joined") val dateJoined: String,
    @SerializedName("municipios_out") val municipios: List<String>,
    @SerializedName("permissions_out") val permissions: List<String>
)

data class CommonDataDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("activity_name") val activityName: String,
    @SerializedName("activity_code") val activityCode: String,
    @SerializedName("id_search") val idSearch: String,
    @SerializedName("address") val address: String,
    @SerializedName("city_code") val cityCode: String,
    @SerializedName("city_desc") val cityDesc: String,
    @SerializedName("capture_date") val captureDate: String, // Consider using a Date/Time object
    @SerializedName("capture_x") val captureX: Double,
    @SerializedName("capture_y") val captureY: Double,
    @SerializedName("event_user_name") val eventUserName: String,
    @SerializedName("create_date") val createDate: String, // Consider using a Date/Time object
    @SerializedName("create_user_name") val createUserName: String,
    @SerializedName("event_date") val eventDate: String, // Consider using a Date/Time object
    @SerializedName("event_x") val eventX: Double,
    @SerializedName("event_y") val eventY: Double,
    @SerializedName("last_edit_date") val lastEditDate: String?, // Nullable
    @SerializedName("last_edit_x") val lastEditX: Double?, // Nullable
    @SerializedName("last_edit_y") val lastEditY: Double?, // Nullable
    @SerializedName("last_edit_name") val lastEditName: String? // Nullable
)
