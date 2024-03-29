package com.example.test.di

import com.example.test.data.network.api.ApiFactory
import com.example.test.data.network.api.ApiService
import com.example.test.data.repository.UserRepositoryImpl
import com.example.test.domain.repository.UserRepository
import com.example.test.domain.usecase.GetDetailsUseCase
import com.example.test.domain.usecase.GetUsersUseCase
import com.example.test.presentation.details.DefaultDetailsComponent
import com.example.test.presentation.details.DetailsComponent
import com.example.test.presentation.home.DefaultHomeComponent
import com.example.test.presentation.home.HomeComponent
import com.example.test.presentation.root.DefaultRootComponent
import com.example.test.presentation.root.RootComponent
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {

    single<ApiService> { ApiFactory.apiService }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single { GetUsersUseCase(get()) }
    single { GetDetailsUseCase(get()) }
    factory<RootComponent> { params -> DefaultRootComponent(params.get()) }
    factory<HomeComponent> { params -> DefaultHomeComponent(params.get(), params.get(), get()) }
    factory<DetailsComponent> { params ->
        DefaultDetailsComponent(
            params.get(),
            params.get(),
            params.get(),
            get(),
            get()
        )
    }
}