package com.muhammetkonukcu.litlounge.room.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import app.cash.paging.PagingData
import com.muhammetkonukcu.litlounge.room.dao.PageTrackDao
import com.muhammetkonukcu.litlounge.room.entity.PageEntity
import kotlinx.coroutines.flow.Flow

class PageTrackRepositoryImpl(private val pageTrackDao: PageTrackDao) : PageTrackRepository {
    override suspend fun insertOrUpdatePage(entity: PageEntity) =
        pageTrackDao.addOrUpdateData(entity)

    override suspend fun getPageByDate(dateStr: String): PageEntity? =
        pageTrackDao.getPageByDate(dateStr)

    override fun getFullPageTrack(pageSize: Int): Flow<PagingData<PageEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { pageTrackDao.getFullPageTrack() }
        ).flow
    }
}