package com.example.test.data.network.api

import com.example.test.data.network.dto.UserDetailsDto
import com.example.test.data.network.dto.UserDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET(USERS_PATH)
    suspend fun getUsers(
        @Query(PAGE_QUERY) page: Int,
        @Query(PER_PAGE_QUERY) perPage: Int
    ): List<UserDto>

    @GET("$USERS_PATH/{$USERNAME_PATH}")
    suspend fun getDetails(@Path(USERNAME_PATH) username: String): UserDetailsDto

    private companion object {
        const val USERS_PATH = "users"
        const val USERNAME_PATH = "username"
        const val PAGE_QUERY = "since"
        const val PER_PAGE_QUERY = "per_page"
    }
}