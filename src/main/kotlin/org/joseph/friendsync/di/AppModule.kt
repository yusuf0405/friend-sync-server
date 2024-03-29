package org.joseph.friendsync.di

import org.joseph.friendsync.db.dao.categories.CategoriesDao
import org.joseph.friendsync.db.dao.categories.CategoriesDaoImpl
import org.joseph.friendsync.db.dao.comments.CommentsDao
import org.joseph.friendsync.db.dao.comments.CommentsDaoImpl
import org.joseph.friendsync.db.dao.likes.LikesPostDao
import org.joseph.friendsync.db.dao.likes.LikesPostDaoImpl
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
import org.joseph.friendsync.repository.comments.CommentsRepository
import org.joseph.friendsync.repository.comments.CommentsRepositoryImpl
import org.joseph.friendsync.repository.likes.LikesPostRepository
import org.joseph.friendsync.repository.likes.LikesPostRepositoryImpl
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
    single<CommentsRepository> { CommentsRepositoryImpl(get()) }
    single<LikesPostDao> { LikesPostDaoImpl() }
    single<SubscriptionRepository> { SubscriptionRepositoryImpl(get()) }
    single<CategoriesRepository> { CategoriesRepositoryImpl(get()) }
    single<UserDao> { UserDaoImpl(get(), get(), get(), get()) }
    single<PostDao> { PostDaoImpl(get()) }
    single<CommentsDao> { CommentsDaoImpl(get()) }
    single<SubscriptionDao> { SubscriptionDaoImpl() }
    single<LikesPostRepository> { LikesPostRepositoryImpl(get()) }
    single<CategoriesDao> { CategoriesDaoImpl(get()) }
    factory { ResultRowToUserMapper() }
    factory { ResultRowToUserPersonalInfoMapper() }
    factory { ResultRowToUserInfoMapper() }
    factory { ResultRowToCategoryMapper() }
    factory<ResultRowToPostMapper> { ResultRowToPostMapperImpl(get(), get(), get()) }
    factory { ResultRowToCommentMapper(get()) }
    factory { ResultRowToCommentMapper(get()) }
}