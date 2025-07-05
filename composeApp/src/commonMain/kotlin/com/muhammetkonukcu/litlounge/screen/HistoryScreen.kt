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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.cash.paging.compose.collectAsLazyPagingItems
import com.muhammetkonukcu.litlounge.room.entity.BookEntity
import com.muhammetkonukcu.litlounge.utils.PlatformImage
import com.muhammetkonukcu.litlounge.viewmodel.HistoryViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun HistoryScreen(navController: NavController, innerPadding: PaddingValues) {
    val viewModel = koinViewModel<HistoryViewModel>()
    val historyPagingItems = viewModel.historyPagingDataFlow.collectAsLazyPagingItems()

    Scaffold(modifier = Modifier.padding(innerPadding)) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                count = historyPagingItems.itemCount,
                key = { index -> historyPagingItems[index]?.id ?: 0 }) { index ->
                val historyItem = historyPagingItems[index]
                historyItem?.let {
                    HistoryItem(entity = it, onClick = {})
                }
            }
        }
    }
}

@Composable
private fun HistoryItem(
    entity: BookEntity,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
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