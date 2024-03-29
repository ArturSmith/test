package com.example.test.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.test.data.mapper.toEntity
import com.example.test.data.network.api.ApiService
import com.example.test.data.pagination.UserSource
import com.example.test.domain.entity.User
import com.example.test.domain.entity.UserDetails
import com.example.test.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val apiService: ApiService
) : UserRepository {


    override  fun getUsers() =
        Pager(
        config = PagingConfig(
            pageSize = DEFAULT_PER_PAGE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { UserSource(apiService, DEFAULT_PER_PAGE) }
    ).flow

    override suspend fun getDetails(username: String): Result<UserDetails> {
        return withContext(Dispatchers.IO) {
            try {
                val user = apiService.getDetails(username).toEntity()
                Result.success(user)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }



    private companion object {
        const val DEFAULT_PER_PAGE = 50
    }
}