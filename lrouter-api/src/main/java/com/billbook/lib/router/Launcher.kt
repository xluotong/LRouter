package com.billbook.lib.router

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent

/**
 * @author xluotong@gmail.com
 */
interface Launcher {
    @Throws(ActivityNotFoundException::class)
    fun launch(context: Context, intent: Intent, contract: Contract)

    fun createIntent(context: Context, contract: Contract): Intent

    interface Contract {
        val request: Request
        val routeInfo: RouteInfo
    }
}
