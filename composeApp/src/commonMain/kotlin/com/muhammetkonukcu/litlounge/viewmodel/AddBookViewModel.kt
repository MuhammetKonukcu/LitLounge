package com.muhammetkonukcu.litlounge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kashif.imagesaverplugin.ImageSaverPlugin
import com.muhammetkonukcu.litlounge.model.AddBookUiState
import com.muhammetkonukcu.litlounge.room.entity.BookEntity
import com.muhammetkonukcu.litlounge.room.repository.BooksRepository
import com.muhammetkonukcu.litlounge.utils.ImageUrlStatus
import com.muhammetkonukcu.litlounge.utils.ValidateImageUrlUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlin.coroutines.cancellation.CancellationException
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AddBookViewModel(
    private val booksRepository: BooksRepository,
    private val validateImageUrlUseCase: ValidateImageUrlUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddBookUiState())
    val uiState: StateFlow<AddBookUiState> = _uiState.asStateFlow()

    private var urlValidationJob: Job? = null

    fun getBookById(bookId: Int) {
        viewModelScope.launch {
            booksRepository.getBookById(bookId).collect { book ->
                book ?: return@collect
                _uiState.update {
                    it.copy(
                        id = book.id,
                        name = book.name,
                        imageURL = book.imageURL,
                        finished = book.finished,
                        totalPage = book.totalPage,
                        authorName = book.authorName,
                        currentPage = book.currentPage,
                        startTimestamp = book.startTimestamp,
                        finishTimestamp = book.finishTimestamp
                    )
                }
            }
        }
    }

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

    fun onImageURLChange(new: String, isUrl: Boolean = false) {
        _uiState.update { it.copy(imageURL = new, isNetworkImage = isUrl) }

        urlValidationJob?.cancel()

        urlValidationJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                // Loading state'i göster
                if (new.isNotBlank()) {
                    withContext(Dispatchers.Main) {
                        _uiState.update { it.copy(imageUrlStatus = ImageUrlStatus.VALIDATING) }
                    }

                    delay(800) // Debouncing

                    if (isActive) {
                        val status = validateImageUrlUseCase(new)
                        withContext(Dispatchers.Main) {
                            _uiState.update { it.copy(imageUrlStatus = status) }
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _uiState.update { it.copy(imageUrlStatus = ImageUrlStatus.IDLE) }
                    }
                }
            } catch (e: CancellationException) {
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _uiState.update { it.copy(imageUrlStatus = ImageUrlStatus.INVALID) }
                }
            }
        }
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

    fun onImageSaving(value: Boolean) {
        _uiState.update { it.copy(isImageSaving = value) }
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
                    id = uiState.value.id,
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

    fun deleteBookFromTheDatabase(bookId: Int?) {
        viewModelScope.launch {
            if (bookId == null) return@launch
            booksRepository.deleteBook(bookId)
            clearUiState()
        }
    }

    fun clearUiState() {
        _uiState.value = AddBookUiState()
    }

    override fun onCleared() {
        super.onCleared()
        urlValidationJob?.cancel()
    }
}