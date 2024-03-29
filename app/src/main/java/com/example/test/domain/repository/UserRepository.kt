package com.example.test.domain.repository

import androidx.paging.PagingData
import com.example.test.data.network.dto.UserDto
import com.example.test.domain.entity.User
import com.example.test.domain.entity.UserDetails
import kotlinx.coroutines.flow.Flow

interface UserRepository {

     fun getUsers(): Flow<PagingData<User>>

    suspend fun getDetails(username: String): Result<UserDetails>
}