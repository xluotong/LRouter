package com.billbook.lib.router

import com.google.gson.Gson

val globalGson = Gson()

const val PACKAGE_GENERATE = "com.billbook.lib.router.generated"
const val MODULES_CLASS = "com.billbook.lib.router.internal.ModuleProvider"
const val ABSTRACT_MODULE_CONTAINER_CLASS = "com.billbook.lib.router.internal.ModuleContainer"
