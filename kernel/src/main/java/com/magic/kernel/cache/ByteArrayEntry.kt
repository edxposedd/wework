package com.magic.kernel.cache

import java.lang.ref.ReferenceQueue
import java.lang.ref.SoftReference

class ByteArrayEntry(
    val key: String,
    value: ByteArray,
    queue: ReferenceQueue<in ByteArray>,
    val timestamp: Long = System.currentTimeMillis()): SoftReference<ByteArray>(value, queue) {
    var size: Long = 0

    init {
        size = value.size.toLong()
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}