package com.muhammetkonukcu.litlounge.room.repository

import com.muhammetkonukcu.litlounge.room.entity.PageEntity

interface PageTrackRepository {
    suspend fun insertOrUpdatePage(entity: PageEntity)
    suspend fun getPageByDate(dateStr: String): PageEntity?
}