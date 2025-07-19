package com.muhammetkonukcu.litlounge.room.repository

import app.cash.paging.PagingData
import com.muhammetkonukcu.litlounge.room.entity.BookEntity
import kotlinx.coroutines.flow.Flow

interface BooksRepository {
    suspend fun insertBook(entity: BookEntity)
    suspend fun deleteBook(id: Int)
    suspend fun getBookById(id: Int): Flow<BookEntity?>
    fun getFinishedCountByMonth(monthStr: String): Flow<Int?>
    fun getBooks(pageSize: Int = 10): Flow<PagingData<BookEntity>>
    fun getCurrentlyReadBooks(pageSize: Int = 5): Flow<PagingData<BookEntity>>
}