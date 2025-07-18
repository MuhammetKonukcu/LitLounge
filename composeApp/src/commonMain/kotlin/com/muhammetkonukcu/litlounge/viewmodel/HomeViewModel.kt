package com.muhammetkonukcu.litlounge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import com.muhammetkonukcu.litlounge.room.entity.BookEntity
import com.muhammetkonukcu.litlounge.room.entity.PageEntity
import com.muhammetkonukcu.litlounge.room.entity.UserEntity
import com.muhammetkonukcu.litlounge.room.repository.BooksRepository
import com.muhammetkonukcu.litlounge.room.repository.PageTrackRepository
import com.muhammetkonukcu.litlounge.room.repository.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.utils.now

class HomeViewModel(
    private val usersRepository: UsersRepository,
    private val booksRepository: BooksRepository,
    private val pageTrackRepository: PageTrackRepository
) : ViewModel() {
    private val _userData = MutableStateFlow<UserEntity?>(null)
    val userData: StateFlow<UserEntity?> = _userData.asStateFlow()

    val readingBooksPagingDataFlow: Flow<PagingData<BookEntity>> =
        booksRepository.getCurrentlyReadBooks()
            .cachedIn(viewModelScope)

    private val _finishedBooksCountFlow = MutableStateFlow<Int?>(null)
    val finishedBooksCountFlow: StateFlow<Int?> = _finishedBooksCountFlow.asStateFlow()

    val pageTrackPagingDataFlow: Flow<PagingData<PageEntity>> =
        pageTrackRepository.getFullPageTrack()

    fun getUserData() {
        viewModelScope.launch {
            val user = usersRepository.getUser()
            user.let {
                _userData.value = it
            }

            val currentMonthNumber = LocalDate.now().monthNumber
            val monthStr = currentMonthNumber.toString().padStart(2, '0')

            booksRepository.getFinishedCountByMonth(monthStr)
                .collect { count ->
                    _finishedBooksCountFlow.value = count
                }
        }
    }
}