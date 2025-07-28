package com.muhammetkonukcu.litlounge.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.cash.paging.PagingSource
import com.muhammetkonukcu.litlounge.room.entity.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BooksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(entity: BookEntity)

    @Query("SELECT * FROM books WHERE currentPage < totalPage ORDER BY startTimestamp DESC")
    fun getCurrentlyReadBooks(): PagingSource<Int, BookEntity>

    @Query("SELECT * FROM books ORDER BY startTimestamp DESC")
    fun getBooks(): PagingSource<Int, BookEntity>

    @Query("SELECT * FROM books WHERE currentPage >= totalPage ORDER BY finishTimestamp DESC")
    fun getFinishedBooks(): PagingSource<Int, BookEntity>

    @Query("SELECT * FROM books WHERE id = :id")
    fun getBookById(id: Int): Flow<BookEntity?>

    @Query("DELETE FROM books WHERE id = :id")
    suspend fun deleteBook(id: Int)

    @Query("""
        SELECT COUNT(*) 
          FROM books 
         WHERE strftime('%m', finishTimestamp) = :monthStr
    """)
    fun getFinishedCountByMonth(monthStr: String): Flow<Int?>
}