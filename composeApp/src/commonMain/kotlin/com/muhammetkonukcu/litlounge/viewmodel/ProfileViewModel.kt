package com.muhammetkonukcu.litlounge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammetkonukcu.litlounge.model.ProfileUiState
import com.muhammetkonukcu.litlounge.room.entity.UserEntity
import com.muhammetkonukcu.litlounge.room.repository.UsersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(private val usersRepository: UsersRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val user = usersRepository.getUser()
            user?.let {
                _uiState.update {
                    it.copy(
                        name = user.name,
                        dailyPageGoal = user.dailyPageGoal,
                        monthlyBookGoal = user.monthlyBookGoal,
                        sendNotification = user.sendNotification
                    )
                }
            }
        }
    }

    fun onNameChange(new: String) {
        _uiState.update { it.copy(name = new) }
        updateUserFromDatabase()
    }

    fun onDailyPageGoalChange(new: String) {
        try {
            _uiState.update { it.copy(dailyPageGoal = new.toInt()) }
            updateUserFromDatabase()
        } catch (e: Exception) {
            // Handle the exception
        }
    }

    fun onMonthlyBookGoalChange(new: String) {
        try {
            _uiState.update { it.copy(monthlyBookGoal = new.toInt()) }
            updateUserFromDatabase()
        } catch (e: Exception) {
            // Handle the exception
        }
    }

    fun onSendNotificationToggle(enabled: Boolean) {
        _uiState.update { it.copy(sendNotification = enabled) }
        updateUserFromDatabase()
    }

    private fun updateUserFromDatabase() {
        viewModelScope.launch {
            usersRepository.insertQuery(
                entity = UserEntity(
                    name = uiState.value.name.trim(),
                    dailyPageGoal = uiState.value.dailyPageGoal,
                    monthlyBookGoal = uiState.value.monthlyBookGoal,
                    sendNotification = uiState.value.sendNotification
                )
            )
        }
    }
}