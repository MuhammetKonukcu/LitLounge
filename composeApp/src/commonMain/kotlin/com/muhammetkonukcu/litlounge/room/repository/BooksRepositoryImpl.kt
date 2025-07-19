package com.muhammetkonukcu.litlounge.room.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import app.cash.paging.PagingData
import com.muhammetkonukcu.litlounge.room.dao.BooksDao
import com.muhammetkonukcu.litlounge.room.entity.BookEntity
import kotlinx.coroutines.flow.Flow

class BooksRepositoryImpl(private val booksDao: BooksDao) : BooksRepository {
    override suspend fun insertBook(entity: BookEntity) = booksDao.insertBook(entity)

    override suspend fun deleteBook(id: Int) = booksDao.deleteBook(id)

    override suspend fun getBookById(id: Int) = booksDao.getBookById(id)

    override fun getFinishedCountByMonth(monthStr: String): Flow<Int?> {
        return booksDao.getFinishedCountByMonth(monthStr)
    }

    override fun getBooks(pageSize: Int): Flow<PagingData<BookEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { booksDao.getBooks() }
        ).flow
    }

    override fun getCurrentlyReadBooks(pageSize: Int): Flow<PagingData<BookEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { booksDao.getCurrentlyReadBooks() }
        ).flow
    }
}