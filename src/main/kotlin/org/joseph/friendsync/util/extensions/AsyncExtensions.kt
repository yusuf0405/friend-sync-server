package org.joseph.friendsync.util.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlin.coroutines.cancellation.CancellationException

/**
 * The asyncSafe function is an extension of the CoroutineScope.async{...}
 * function and allows launching coroutines with additional exception handling
 *
 * @param defaultValue - default value witch return after throwing error in [block]
 *
 * @param block - suspend function that will be executed safely with in a CoroutineScope.
 */
inline fun <T> CoroutineScope.asyncSafe(
    defaultValue: T,
    crossinline block: suspend CoroutineScope.() -> T
): Deferred<T> = async {
    try {
        block()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        println(e)
        defaultValue
    }
}

/**
 * The asyncSafe function is an extension of the CoroutineScope.async{...}
 * function and allows launching coroutines with additional exception handling
 *
 * @param block - suspend function that will be executed safely with in a CoroutineScope.
 */
inline fun <T> CoroutineScope.asyncSafe(
    crossinline block: suspend CoroutineScope.() -> T
): Deferred<Result<T>> = async {
    try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}


/**
 * The asyncSafe function is an extension of the CoroutineScope.async{...}
 * function and allows launching coroutines with additional exception handling
 *
 * @param onErrorAction - function for handling error and should be return something default value
 *
 * @param block - suspend function that will be executed safely with in a CoroutineScope.
 */
inline fun <T> CoroutineScope.asyncSafe(
    crossinline onErrorAction: (Throwable) -> T,
    crossinline block: suspend CoroutineScope.() -> T
) = async {
    try {
        block()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        println(e)
        onErrorAction(e)
    }
}