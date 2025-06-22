package com.muhammetkonukcu.litlounge.viewmodel

import androidx.lifecycle.ViewModel
import com.muhammetkonukcu.litlounge.model.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun onNameChange(new: String) {
        _uiState.update { it.copy(name = new) }
    }

    fun onDailyPageGoalChange(new: String) {
        try {
            _uiState.update { it.copy(dailyPageGoal = new.toInt()) }
        } catch (e: Exception) {
            // Handle the exception
        }
    }

    fun onMonthlyBookGoalChange(new: String) {
        try {
            _uiState.update { it.copy(monthlyBookGoal = new.toInt()) }
        } catch (e: Exception) {
            // Handle the exception
        }
    }

    fun onSendNotificationToggle(enabled: Boolean) {
        _uiState.update { it.copy(sendNotification = enabled) }
    }
}