package com.example.test.domain.usecase

import com.example.test.domain.repository.UserRepository
import javax.inject.Inject

data class GetDetailsUseCase (private val repository: UserRepository) {
    suspend operator fun invoke(username: String) = repository.getDetails(username)
}
