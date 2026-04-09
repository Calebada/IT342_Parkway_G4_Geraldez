package com.parkway.demo.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("userID")
    val userID: Long? = null,
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("user_id")
    val userId: Long? = null,
    @SerializedName("firstname")
    val firstname: String,
    @SerializedName("lastname")
    val lastname: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("role")
    val role: String? = null
) {
    fun getActualId(): Long? = userID ?: id ?: userId
}

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    @SerializedName("userID")
    val userID: Long? = null,
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("user_id")
    val userId: Long? = null,
    @SerializedName("firstname")
    val firstname: String,
    @SerializedName("lastname")
    val lastname: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("role")
    val role: String? = null,
    @SerializedName("message")
    val message: String? = null
) {
    fun getActualId(): Long? = userID ?: id ?: userId
}
