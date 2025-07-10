package com.muhammetkonukcu.litlounge.room.repository

import androidx.paging.PagingData
import com.muhammetkonukcu.litlounge.room.entity.PageEntity
import kotlinx.coroutines.flow.Flow

interface PageTrackRepository {
    suspend fun insertOrUpdatePage(entity: PageEntity)
    suspend fun getPageByDate(dateStr: String): PageEntity?
    fun getFullPageTrack(pageSize: Int = 10): Flow<PagingData<PageEntity>>
}