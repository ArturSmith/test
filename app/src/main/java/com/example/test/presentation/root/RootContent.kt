package com.example.test.presentation.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.FaultyDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import com.example.test.presentation.details.DetailsContent
import com.example.test.presentation.home.HomeContent

@OptIn(FaultyDecomposeApi::class)
@Composable
fun RootContent(component: RootComponent) {

    Children(
        stack = component.childStack,
        modifier = Modifier.fillMaxSize(),
        animation = stackAnimation { child, _, _ ->
            when (child.instance) {
                is RootComponent.Child.Home -> {
                    slide()
                }

                is RootComponent.Child.Details -> {
                    slide()
                }
            }
        }
    ) {
        when (val instance = it.instance) {
            is RootComponent.Child.Home -> {
                HomeContent(instance.component)
            }

            is RootComponent.Child.Details -> {
                DetailsContent(instance.component)
            }
        }
    }
}