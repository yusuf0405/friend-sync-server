package org.joseph.friendsync.di

import org.joseph.friendsync.db.dao.categories.CategoriesDao
import org.joseph.friendsync.db.dao.categories.CategoriesDaoImpl
import org.joseph.friendsync.db.dao.post.PostDao
import org.joseph.friendsync.db.dao.post.PostDaoImpl
import org.joseph.friendsync.db.dao.subscription.SubscriptionDao
import org.joseph.friendsync.db.dao.subscription.SubscriptionDaoImpl
import org.joseph.friendsync.db.dao.user.UserDao
import org.joseph.friendsync.db.dao.user.UserDaoImpl
import org.joseph.friendsync.mappers.*
import org.joseph.friendsync.repository.auth.AuthRepository
import org.joseph.friendsync.repository.auth.AuthRepositoryImpl
import org.joseph.friendsync.repository.categories.CategoriesRepository
import org.joseph.friendsync.repository.categories.CategoriesRepositoryImpl
import org.joseph.friendsync.repository.post.PostRepository
import org.joseph.friendsync.repository.post.PostRepositoryImpl
import org.joseph.friendsync.repository.subscription.SubscriptionRepository
import org.joseph.friendsync.repository.subscription.SubscriptionRepositoryImpl
import org.joseph.friendsync.repository.user.UserRepository
import org.joseph.friendsync.repository.user.UserRepositoryImpl
import org.koin.dsl.module

val appModule = module {
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<PostRepository> { PostRepositoryImpl(get(), get()) }
    single<SubscriptionRepository> { SubscriptionRepositoryImpl(get()) }
    single<CategoriesRepository> { CategoriesRepositoryImpl(get()) }
    single<UserDao> { UserDaoImpl(get(), get(), get()) }
    single<PostDao> { PostDaoImpl(get()) }
    single<SubscriptionDao> { SubscriptionDaoImpl() }
    single<CategoriesDao> { CategoriesDaoImpl(get()) }
    factory { ResultRowToUserMapper() }
    factory { ResultRowToUserInfoMapper() }
    factory { ResultRowToCategoryMapper() }
    factory<ResultRowToPostMapper> { ResultRowToPostMapperImpl() }
}