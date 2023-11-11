package org.joseph.friendsync.di

import com.joseph.db.dao.categories.CategoriesDao
import com.joseph.db.dao.categories.CategoriesDaoImpl
import com.joseph.db.dao.post.PostDao
import com.joseph.db.dao.post.PostDaoImpl
import com.joseph.db.dao.subscription.SubscriptionDao
import com.joseph.db.dao.subscription.SubscriptionDaoImpl
import com.joseph.db.dao.user.UserDao
import com.joseph.db.dao.user.UserDaoImpl
import com.joseph.mappers.*
import com.joseph.repository.auth.AuthRepository
import com.joseph.repository.auth.AuthRepositoryImpl
import com.joseph.repository.categories.CategoriesRepository
import com.joseph.repository.categories.CategoriesRepositoryImpl
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