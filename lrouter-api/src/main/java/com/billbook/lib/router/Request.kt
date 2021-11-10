package com.billbook.lib.router

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment

interface RequestBuilder<T> {
    fun uri(url: Uri): T
    fun launchMode(mode: Request.Mode): T
    fun requestCode(code: Int): T
    fun context(fragment: Fragment): T
    fun context(context: Context): T
    fun withExtras(block: Bundle.() -> Unit): T
    fun withOptions(block: Bundle.() -> Unit): T
    fun addFlags(flag: Int): T
    fun setFlags(flag: Int): T
    fun enterAnim(int: Int): T
    fun exitAnim(int: Int): T
    fun build(): Request
}

class Request private constructor(builder: Builder) {

    @get:JvmName("url")
    val uri: Uri = requireNotNull(builder.uri) { "Route request uri must not be null!" }

    @get:JvmName("extras")
    val extras: Bundle? = builder.extras

    @get:JvmName("options")
    val options: Bundle? = builder.options

    @get:JvmName("requestCode")
    val requestCode: Int? = builder.requestCode

    @get:JvmName("fragment")
    val fragment: Fragment? = builder.fragment

    @get:JvmName("context")
    val context: Context? = builder.context

    @get:JvmName("flags")
    val flags: Int? = builder.flags

    @get:JvmName("enterAnim")
    val enterAnim: Int? = builder.enterAnim

    @get:JvmName("exitAnim")
    val exitAnim: Int? = builder.exitAnim

    @get:JvmName("mode")
    val mode: Mode = builder.launchMode

    fun newBuilder(): Builder = Builder().apply {
        this@Request.uri.let { this.uri(it) }
        this@Request.extras?.let { this.withExtras { putAll(it) } }
        this@Request.mode.let { this.launchMode(it) }
        this@Request.fragment?.let { this.context(it) }
        this@Request.context?.let { this.context(it) }
        this@Request.requestCode?.let { this.requestCode(it) }
        this@Request.flags?.let { this.setFlags(it) }
        this@Request.enterAnim?.let { this.enterAnim(it) }
        this@Request.exitAnim?.let { this.exitAnim(it) }
        this@Request.options?.let { this.withOptions { putAll(it) } }
    }

    class Builder : RequestBuilder<Builder> {
        internal var extras: Bundle? = null
        internal var options: Bundle? = null
        internal var requestCode: Int? = null
        internal var flags: Int = 0
        internal var enterAnim: Int? = null
        internal var exitAnim: Int? = null
        internal var uri: Uri? = null
        internal var launchMode: Mode = Mode.START
        internal var fragment: Fragment? = null
        internal var context: Context? = null

        override fun uri(uri: Uri): Builder = apply {
            this.uri = uri
        }

        override fun launchMode(mode: Mode) = apply {
            this.launchMode = mode
        }

        override fun requestCode(requestCode: Int): Builder = apply {
            this.requestCode = requestCode
        }

        override fun withExtras(block: Bundle.() -> Unit): Builder = apply {
            if (extras == null) extras = Bundle()
            extras!!.apply(block)
        }

        override fun withOptions(block: Bundle.() -> Unit): Builder = apply {
            if (options == null) options = Bundle()
            options!!.apply(block)
        }

        override fun context(fragment: Fragment): Builder = apply {
            this.fragment = fragment
        }

        override fun context(context: Context): Builder = apply {
            this.context = context
        }

        override fun addFlags(flag: Int): Builder = apply {
            this.flags = this.flags or flag
        }

        override fun setFlags(flags: Int): Builder = apply {
            this.flags = flags
        }

        override fun enterAnim(animId: Int): Builder = apply {
            this.enterAnim = animId
        }

        override fun exitAnim(animId: Int): Builder = apply {
            this.exitAnim = animId
        }

        override fun build(): Request = Request(this)

        companion object {
            inline fun from(uri: Uri): Builder = Builder().uri(uri)
            inline fun from(url: String): Builder = Builder().uri(url.toUri())
        }
    }

    companion object {
        inline fun from(uri: Uri): Request = Builder().uri(uri).build()
        inline fun from(url: String): Request = from(url.toUri())
    }

    enum class Mode {
        START, ROUTE_ONLY, REACHABLE
    }
}

inline fun String.toUri(): Uri = Uri.parse(this)

inline fun routeRequestOf(uri: String, action: (Bundle) -> Unit) {
    val uri = Uri.parse(uri)
}

inline fun routeBuilderOf() = Request.Builder()