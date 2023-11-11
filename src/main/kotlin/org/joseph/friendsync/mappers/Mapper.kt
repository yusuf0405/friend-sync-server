package org.joseph.friendsync.mappers

interface Mapper<From, To> {

    fun map(from: From): To
}