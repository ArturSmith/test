package com.example.test.domain.entity

data class UserDetails(
    val login: String,
    val id: Int,
    val name: String,
    val company: String,
    val avatar: String,
    val email: String?,
    val followers: Int,
    val following: Int,
    val date: String
)
