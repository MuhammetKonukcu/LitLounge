package com.muhammetkonukcu.litlounge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammetkonukcu.litlounge.model.BookDetailUiState
import com.muhammetkonukcu.litlounge.room.entity.BookEntity
import com.muhammetkonukcu.litlounge.room.entity.PageEntity
import com.muhammetkonukcu.litlounge.room.repository.BooksRepository
import com.muhammetkonukcu.litlounge.room.repository.PageTrackRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookDetailViewModel(
    private val booksRepository: BooksRepository,
    private val trackRepository: PageTrackRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(BookDetailUiState())
    val uiState: StateFlow<BookDetailUiState> = _uiState.asStateFlow()

    private val _bookDataFlow = MutableStateFlow(BookEntity())
    val bookDataFlow: StateFlow<BookEntity> = _bookDataFlow.asStateFlow()

    fun getBookDataFromDatabase(bookId: Int) {
        viewModelScope.launch {
            booksRepository.getBookById(bookId).collect { data ->
                data?.let {
                    _bookDataFlow.value = data
                }
            }
        }
    }

    fun onCurrentPageChange(new: String) {
        try {
            _uiState.update { it.copy(pageCount = new.toInt()) }
        } catch (e: Exception) {
            // Handle the exception
        }
    }

    fun saveBookToTheDatabase(trackEntity: PageEntity, bookEntity: BookEntity) {
        viewModelScope.launch {
            booksRepository.insertBook(
                entity = bookEntity.copy(currentPage = bookEntity.currentPage + trackEntity.pageCount)
            )
            trackRepository.insertOrUpdatePage(entity = trackEntity)
            clearUiState()
        }
    }

    fun clearUiState() {
        _uiState.value = BookDetailUiState()
    }
}