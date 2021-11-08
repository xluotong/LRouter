package com.billbook.lib.router

import android.app.Application
import com.billbook.lib.router.internel.RouterRuntime
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author xluotong@gmail.com
 */
object LRouterInitializer {

    private val initialized = AtomicBoolean(false)

    @JvmOverloads
    @JvmStatic
    fun initialize(app: Application) {
        if (initialized.getAndSet(true)) return
        RouterRuntime.initialize(app)
    }
}

class Configuration {

}