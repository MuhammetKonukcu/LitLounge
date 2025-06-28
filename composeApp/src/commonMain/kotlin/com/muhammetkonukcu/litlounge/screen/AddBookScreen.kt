package com.muhammetkonukcu.litlounge.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.muhammetkonukcu.litlounge.viewmodel.AddBookViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun AddBookScreen(navController: NavController, innerPadding: PaddingValues) {
    val viewModel = koinViewModel<AddBookViewModel>()
    val uiState = viewModel.uiState.collectAsState()
    Scaffold(modifier = Modifier.padding(innerPadding)) {

    }
}