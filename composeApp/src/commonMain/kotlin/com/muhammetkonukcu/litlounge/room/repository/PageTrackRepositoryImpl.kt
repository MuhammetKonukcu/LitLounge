package com.muhammetkonukcu.litlounge.room.repository

import com.muhammetkonukcu.litlounge.room.dao.PageTrackDao
import com.muhammetkonukcu.litlounge.room.entity.PageEntity

class PageTrackRepositoryImpl(private val pageTrackDao: PageTrackDao) : PageTrackRepository {
    override suspend fun insertOrUpdatePage(entity: PageEntity) =
        pageTrackDao.addOrUpdateData(entity)

    override suspend fun getPageByDate(dateStr: String): PageEntity? =
        pageTrackDao.getPageByDate(dateStr)
}