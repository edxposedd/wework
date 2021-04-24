package com.magic.kernel.cache

import java.lang.ref.ReferenceQueue

/**
 * LRU 内存缓存
 */
class LRUMemoryCache(var maxCacheSize: Long = 256 * 1024 * 1024) {

    private var cachedSize: Long = 0

    private val linkedHashMap: LinkedHashMap<String, ByteArrayEntry>

    private val referenceQueue = ReferenceQueue<ByteArray>()

    init {
        linkedHashMap = object : LinkedHashMap<String, ByteArrayEntry>(16, 0.75f, true) {
            override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, ByteArrayEntry>?): Boolean {
                val shouldRemove = cachedSize > maxCacheSize
                if (shouldRemove) {
                    clearRecycled()
                    System.gc()

                    cachedSize -= eldest?.value?.size ?: 0
                }
                return shouldRemove
            }
        }
    }

    fun cache(key: String, value: ByteArray) {
        val entry = ByteArrayEntry(key, value, referenceQueue)
        cache(key, entry)
    }

    fun cache(key: String, entry: ByteArrayEntry) {
        cachedSize = entry.size
        linkedHashMap[key] = entry
    }

    fun getEntry(key: String): ByteArrayEntry? = linkedHashMap[key]

    fun getByteArray(key: String): ByteArray? = getEntry(key)?.get()

    fun clearCache() {
        linkedHashMap.clear()
        cachedSize = 0
        System.gc()
        System.runFinalization()
    }

    private fun clearRecycled() {
        var softReference: ByteArrayEntry? = referenceQueue.poll() as? ByteArrayEntry
        while (softReference != null) {
            if (softReference.get() == null) {
                cachedSize -= softReference.size
                linkedHashMap.remove(softReference.key)
            }
            softReference = referenceQueue.poll() as? ByteArrayEntry
        }
    }

}