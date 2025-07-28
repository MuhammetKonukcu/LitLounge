package com.muhammetkonukcu.litlounge.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import app.cash.paging.compose.collectAsLazyPagingItems
import com.muhammetkonukcu.litlounge.room.entity.BookEntity
import com.muhammetkonukcu.litlounge.utils.PlatformImage
import com.muhammetkonukcu.litlounge.utils.ReadingFilter
import com.muhammetkonukcu.litlounge.viewmodel.HistoryViewModel
import litlounge.composeapp.generated.resources.Res
import litlounge.composeapp.generated.resources.delete
import litlounge.composeapp.generated.resources.edit
import litlounge.composeapp.generated.resources.my_library
import litlounge.composeapp.generated.resources.ph_caret_down
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class, ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController, innerPadding: PaddingValues) {
    val viewModel = koinViewModel<HistoryViewModel>()
    val historyPagingItems = viewModel.historyPagingDataFlow.collectAsLazyPagingItems()
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by remember { mutableStateOf(false) }
    var selectedBookId by remember { mutableStateOf(0) }
    var selectedBookName by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.padding(innerPadding),
        topBar = {
            HistoryTopAppBar(
                modifier = Modifier,
                onFilterSelected = { filter ->}
            )
        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(paddingValues),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                count = historyPagingItems.itemCount,
                key = { index -> historyPagingItems[index]?.id ?: 0 }) { index ->
                val historyItem = historyPagingItems[index]
                historyItem?.let { item ->
                    HistoryItem(
                        entity = item,
                        onClick = { navController.navigate("AddBook/${item.id}") },
                        onLongClick = {
                            selectedBookId = item.id
                            selectedBookName = item.name
                            isSheetOpen = true
                        }
                    )
                }
            }
        }
    }

    if (isSheetOpen) {
        HistoryBottomSheet(
            bookId = selectedBookId,
            bookName = selectedBookName,
            bottomSheetState = sheetState,
            onDismissRequest = { isSheetOpen = false },
            onEditClick = { bookId ->
                navController.navigate("AddBook/$bookId")
                isSheetOpen = false
            },
            onRemoveClick = { bookId ->
                viewModel.removeBookFromDatabase(bookId)
                isSheetOpen = false
            }
        )
    }
}

@Composable
private fun HistoryTopAppBar(
    modifier: Modifier = Modifier,
    onFilterSelected: (ReadingFilter) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf(ReadingFilter.ALL) }

    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(Res.string.my_library),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Box(modifier = modifier.wrapContentSize(Alignment.TopStart)) {
            Row(
                modifier = Modifier
                    .clickable { expanded = true }
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(selectedFilter.labelRes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(4.dp))
                Icon(
                    imageVector = vectorResource(Res.drawable.ph_caret_down),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                ReadingFilter.entries.forEach { filter ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(filter.labelRes),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        },
                        onClick = {
                            selectedFilter = filter
                            expanded = false
                            onFilterSelected(filter)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryItem(
    entity: BookEntity,
    onClick: (Int) -> Unit,
    onLongClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onBackground, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 12.dp)
            .clickable { onClick.invoke(entity.id) }
            .combinedClickable(
                onClick = { onClick.invoke(entity.id) },
                onLongClick = { onLongClick.invoke(entity.id) }
            ),
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HistoryBottomSheet(
    bookId: Int,
    bookName: String,
    bottomSheetState: SheetState,
    onEditClick: (Int) -> Unit,
    onRemoveClick: (Int) -> Unit,
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = bottomSheetState,
        dragHandle = { CustomDragHandle() },
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = bookName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    .clickable { onEditClick.invoke(bookId) },
                text = stringResource(Res.string.edit),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Start
            )

            Text(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    .clickable { onRemoveClick.invoke(bookId) },
                text = stringResource(Res.string.delete),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun CustomDragHandle(
    modifier: Modifier = Modifier,
    width: Dp = 32.dp,
    height: Dp = 4.dp,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    color: Color = MaterialTheme.colorScheme.tertiaryContainer,
) {
    Surface(
        modifier =
            modifier
                .padding(vertical = 12.dp)
                .semantics {
                    contentDescription = ""
                },
        color = color,
        shape = shape
    ) {
        Box(Modifier.size(width = width, height = height))
    }
}