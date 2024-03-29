package com.example.test.domain.usecase

import com.example.test.domain.repository.UserRepository
import javax.inject.Inject

data class GetUsersUseCase (private val repository: UserRepository) {
     operator fun invoke() = repository.getUsers()
}
