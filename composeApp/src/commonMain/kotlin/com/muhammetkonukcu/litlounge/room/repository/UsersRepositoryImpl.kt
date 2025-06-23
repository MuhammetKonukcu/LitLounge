package com.muhammetkonukcu.litlounge.room.repository

import com.muhammetkonukcu.litlounge.room.dao.UsersDao
import com.muhammetkonukcu.litlounge.room.entity.UserEntity

class UsersRepositoryImpl(private val usersDao: UsersDao) : UsersRepository {
    override suspend fun insertQuery(entity: UserEntity) = usersDao.insertUser(entity)

    override suspend fun getUser(): UserEntity = usersDao.getUser()
}