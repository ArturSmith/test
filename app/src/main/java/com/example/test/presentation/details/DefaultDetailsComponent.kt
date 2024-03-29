package com.example.test.presentation.details

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.example.test.domain.entity.User
import com.example.test.domain.entity.UserDetails
import com.example.test.domain.usecase.GetDetailsUseCase
import com.example.test.domain.usecase.GetUsersUseCase
import com.example.test.extensions.componentScope
import com.example.test.presentation.root.DefaultRootComponent
import com.example.test.presentation.root.RootComponent
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DefaultDetailsComponent(
    componentContext: ComponentContext,
    private val navigation: StackNavigation<DefaultRootComponent.Config>,
    private val params: String,
    private val getDetailsUseCase: GetDetailsUseCase,
    getUsersUseCase: GetUsersUseCase
) : DetailsComponent, ComponentContext by componentContext {

    sealed class DetailsState {
        data object Loading : DetailsState()
        data class Initial(val user: UserDetails, val users: Flow<PagingData<User>>) :
            DetailsState()

        data class Error(val message: String) : DetailsState()
    }

    private val users = getUsersUseCase()
        .map {
            it.filter {
                it.login != params
            }
        }
        .cachedIn(componentScope())

    private val _model = MutableStateFlow<DetailsState>(DetailsState.Loading)
    override val model: StateFlow<DetailsState>
        get() = _model.asStateFlow()


    init {
        loadUser()
    }

    private fun loadUser() {
        componentScope().launch {
            async { getDetailsUseCase(params) }
                .await()
                .onSuccess {
                    // delay added to animate shimmer a bit longer
                    delay(1500)
                    _model.emit(
                        DetailsState.Initial(it, users)
                    )
                }
                .onFailure {
                    _model.emit(
                        DetailsState.Error(it.message.toString())
                    )
                }
        }
    }

    override fun onBackPressed() {
        navigation.pop()
    }

    @OptIn(ExperimentalDecomposeApi::class)
    override fun onClickUser(username: String) {
        navigation.pushNew(DefaultRootComponent.Config.Details(username))
    }
}


