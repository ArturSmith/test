package com.example.test.data.network.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("login")
    val login: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("avatar_url")
    val avatar_url: String
)
