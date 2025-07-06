package com.muhammetkonukcu.litlounge.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.muhammetkonukcu.litlounge.room.entity.BookEntity
import com.muhammetkonukcu.litlounge.theme.Blue500
import com.muhammetkonukcu.litlounge.theme.White
import com.muhammetkonukcu.litlounge.utils.PlatformImage
import com.muhammetkonukcu.litlounge.viewmodel.HomeViewModel
import litlounge.composeapp.generated.resources.Res
import litlounge.composeapp.generated.resources.add_a_book
import litlounge.composeapp.generated.resources.hello_name
import litlounge.composeapp.generated.resources.my_friend
import litlounge.composeapp.generated.resources.what_are_you_reading_right_now
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun HomeScreen(navController: NavController, innerPadding: PaddingValues) {
    val viewModel = koinViewModel<HomeViewModel>()
    val userData = viewModel.userData.collectAsState()
    val readingBooksPagingData = viewModel.readingBooksPagingDataFlow.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.getUserData()
    }

    Scaffold(modifier = Modifier.padding(innerPadding)) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(
                    Res.string.hello_name,
                    userData.value?.name ?: run {
                        stringResource(Res.string.my_friend)
                    }
                ),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            ReadingBooksLazyColumn(pagingItems = readingBooksPagingData)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.what_are_you_reading_right_now),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                TextButton(
                    colors = getButtonColors(),
                    onClick = {
                        navController.navigate("AddBook") {
                            launchSingleTop = true
                        }
                    }
                ) {
                    Text(
                        text = stringResource(Res.string.add_a_book),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun ReadingBooksLazyColumn(pagingItems: LazyPagingItems<BookEntity>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            count = pagingItems.itemCount,
            key = { index -> pagingItems[index]?.id ?: 0 }) { index ->
            val historyItem = pagingItems[index]
            historyItem?.let {
                ReadingBookItem(entity = it, onClick = {})
            }
        }
    }
}

@Composable
private fun ReadingBookItem(entity: BookEntity, onClick: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onBackground, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 12.dp)
            .clickable { onClick.invoke(entity.id) },
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        PlatformImage(
            imageURL = entity.imageURL,
            modifier = Modifier.size(140.dp, 140.dp).clip(RoundedCornerShape(8.dp))
        )

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = entity.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = entity.authorName,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            if (!entity.finished) {
                Text(
                    text = "${entity.currentPage} / ${entity.totalPage}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            val timeText = when {
                entity.finishTimestamp.isNotBlank() -> "${entity.startTimestamp} - ${entity.finishTimestamp}"
                else -> entity.startTimestamp
            }

            Text(
                text = timeText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun getButtonColors(): ButtonColors {
    val colors = ButtonDefaults.buttonColors().copy(
        containerColor = Blue500,
        contentColor = White,
        disabledContainerColor = Blue500.copy(alpha = 0.5f),
        disabledContentColor = White
    )
    return colors
}