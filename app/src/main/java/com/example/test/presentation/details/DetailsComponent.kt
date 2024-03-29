package com.example.test.presentation.details

import kotlinx.coroutines.flow.StateFlow

interface DetailsComponent {
    val model: StateFlow<DefaultDetailsComponent.DetailsState>

    fun onBackPressed()
    fun onClickUser(username:String)

}