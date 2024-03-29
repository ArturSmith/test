package com.example.test.data.network.dto

import com.google.gson.annotations.SerializedName

data class UserDetailsDto(
    @SerializedName("login")
    val login:String,
    @SerializedName("id")
    val id:Int,
    @SerializedName("name")
    val name:String,
    @SerializedName("company")
    val company:String,
    @SerializedName("avatar_url")
    val avatar:String,
    @SerializedName("email")
    val email:String?,
    @SerializedName("followers")
    val followers:Int,
    @SerializedName("following")
    val following:Int,
    @SerializedName("created_at")
    val date: String
)
