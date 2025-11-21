package com.blueprint.squaregps.core.exception

import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <T> suspendCatching(
    run: suspend () -> T,
    onException: (Exception) -> T,
): T {
    return try {
        run()
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        e.printStackTrace()
        onException(e)
    }
}