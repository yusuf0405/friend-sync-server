package com.joseph.di

import com.joseph.db.dao.post.PostDao
import com.joseph.db.dao.post.PostDaoImpl
import com.joseph.db.dao.subscription.SubscriptionDao
import com.joseph.db.dao.subscription.SubscriptionDaoImpl
import com.joseph.db.dao.user.UserDao
import com.joseph.db.dao.user.UserDaoImpl
import com.joseph.mappers.ResultRowToPostMapper
import com.joseph.mappers.ResultRowToPostMapperImpl
import com.joseph.mappers.ResultRowToUserInfoMapper
import com.joseph.mappers.ResultRowToUserMapper
import com.joseph.repository.auth.AuthRepository
import com.joseph.repository.auth.AuthRepositoryImpl
import com.joseph.repository.post.PostRepository
import com.joseph.repository.post.PostRepositoryImpl
import com.joseph.repository.subscription.SubscriptionRepository
import com.joseph.repository.subscription.SubscriptionRepositoryImpl
import com.joseph.repository.user.UserRepository
import com.joseph.repository.user.UserRepositoryImpl
import org.koin.dsl.module

val appModule = module {
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<PostRepository> { PostRepositoryImpl(get(), get()) }
    single<SubscriptionRepository> { SubscriptionRepositoryImpl(get()) }
    single<UserDao> { UserDaoImpl(get(), get(), get()) }
    single<PostDao> { PostDaoImpl(get()) }
    single<SubscriptionDao> { SubscriptionDaoImpl() }
    factory { ResultRowToUserMapper() }
    factory { ResultRowToUserInfoMapper() }
    factory<ResultRowToPostMapper> { ResultRowToPostMapperImpl() }
}