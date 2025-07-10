package com.muhammetkonukcu.litlounge.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import app.cash.paging.PagingSource
import com.muhammetkonukcu.litlounge.room.entity.PageEntity

@Dao
interface PageTrackDao {

    @Update
    suspend fun updatePage(entity: PageEntity)

    @Insert
    suspend fun insertPage(entity: PageEntity)

    @Transaction
    suspend fun addOrUpdateData(entity: PageEntity) {
        val existing = getPageByDate(entity.dateStr)
        if (existing == null) {
            insertPage(PageEntity(pageCount = entity.pageCount, dateStr = entity.dateStr))
        } else {
            val updated = existing.copy(pageCount = existing.pageCount + entity.pageCount)
            updatePage(updated)
        }
    }

    @Query("SELECT * FROM PageEntity WHERE dateStr = :dateStr LIMIT 1")
    suspend fun getPageByDate(dateStr: String): PageEntity?

    @Query("SELECT * FROM PageEntity ORDER BY id DESC")
    fun getFullPageTrack(): PagingSource<Int, PageEntity>
}
