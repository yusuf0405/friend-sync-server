package org.joseph.friendsync.util

import com.joseph.repository.subscription.something_went_wrong
import io.ktor.http.*

sealed class Response<T>(
    val code: HttpStatusCode = HttpStatusCode.OK,
    val data: T
) {
    class Success<T>(data: T) : Response<T>(data = data)

    class Error<T>(
        code: HttpStatusCode,
        data: T
    ) : Response<T>(
        data = data,
        code = code
    ) {
        companion object {
            val defaultError = Error(
                code = HttpStatusCode.InternalServerError,
                data = something_went_wrong
            )
        }
    }
}