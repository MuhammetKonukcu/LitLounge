package com.muhammetkonukcu.litlounge.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.muhammetkonukcu.litlounge.room.entity.BookEntity
import com.muhammetkonukcu.litlounge.room.entity.PageEntity
import com.muhammetkonukcu.litlounge.theme.Blue500
import com.muhammetkonukcu.litlounge.theme.White
import com.muhammetkonukcu.litlounge.utils.PlatformImage
import com.muhammetkonukcu.litlounge.viewmodel.HomeViewModel
import kotlinx.datetime.LocalDate
import litlounge.composeapp.generated.resources.Res
import litlounge.composeapp.generated.resources.add_a_book
import litlounge.composeapp.generated.resources.congratulations_you_have_achieved_your_goal
import litlounge.composeapp.generated.resources.currently_reading
import litlounge.composeapp.generated.resources.hello_name
import litlounge.composeapp.generated.resources.monthly_book_goal
import litlounge.composeapp.generated.resources.my_friend
import litlounge.composeapp.generated.resources.what_are_you_reading_right_now
import litlounge.composeapp.generated.resources.x_books_left_to_reach_your_goal
import litlounge.composeapp.generated.resources.your_goal_this_month
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun HomeScreen(navController: NavController, innerPadding: PaddingValues) {
    val viewModel = koinViewModel<HomeViewModel>()
    val userData by viewModel.userData.collectAsState()
    val finishedBookCount by viewModel.finishedBooksCountFlow.collectAsState()
    val readingBooksPagingData = viewModel.readingBooksPagingDataFlow.collectAsLazyPagingItems()
    val pageTrackPagingData = viewModel.pageTrackPagingDataFlow.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.getUserData()
    }

    Scaffold(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(
                    Res.string.hello_name,
                    userData?.name ?: run {
                        stringResource(Res.string.my_friend)
                    }
                ),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            if (readingBooksPagingData.itemSnapshotList.isNotEmpty()) {
                Text(
                    text = stringResource(Res.string.currently_reading),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            ReadingBooksLazyColumn(
                pagingItems = readingBooksPagingData,
                navController = navController
            )

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
                        navController.navigate("AddBook/0") {
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

            if (userData?.monthlyBookGoal != null && userData?.monthlyBookGoal != 0) {
                finishedBookCount?.let { bookCount ->
                    MonthlyGoalColumn(
                        bookGoal = userData?.monthlyBookGoal ?: 0,
                        bookCount = bookCount
                    )
                }

                DashedDivider()
            }

            PagesBarChartWithLazyRow(
                data = pageTrackPagingData.itemSnapshotList.items,
                dailyGoal = userData?.dailyPageGoal ?: 0
            )
        }
    }
}

@Composable
private fun ReadingBooksLazyColumn(
    pagingItems: LazyPagingItems<BookEntity>,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            count = pagingItems.itemCount,
            key = { index -> pagingItems[index]?.id ?: 0 }) { index ->
            val historyItem = pagingItems[index]
            historyItem?.let {
                ReadingBookItem(
                    entity = it,
                    onClick = { id -> navController.navigate("BookDetail/$id") })
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
private fun PagesBarChartWithLazyRow(
    data: List<PageEntity>,
    dailyGoal: Int,
    modifier: Modifier = Modifier,
    chartHeight: Dp = 200.dp,
    labelHeight: Dp = 20.dp,
    spacerHeight: Dp = 4.dp,
    gridLines: Int = 5
) {
    if (data.isEmpty()) return

    val listState = rememberLazyListState()

    val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

    val lastVisibleDate = LocalDate.parse(data[lastVisibleItemIndex].dateStr)
    val monthYear = lastVisibleDate.month.name.lowercase().replaceFirstChar { it.titlecase() } +
            " ${lastVisibleDate.year}"

    val totalPages = data.filter { pageEntity ->
        val pageDate = LocalDate.parse(pageEntity.dateStr)
        pageDate.month == lastVisibleDate.month && pageDate.year == lastVisibleDate.year
    }.sumOf { it.pageCount }

    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        Text(
            text = "Pages in $monthYear",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = "$totalPages pages",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartHeight)
        ) {

            val cellHeight = chartHeight + labelHeight + spacerHeight
            val maxValue = data.maxOf { it.pageCount }.coerceAtLeast(dailyGoal)

            Column(Modifier.fillMaxSize().padding(bottom = labelHeight + spacerHeight)) {
                repeat(gridLines) { idx ->
                    val value = maxValue * (gridLines - idx) / gridLines
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = value.toString(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.width(32.dp)
                        )
                        DashedDivider()
                    }
                    Spacer(Modifier.weight(1f))
                }
            }

            LazyRow(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 32.dp),
                reverseLayout = true,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                items(data) { dp ->
                    val dayText = LocalDate.parse(dp.dateStr).dayOfMonth.toString()

                    BoxWithConstraints(
                        modifier = Modifier
                            .wrapContentWidth()
                            .widthIn(min = 24.dp)
                            .height(cellHeight),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        val totalPx = constraints.maxHeight.toFloat()
                        val density = LocalDensity.current
                        val labelPx = with(density) { labelHeight.toPx() }
                        val spacerPx = with(density) { spacerHeight.toPx() }
                        val chartAreaHeightPx = (totalPx - labelPx - spacerPx).coerceAtLeast(0f)

                        val fraction = dp.pageCount / maxValue.toFloat()
                        val barHeightPx = chartAreaHeightPx * fraction

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .height(with(density) { barHeightPx.toDp() })
                                    .width(24.dp)
                                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                    .background(
                                        if (dp.pageCount >= dailyGoal) Color(0xFF4CAF50)
                                        else Color(0xFFF44336)
                                    )
                            )
                            Spacer(Modifier.height(spacerHeight))
                            Text(
                                text = dayText,
                                style = MaterialTheme.typography.labelMedium,
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .height(labelHeight)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                overflow = TextOverflow.Visible
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthlyGoalColumn(bookGoal: Int, bookCount: Int) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = stringResource(Res.string.monthly_book_goal),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = stringResource(Res.string.your_goal_this_month, bookGoal),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        if (bookCount >= bookGoal) {
            Text(
                text = stringResource(Res.string.congratulations_you_have_achieved_your_goal),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            Text(
                text = stringResource(
                    Res.string.x_books_left_to_reach_your_goal,
                    bookGoal - bookCount
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun DashedDivider(
    color: Color = MaterialTheme.colorScheme.tertiary,
    thickness: Dp = 1.dp,
    dashLength: Dp = 4.dp,
    gapLength: Dp = 4.dp,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    val stroke = with(LocalDensity.current) { thickness.toPx() }
    val dash = with(LocalDensity.current) { dashLength.toPx() }
    val gap = with(LocalDensity.current) { gapLength.toPx() }

    Canvas(modifier = modifier.height(thickness)) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = stroke,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(dash, gap), 0f)
        )
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