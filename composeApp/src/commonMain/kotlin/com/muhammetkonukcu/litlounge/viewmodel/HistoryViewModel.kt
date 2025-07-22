package com.muhammetkonukcu.litlounge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import com.muhammetkonukcu.litlounge.room.entity.BookEntity
import com.muhammetkonukcu.litlounge.room.repository.BooksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HistoryViewModel(private val booksRepository: BooksRepository) : ViewModel() {
    val historyPagingDataFlow: Flow<PagingData<BookEntity>> =
        booksRepository.getBooks()
            .cachedIn(viewModelScope)

    fun removeBookFromDatabase(bookId: Int) {
        viewModelScope.launch {
            booksRepository.deleteBook(bookId)
        }
    }
}