package com.muhammetkonukcu.litlounge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kashif.imagesaverplugin.ImageSaverPlugin
import com.muhammetkonukcu.litlounge.model.AddBookUiState
import com.muhammetkonukcu.litlounge.room.entity.BookEntity
import com.muhammetkonukcu.litlounge.room.repository.BooksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AddBookViewModel(private val booksRepository: BooksRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(AddBookUiState())
    val uiState: StateFlow<AddBookUiState> = _uiState.asStateFlow()

    fun onNameChange(new: String) {
        _uiState.update { it.copy(name = new) }
    }

    fun onAuthorNameChange(new: String) {
        _uiState.update { it.copy(authorName = new) }
    }

    fun onTotalPageChange(new: String) {
        try {
            _uiState.update { it.copy(totalPage = new.toInt()) }
        } catch (e: Exception) {
            // Handle the exception
        }
    }

    fun onCurrentPageChange(new: String) {
        try {
            _uiState.update { it.copy(currentPage = new.toInt()) }
        } catch (e: Exception) {
            // Handle the exception
        }
    }

    fun onImageURLChange(new: String) {
        _uiState.update { it.copy(imageURL = new) }
    }

    fun onStartTimestampChange(new: LocalDate) {
        _uiState.update { it.copy(startTimestamp = new.toString()) }
    }

    fun onFinishTimestampChange(new: LocalDate) {
        _uiState.update { it.copy(finishTimestamp = new.toString()) }
    }

    fun onFinishedToggle(enabled: Boolean) {
        _uiState.update { it.copy(finished = enabled) }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun saveImage(imageSaverPlugin: ImageSaverPlugin, byteArray: ByteArray?) {
        if (byteArray == null) return

        val customName = "image_${Uuid.random().toHexString()}"
        viewModelScope.launch {
            imageSaverPlugin.saveImage(
                byteArray = byteArray,
                imageName = customName
            )?.let { path ->
                onImageURLChange(path)
            }
        }
    }

    fun saveBookToTheDatabase() {
        viewModelScope.launch {
            val finishedTimestampStr =
                if (uiState.value.finished) uiState.value.finishTimestamp else ""
            booksRepository.insertBook(
                BookEntity(
                    name = uiState.value.name,
                    authorName = uiState.value.authorName,
                    totalPage = uiState.value.totalPage,
                    currentPage = uiState.value.currentPage,
                    imageURL = uiState.value.imageURL,
                    startTimestamp = uiState.value.startTimestamp,
                    finishTimestamp = finishedTimestampStr,
                    finished = uiState.value.finished
                )
            )
            clearUiState()
        }
    }

    fun clearUiState() {
        _uiState.value = AddBookUiState()
    }
}