package com.muhammetkonukcu.litlounge

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.muhammetkonukcu.litlounge.theme.Blue500
import com.muhammetkonukcu.litlounge.theme.White

@Composable
fun ProfileScreen(navController: NavController, innerPadding: PaddingValues) {
    var nameValue by remember { mutableStateOf("") }
    var pageValue by remember { mutableIntStateOf(0) }
    var bookValue by remember { mutableIntStateOf(0) }
    var notificationValue by remember { mutableStateOf(false) }
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
                text = "Hello $nameValue",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            LabeledOutlinedTextField(
                label = "Name",
                value = nameValue,
                onValueChange = { nameValue = it },
                placeholder = "Name"
            )

            LabeledOutlinedTextField(
                label = "Daily page goal",
                value = pageValue.toString(),
                onValueChange = { pageValue = it.toInt() },
                placeholder = "Example 20"
            )

            LabeledOutlinedTextField(
                label = "Monthly book goal",
                value = bookValue.toString(),
                onValueChange = { bookValue = it.toInt() },
                placeholder = "Example 4"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Send me a notification",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Switch(
                    checked = notificationValue,
                    onCheckedChange = { notificationValue = it },
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
            modifier = Modifier.fillMaxWidth()
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