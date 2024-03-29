package com.example.test.presentation.details

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import com.example.test.R
import com.example.test.domain.entity.User
import com.example.test.domain.entity.UserDetails
import com.example.test.extensions.items
import com.example.test.extensions.shimmerEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsContent(component: DetailsComponent) {

    val state by component.model.collectAsState()
    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton({ component.onBackPressed() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                title = {},
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) {
        Box(Modifier.padding(it).fillMaxSize(), contentAlignment = Alignment.Center) {
            when (val currentState = state) {

                is DefaultDetailsComponent.DetailsState.Error -> {
                    Text("Something went wrong...")
                }

                is DefaultDetailsComponent.DetailsState.Initial -> {
                    Initial(currentState){component.onClickUser(it)}
                }

                DefaultDetailsComponent.DetailsState.Loading -> {
                    Shimmer()
                }
            }
        }
    }
}

@Composable
private fun Initial(
    state: DefaultDetailsComponent.DetailsState.Initial,
    onCLick: (username: String) -> Unit
) {

    val userDetails = state.user
    val users = state.users.collectAsLazyPagingItems()
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {
        item(span = { GridItemSpan(2) }) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 20.dp
                ),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(10.dp).fillMaxSize(),
                ) {
                    Header(userDetails)
                    Spacer(Modifier.height(15.dp))
                    Subtitle(userDetails)
                }
            }
        }
        items(users) {
            it?.let {
                UserItem(it) {onCLick(it)}
            }
        }
    }
}


@Composable
private fun UserItem(user: User, onCLick: (username: String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .clickable { onCLick(user.login) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .padding(3.dp)
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
        Spacer(Modifier.height(5.dp))
        Text(user.login)
    }
}

@Composable
private fun Header(userDetails: UserDetails) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .padding(3.dp)
                .size(100.dp),
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = userDetails.avatar,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
        Spacer(Modifier.width(10.dp))
        Column {
            Text(userDetails.date, color = Color.Gray, fontSize = 10.sp)
            if (userDetails.name.isEmpty()) {
                Text(userDetails.login, fontSize = 25.sp)
            } else {
                Text(userDetails.name, fontSize = 25.sp)
            }
            userDetails.email?.let { Text(it, color = Color.Gray) }
        }
    }
}

@Composable
private fun Subtitle(userDetails: UserDetails) {
    if (userDetails.company.isNotEmpty()) {
        Text("${stringResource(R.string.company)}: ${userDetails.company}")
        Spacer(Modifier.height(15.dp))
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("${stringResource(R.string.followers)}: ${userDetails.followers}", color = Color.Gray)
        Text("${stringResource(R.string.following)}: ${userDetails.following}", color = Color.Gray)
    }
}


@Composable
private fun Shimmer() {
    Column(
        modifier = Modifier.fillMaxSize().padding(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier.size(100.dp).clip(CircleShape).shimmerEffect()
            )
            Spacer(Modifier.width(10.dp))
            Column {
                Box(modifier = Modifier.height(15.dp).width(200.dp).shimmerEffect())
                Spacer(Modifier.height(5.dp))
                Box(modifier = Modifier.height(10.dp).width(150.dp).shimmerEffect())
                Spacer(Modifier.height(5.dp))
                Box(modifier = Modifier.height(5.dp).width(200.dp).shimmerEffect())
            }
        }
        Spacer(Modifier.height(20.dp))
        Box(modifier = Modifier.fillMaxWidth().height(20.dp).shimmerEffect())
        Spacer(Modifier.height(5.dp))
        Box(modifier = Modifier.width(300.dp).height(30.dp).shimmerEffect())
        Spacer(Modifier.height(10.dp))
        Box(modifier = Modifier.width(350.dp).height(15.dp).shimmerEffect())
        Spacer(Modifier.height(5.dp))
        Box(modifier = Modifier.fillMaxWidth().height(20.dp).shimmerEffect())
    }
}
