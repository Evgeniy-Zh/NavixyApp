package com.blueprint.squaregps.utils

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope


suspend fun <R> testScope(block: suspend CoroutineScope.() -> R) {
    try {
        coroutineScope(block)
    } catch (cancellation: CancellationException) {
        println("scope canceled")
    }
}