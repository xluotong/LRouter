package com.billbook.lib.router

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

interface HasExtras<T> {
    fun addExtra(key: String, value: Byte): T
    fun addExtra(key: String, value: Short): T
    fun addExtra(key: String, value: Int): T
    fun addExtra(key: String, value: Long): T
    fun addExtra(key: String, value: CharSequence): T
    fun addExtra(key: String, value: Array<*>): T
    fun addExtra(key: String, value: String): T
    fun addExtra(key: String, value: Float): T
    fun addExtra(key: String, value: Double): T
    fun addExtra(key: String, value: Char): T
    fun addExtra(key: String, value: Boolean): T
    fun addExtra(key: String, value: Serializable): T
    fun addExtra(key: String, value: Bundle): T
    fun addExtra(key: String, value: Parcelable): T
    fun addExtra(key: String, value: ByteArray): T
    fun addExtra(key: String, value: ShortArray): T
    fun addExtra(key: String, value: IntArray): T
    fun addExtra(key: String, value: LongArray): T
    fun addExtra(key: String, value: FloatArray): T
    fun addExtra(key: String, value: DoubleArray): T
    fun addExtra(key: String, value: CharArray): T
    fun addExtra(key: String, value: BooleanArray): T
    fun addExtras(bundle: Bundle): T
}

interface RequestBuilder<T> : HasExtras<T> {
    fun uri(uri: String)
    fun requestCode(code: Int): T
    fun addFlag(flag: Int): T
    fun setFlag(flag: Int): T
    fun enterAnim(int: Int): T
    fun exitAnim(int: Int): T
    fun build(): Request
}

class Request private constructor(builder: Builder) {

    private val uri: String = requireNotNull(builder.uri) { "Route request uri must not be null!" }
    private val extras: Bundle? = builder.extras
    private val requestCode: Int? = builder.requestCode
    private val flag: Int? = builder.flag
    private val enterAnim: Int? = builder.enterAnim

    fun newBuilder(): Builder = Builder().apply {
        this@Request.uri.let { this.uri(it) }
        this@Request.extras?.let { this.addExtras(it) }
        this@Request.requestCode?.let { this.requestCode(it) }
        this@Request.flag?.let { this.setFlag(it) }
        this@Request.enterAnim?.let { this.enterAnim(it) }
    }

    class Builder : RequestBuilder<Builder> {
        internal val extras: Bundle by lazy { Bundle() }
        internal var requestCode: Int? = null
        internal var flag: Int? = null
        internal var enterAnim: Int? = null
        internal var uri: String? = null

        override fun addExtra(key: String, value: Byte): Builder {
            extras.putByte(key, value)
            return this
        }

        override fun addExtra(key: String, value: Short): Builder {
            TODO("Not yet implemented")
        }

        override fun addExtra(key: String, value: Int): Builder {
            TODO("Not yet implemented")
        }

        override fun addExtra(key: String, value: Long): Builder {
            TODO("Not yet implemented")
        }

        override fun addExtra(key: String, value: CharSequence): Builder {
            TODO("Not yet implemented")
        }

        override fun addExtra(key: String, value: Array<*>): Builder {
            TODO("Not yet implemented")
        }

        override fun addExtra(key: String, value: String): Builder {
            TODO("Not yet implemented")
        }

        override fun addExtra(key: String, value: Float): Builder {
            TODO("Not yet implemented")
        }

        override fun addExtra(key: String, value: Double): Builder {
            TODO("Not yet implemented")
        }

        override fun addExtra(key: String, value: Char): Builder {
            TODO("Not yet implemented")
        }

        override fun addExtra(key: String, value: Boolean): Builder {
            TODO("Not yet implemented")
        }

        override fun addExtra(key: String, value: Serializable): Builder {
            TODO("Not yet implemented")
        }

        override fun addExtra(key: String, value: Bundle): Builder {
            TODO("Not yet implemented")
        }

        override fun addExtra(key: String, value: Parcelable): Builder {
            TODO("Not yet implemented")
        }

        override fun addExtra(key: String, value: ByteArray): Builder {
            TODO("Not yet implemented")
        }

        override fun addExtra(key: String, value: ShortArray): Builder {
            TODO("Not yet implemented")
        }

        override fun addExtra(key: String, value: IntArray): Builder {
            TODO("Not yet implemented")
        }

        override fun addExtra(key: String, value: LongArray): Builder {
            TODO("Not yet implemented")
        }

        override fun addExtra(key: String, value: FloatArray): Builder {
            TODO("Not yet implemented")
        }

        override fun addExtra(key: String, value: DoubleArray): Builder {
            TODO("Not yet implemented")
        }

        override fun addExtra(key: String, value: CharArray): Builder {
            TODO("Not yet implemented")
        }

        override fun addExtra(key: String, value: BooleanArray): Builder {
            TODO("Not yet implemented")
        }

        override fun addExtras(bundle: Bundle): Builder {
            TODO("Not yet implemented")
        }

        override fun uri(uriString: String) {
            TODO("Not yet implemented")
        }

        override fun requestCode(code: Int): Builder {
            TODO("Not yet implemented")
        }

        override fun addFlag(flag: Int): Builder {
            TODO("Not yet implemented")
        }

        override fun setFlag(flag: Int): Builder {
            TODO("Not yet implemented")
        }

        override fun enterAnim(int: Int): Builder {
            TODO("Not yet implemented")
        }

        override fun exitAnim(int: Int): Builder {
            TODO("Not yet implemented")
        }

        override fun build(): Request {
            TODO("Not yet implemented")
        }
    }
}

inline fun routeBuilderOf(uri: String, extra: Bundle? = null) {
    val uri = Uri.parse(uri)
}

//val request:Request.Builder()
//        .addExtras(Bundle())
//        .addExtra("A",1)
//        .setRequestCode(1)
//        .setFlag()
//        .addFlag()
//        .build()

