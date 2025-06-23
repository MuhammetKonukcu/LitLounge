package com.muhammetkonukcu.litlounge.room.repository

import com.muhammetkonukcu.litlounge.room.entity.UserEntity

interface UsersRepository {
    suspend fun insertQuery(entity: UserEntity)
    suspend fun getUser(): UserEntity?
}