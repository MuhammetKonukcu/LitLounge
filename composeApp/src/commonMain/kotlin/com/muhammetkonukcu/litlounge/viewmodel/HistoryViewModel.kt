package com.muhammetkonukcu.litlounge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import com.muhammetkonukcu.litlounge.room.entity.BookEntity
import com.muhammetkonukcu.litlounge.room.repository.BooksRepository
import com.muhammetkonukcu.litlounge.utils.ReadingFilter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class HistoryViewModel(private val booksRepository: BooksRepository) : ViewModel() {
    private val _currentFilter = MutableStateFlow(ReadingFilter.ALL)

    @OptIn(ExperimentalCoroutinesApi::class)
    val historyPagingDataFlow: Flow<PagingData<BookEntity>> = _currentFilter
        .flatMapLatest { filter ->
            when (filter) {
                ReadingFilter.ALL -> booksRepository.getBooks()
                ReadingFilter.READING -> booksRepository.getCurrentlyReadBooks()
                ReadingFilter.FINISHED -> booksRepository.getFinishedBooks()
            }
        }
        .cachedIn(viewModelScope)

    fun getBooksByFilter(filter: ReadingFilter) {
        _currentFilter.value = filter
    }

    fun removeBookFromDatabase(bookId: Int) {
        viewModelScope.launch {
            booksRepository.deleteBook(bookId)
        }
    }
}