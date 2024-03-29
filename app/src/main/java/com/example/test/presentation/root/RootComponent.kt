package com.example.test.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.test.presentation.details.DetailsComponent
import com.example.test.presentation.home.HomeComponent

interface RootComponent {
    val childStack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data class Home(val component: HomeComponent) : Child
        data class Details(val component: DetailsComponent): Child
    }
}