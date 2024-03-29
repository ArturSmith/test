package com.example.test.data.mapper

import com.example.test.data.network.dto.UserDetailsDto
import com.example.test.data.network.dto.UserDto
import com.example.test.domain.entity.User
import com.example.test.domain.entity.UserDetails

fun User.toDto() = UserDto(login, id, avatar_url)

fun UserDetails.toDto() =
    UserDetails(login, id, name, company, avatar, email, followers, following, date)

fun UserDto.toEntity() = User(login, id, avatar_url)

fun UserDetailsDto.toEntity() =
    UserDetails(login, id, name, company, avatar, email, followers, following, date)