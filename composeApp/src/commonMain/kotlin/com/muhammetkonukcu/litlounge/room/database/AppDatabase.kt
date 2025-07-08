package com.muhammetkonukcu.litlounge.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.muhammetkonukcu.litlounge.room.dao.BooksDao
import com.muhammetkonukcu.litlounge.room.dao.PageTrackDao
import com.muhammetkonukcu.litlounge.room.dao.UsersDao
import com.muhammetkonukcu.litlounge.room.entity.BookEntity
import com.muhammetkonukcu.litlounge.room.entity.PageEntity
import com.muhammetkonukcu.litlounge.room.entity.UserEntity

@Database(entities = [UserEntity::class, BookEntity::class, PageEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getUsersDao(): UsersDao
    abstract fun getBooksDao(): BooksDao
    abstract fun getPageTrackDao(): PageTrackDao
}