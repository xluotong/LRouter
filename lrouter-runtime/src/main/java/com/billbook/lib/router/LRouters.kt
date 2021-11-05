package com.billbook.lib.router

import com.billbook.lib.router.internel.LRouterDelegate

/**
 * @author xluotong@gmail.com
 */
object LRouters {

    fun initialize() {
        LRouter.initialize(LRouterDelegate())
    }
}