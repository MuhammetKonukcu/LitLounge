package com.muhammetkonukcu.litlounge.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PageEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var pageCount: Int,
    var dateStr: String
)