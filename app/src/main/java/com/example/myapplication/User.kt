package com.example.myapplication

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("users")
    val users: List<User>
)

data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("image")
    val image: String
)
