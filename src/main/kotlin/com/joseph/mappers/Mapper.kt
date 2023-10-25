package com.joseph.mappers

interface Mapper<From, To> {

    fun map(from: From): To
}