package com.billbook.lib.router

import android.app.Activity

/**
 * @author xluotong@gmail.com
 */
interface Injector<T> {

    fun inject(target: T)
}

class MainActivityInject : Injector<Activity> {
    override fun inject(target: Activity) {
        // 怎么区分是服务还是extra,extra用Autowired
        // 收集类Field
    }
}