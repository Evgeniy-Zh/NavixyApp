package com.blueprint.squaregps.navigation

import androidx.lifecycle.LifecycleOwner

interface NavigationEventHandler {
    fun handle(lifecycleOwner: LifecycleOwner, block: (NavAction) -> Unit)
}