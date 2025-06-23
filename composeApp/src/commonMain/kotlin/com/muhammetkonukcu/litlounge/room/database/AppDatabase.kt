package com.muhammetkonukcu.litlounge.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.muhammetkonukcu.litlounge.room.dao.UsersDao
import com.muhammetkonukcu.litlounge.room.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getUsersDao(): UsersDao
}