package com.example.test.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.arkivanov.decompose.defaultComponentContext
import com.example.test.data.network.api.ApiFactory
import com.example.test.presentation.home.HomeContent
import com.example.test.presentation.root.RootComponent
import com.example.test.presentation.root.RootContent
import com.example.test.presentation.ui.theme.TestTheme
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity(), KoinComponent {

    private lateinit var rootComponent: RootComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootComponent = get { parametersOf(defaultComponentContext()) }
        setContent {
            TestTheme {
                RootContent(rootComponent)
            }
        }
    }
}

