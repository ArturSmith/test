package com.example.test.presentation.home

import kotlinx.coroutines.flow.StateFlow

interface HomeComponent {
    val model: StateFlow<DefaultHomeComponent.HomeScreenState>

    fun onClickUser(username:String)

}