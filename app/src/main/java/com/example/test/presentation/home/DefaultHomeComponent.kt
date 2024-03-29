package com.example.test.presentation.home

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.push
import com.example.test.domain.entity.User
import com.example.test.domain.usecase.GetUsersUseCase
import com.example.test.extensions.componentScope
import com.example.test.presentation.root.DefaultRootComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.getScopeId


class DefaultHomeComponent(
    componentContext: ComponentContext,
    private val navigation: StackNavigation<DefaultRootComponent.Config>,
    getUsersUseCase: GetUsersUseCase
) : HomeComponent, ComponentContext by componentContext {

    sealed interface HomeScreenState {
        data object Loading : HomeScreenState
        data class Initial(
            val users: Flow<PagingData<User>>
        ) : HomeScreenState

        data class Error(val message: String) : HomeScreenState
    }

    private val users = getUsersUseCase()
        .cachedIn(componentScope())

    init {
        loadUsers()
    }

    private val _model = MutableStateFlow<HomeScreenState>(
        HomeScreenState.Loading
    )
    override val model: StateFlow<HomeScreenState> = _model.asStateFlow()

    private fun loadUsers() {
        componentScope().launch {
            delay(3000)
            when (val currentState = _model.value) {
                is HomeScreenState.Initial -> {
                    _model.emit(currentState.copy(users))
                }

                else -> {
                    _model.emit(HomeScreenState.Initial(users))
                }
            }
        }
    }


    override fun onClickUser(username: String) {
        navigation.push(DefaultRootComponent.Config.Details(username))
    }
}