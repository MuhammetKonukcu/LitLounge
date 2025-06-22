package com.muhammetkonukcu.litlounge.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.muhammetkonukcu.litlounge.theme.Blue500
import com.muhammetkonukcu.litlounge.theme.White
import com.muhammetkonukcu.litlounge.viewmodel.ProfileViewModel
import litlounge.composeapp.generated.resources.Res
import litlounge.composeapp.generated.resources.daily_page_goal
import litlounge.composeapp.generated.resources.daily_page_goal_hint
import litlounge.composeapp.generated.resources.hello_name
import litlounge.composeapp.generated.resources.monthly_book_goal
import litlounge.composeapp.generated.resources.monthly_book_goal_hint
import litlounge.composeapp.generated.resources.my_friend
import litlounge.composeapp.generated.resources.name
import litlounge.composeapp.generated.resources.name_hint
import litlounge.composeapp.generated.resources.send_me_a_notification
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ProfileScreen(navController: NavController, innerPadding: PaddingValues) {
    val viewModel = koinViewModel<ProfileViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    val scrollState = rememberScrollState()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp).verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(
                    Res.string.hello_name,
                    if (uiState.name.isNotBlank()) uiState.name else stringResource(Res.string.my_friend)
                ),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            LabeledOutlinedTextField(
                label = stringResource(Res.string.name),
                value = uiState.name,
                onValueChange = viewModel::onNameChange,
                placeholder = stringResource(Res.string.name_hint),
            )

            LabeledOutlinedTextField(
                label = stringResource(Res.string.daily_page_goal),
                value = uiState.dailyPageGoal.toString(),
                onValueChange = viewModel::onDailyPageGoalChange,
                placeholder = stringResource(Res.string.daily_page_goal_hint),
                keyboardType = KeyboardType.Number
            )

            LabeledOutlinedTextField(
                label = stringResource(Res.string.monthly_book_goal),
                value = uiState.monthlyBookGoal.toString(),
                onValueChange = viewModel::onMonthlyBookGoalChange,
                placeholder = stringResource(Res.string.monthly_book_goal_hint),
                keyboardType = KeyboardType.Number
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(Res.string.send_me_a_notification),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Switch(
                    checked = uiState.sendNotification,
                    onCheckedChange = viewModel::onSendNotificationToggle,
                    colors = GetSwitchColors()
                )
            }
        }
    }
}

@Composable
private fun LabeledOutlinedTextField(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
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
            textStyle = MaterialTheme.typography.bodyMedium,
            singleLine = true,
            colors = GetTextFieldColors(),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            )
        )
    }
}

@Composable
private fun GetTextFieldColors(): TextFieldColors {
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
private fun GetSwitchColors(): SwitchColors {
    val colors = SwitchDefaults.colors().copy(
        checkedThumbColor = White,
        uncheckedThumbColor = MaterialTheme.colorScheme.tertiary,
        checkedTrackColor = Blue500,
        uncheckedTrackColor = MaterialTheme.colorScheme.background,
        checkedBorderColor = Blue500,
        uncheckedBorderColor = MaterialTheme.colorScheme.tertiary
    )
    return colors
}