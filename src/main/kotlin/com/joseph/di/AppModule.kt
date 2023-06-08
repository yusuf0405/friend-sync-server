package com.joseph.di

import com.joseph.dao.user.UserDao
import com.joseph.dao.user.UserDaoImpl
import com.joseph.repository.UserRepository
import com.joseph.repository.UserRepositoryImpl
import org.koin.dsl.module

val appModule = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<UserDao> { UserDaoImpl() }
}