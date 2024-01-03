package org.joseph.friendsync.mappers

interface AsyncMapper<From, To> {

    suspend fun map(from: From): To
}