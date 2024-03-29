package com.example.test.data.pagination

import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.test.data.mapper.toEntity
import com.example.test.data.network.api.ApiService
import com.example.test.data.network.dto.UserDto
import com.example.test.domain.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.math.max

class UserSource(
    private val apiService: ApiService,
    private val perPage: Int
) : PagingSource<Int, User>() {

    private fun ensureValidKey(key: Int) = max(STARTING_KEY, key)

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val user = state.closestItemToPosition(anchorPosition) ?: return null
        return ensureValidKey(key = user.id - (state.config.pageSize / 2))
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return withContext(Dispatchers.IO) {
            try {
                val nextPage = params.key ?: STARTING_KEY
                val userList =
                    apiService.getUsers(page = nextPage, perPage = perPage).map { it.toEntity() }

                LoadResult.Page(
                    data = userList,
                    prevKey = if (nextPage == STARTING_KEY) null else nextPage - 1,
                    nextKey = if (userList.isEmpty()) { null } else { nextPage + userList.last().id }
                )

            } catch (exception: IOException) {
                LoadResult.Error(exception)
            } catch (exception: HttpException) {
                LoadResult.Error(exception)
            }
        }
    }

    private companion object {
        const val STARTING_KEY = 0
    }
}