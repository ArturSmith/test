package com.example.test.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.SubcomposeAsyncImage
import com.example.test.domain.entity.User
import com.example.test.extensions.shimmerEffect
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun HomeContent(component: HomeComponent) {
    val state by component.model.collectAsState()
    Scaffold {
        Box(Modifier.fillMaxSize().padding(it), contentAlignment = Alignment.Center) {
            when (val currentState = state) {
                is DefaultHomeComponent.HomeScreenState.Error -> {
                    Error(currentState)
                }

                is DefaultHomeComponent.HomeScreenState.Initial -> {
                    Initial(
                        state = currentState,
                        onClickUser = { component.onClickUser(it) }
                    )
                }

                DefaultHomeComponent.HomeScreenState.Loading -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(10) {
                            Shimmer()
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun Error(state: DefaultHomeComponent.HomeScreenState.Error) {
    Text(state.message, color = Color.Red)
}

@Composable
private fun Initial(
    state: DefaultHomeComponent.HomeScreenState.Initial,
    onClickUser: (username: String) -> Unit
) {
    val users = state.users.collectAsLazyPagingItems()
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(users) {
        snapshotFlow { users.loadState.refresh }
            .collect {
                isRefreshing = it == LoadState.Loading
            }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = {
            if (!isRefreshing) {
                users.refresh()
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Spacer(Modifier.height(10.dp))
            }
            items(users) {
                it?.let {
                    UserCard(it) { onClickUser(it) }
                }
            }

            item {
                Spacer(Modifier.height(10.dp))
            }
        }
    }

}

@Composable
private fun UserCard(user: User, onClickUser: (username: String) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onClickUser(user.login) }
    ) {
        Card(
            modifier = Modifier
                .size(100.dp),
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = user.avatar_url,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
        Spacer(Modifier.width(10.dp))
        Column {
            Text(text = user.login, fontWeight = FontWeight.Bold)
            Text(text = user.id.toString(), color = Color.Gray, fontSize = 13.sp)
        }
    }
}

@Composable
private fun Shimmer() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(3.dp)
                .size(100.dp)
                .clip(CircleShape)
                .shimmerEffect()
        ) {
        }
        Spacer(Modifier.width(10.dp))
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Box(modifier = Modifier.fillMaxWidth().height(30.dp).shimmerEffect())
            Box(modifier = Modifier.width(200.dp).height(20.dp).shimmerEffect())
        }
    }
}