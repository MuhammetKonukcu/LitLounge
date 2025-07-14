package com.muhammetkonukcu.litlounge.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.muhammetkonukcu.litlounge.room.entity.PageEntity
import com.muhammetkonukcu.litlounge.theme.Blue500
import com.muhammetkonukcu.litlounge.theme.White
import com.muhammetkonukcu.litlounge.viewmodel.BookDetailViewModel
import kotlinx.datetime.LocalDate
import litlounge.composeapp.generated.resources.Res
import litlounge.composeapp.generated.resources.back
import litlounge.composeapp.generated.resources.how_many_pages_did_you_read_today
import litlounge.composeapp.generated.resources.ph_arrow_left
import litlounge.composeapp.generated.resources.save
import network.chaintech.kmp_date_time_picker.utils.now
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun BookDetailScreen(bookId: Int, navController: NavController, innerPadding: PaddingValues) {
    val viewModel: BookDetailViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val bookData by viewModel.bookDataFlow.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.getBookDataFromDatabase(bookId)
    }

    Scaffold(
        modifier = Modifier.padding(innerPadding),
        topBar = {
            TopAppBar(
                bookName = bookData.name,
                navController = navController
            )
        },
        bottomBar = { BottomBar(viewModel = viewModel, navController = navController) }
    ) { contentPadding ->
        Column(
            Modifier
                .padding(horizontal = 12.dp, vertical = contentPadding.calculateTopPadding())
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BookTextField(
                Res.string.how_many_pages_did_you_read_today,
                uiState.pageCount.toString(),
                viewModel::onCurrentPageChange,
                keyboardType = KeyboardType.Number
            )
        }
    }
}

@Composable
private fun TopAppBar(
    bookName: String,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navController.navigateUp() }) {
            Icon(
                imageVector = vectorResource(Res.drawable.ph_arrow_left),
                contentDescription = stringResource(Res.string.back),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Text(
            text = bookName,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun BottomBar(
    viewModel: BookDetailViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val bookData by viewModel.bookDataFlow.collectAsState()
    val isSaveBtnEnabled = uiState.pageCount > 0
    Row(modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp)) {
        BottomButton(
            modifier = Modifier.weight(1f),
            label = stringResource(Res.string.save),
            colors = getSaveButtonColors(),
            isEnabled = isSaveBtnEnabled,
            onClick = {
                viewModel.saveBookToTheDatabase(
                    trackEntity = PageEntity(
                        pageCount = uiState.pageCount,
                        dateStr = LocalDate.now().toString()
                    ),
                    bookEntity = bookData
                )
                navController.navigateUp()
            }
        )
    }
}

@Composable
private inline fun BottomButton(
    modifier: Modifier,
    label: String,
    colors: ButtonColors,
    isEnabled: Boolean,
    crossinline onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        colors = colors,
        enabled = isEnabled,
        onClick = { onClick.invoke() }
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@Composable
private fun BookTextField(
    labelRes: StringResource,
    value: String,
    onValueChange: (String) -> Unit,
    placeholderRes: StringResource? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    LabeledOutlinedTextField(
        label = stringResource(labelRes),
        value = value,
        keyboardType = keyboardType,
        onValueChange = onValueChange,
        placeholder = placeholderRes?.let { stringResource(it) } ?: ""
    )
}

@Composable
private fun LabeledOutlinedTextField(
    label: String,
    value: String,
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.labelMedium
                )
            },
            readOnly = readOnly,
            textStyle = MaterialTheme.typography.bodyMedium,
            singleLine = true,
            colors = getTextFieldColors(),
            modifier = Modifier.fillMaxWidth().clickable {
                if (readOnly) {
                    onClick.invoke()
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            )
        )
    }
}

@Composable
private fun getTextFieldColors(): TextFieldColors {
    val colors = TextFieldDefaults.colors().copy(
        focusedTextColor = MaterialTheme.colorScheme.primary,
        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
        focusedPlaceholderColor = MaterialTheme.colorScheme.tertiary,
        focusedContainerColor = MaterialTheme.colorScheme.background,
        unfocusedTextColor = MaterialTheme.colorScheme.primary,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.tertiary,
        unfocusedContainerColor = MaterialTheme.colorScheme.background,
        errorContainerColor = MaterialTheme.colorScheme.background,
        disabledTextColor = MaterialTheme.colorScheme.primary,
        disabledIndicatorColor = MaterialTheme.colorScheme.tertiary,
        disabledContainerColor = MaterialTheme.colorScheme.background,
        disabledPlaceholderColor = MaterialTheme.colorScheme.tertiary,
    )
    return colors
}

@Composable
private fun getSaveButtonColors(): ButtonColors {
    val colors = ButtonDefaults.buttonColors().copy(
        containerColor = Blue500,
        contentColor = White,
        disabledContainerColor = Blue500.copy(alpha = 0.5f),
        disabledContentColor = White
    )
    return colors
}