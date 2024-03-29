package com.example.test.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.example.test.presentation.details.DetailsComponent
import com.example.test.presentation.home.HomeComponent
import kotlinx.android.parcel.Parcelize
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class DefaultRootComponent(
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext, KoinComponent {

    private val navigation = StackNavigation<Config>()

    override val childStack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Home,
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child {
        return when (config) {
            Config.Home -> {
                val homeComponent: HomeComponent by inject {
                    parametersOf(componentContext, navigation)
                }
                RootComponent.Child.Home(homeComponent)
            }

            is Config.Details -> {
                val detailsComponent: DetailsComponent by inject {
                    parametersOf(componentContext, navigation, config.username)
                }
                RootComponent.Child.Details(detailsComponent)
            }
        }
    }


    @Parcelize
    sealed interface Config : Parcelable {
        @Parcelize
        data object Home : Config

        @Parcelize
        data class Details(val username: String) : Config
    }


}