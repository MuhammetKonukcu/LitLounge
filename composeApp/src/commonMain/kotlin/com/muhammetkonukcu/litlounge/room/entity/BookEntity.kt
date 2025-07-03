package com.muhammetkonukcu.litlounge.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val totalPage: Int = 0,
    val currentPage: Int = 0,
    val imageURL: String = "",
    val authorName: String = "",
    val finished: Boolean = false,
    val startTimestamp: String = "",
    val finishTimestamp: String = "",
)