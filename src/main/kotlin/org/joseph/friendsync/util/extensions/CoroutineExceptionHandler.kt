package org.joseph.friendsync.util.extensions

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * The launchSafe function is an extension of the [CoroutineScope.launch()]
 * function and allows launching coroutines with additional exception handling
 *
 * @param safeAction - suspend function that will be executed safely within a coroutine.
 *
 * @param onError -  lambda function that will be invoked in case an exception is thrown while
 * executing the [safeAction]. By default, this lambda function is set to log the error using [Timber::e]
 */
inline fun CoroutineScope.launchSafe(
    crossinline onError: (Throwable) -> Unit = { println(it) },
    crossinline safeAction: suspend CoroutineScope.() -> Unit,
) = this.launch {
    try {
        safeAction()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        onError(e)
    }
}

/**
 * The callSafe function allows for safe execution of a suspend block of code,
 * with the ability to specify a default value in case of an exception.
 *
 * @param defaultValue - The default value to be returned in case of an exception
 * @param block - A suspend function that contains the code to be executed safely.
 *
 * @return The result of the suspend block execution, or the specified default value if an exception occurs.
 *
 * @throws CancellationException if the coroutine is canceled during execution.
 */
suspend fun <T> callSafe(
    defaultValue: T,
    block: suspend () -> T
): T {
    return try {
        block()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        println(e)
        defaultValue
    }
}

/**
 * The asyncWithDefault function allows you to safely execute a suspend block of code asynchronously
 * within a coroutine, with the ability to specify a default value in case of an exception.
 *
 * @param defaultValue - The default value to be returned in case of an exception
 * @param block - A suspend function that contains the asynchronous code to be executed safely.
 *
 * @return A [Deferred] that represents the result of the suspend block execution.
 *         If an exception occurs, the [Deferred] will complete with the specified default value.
 *
 * @throws CancellationException if the coroutine is canceled during execution.
 */
suspend fun <T> asyncWithDefault(
    defaultValue: T,
    block: suspend () -> T
): Deferred<T> {
    return coroutineScope { asyncSafe(defaultValue) { block() } }
}

inline fun <T> Flow<T>.onError(crossinline action: (Throwable) -> Unit) = this.catch { action(it) }