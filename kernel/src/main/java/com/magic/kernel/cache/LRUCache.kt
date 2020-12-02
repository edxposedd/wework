package com.magic.kernel.cache

import android.os.Environment
import com.magic.kernel.helper.MD5
import java.io.File

object LRUCache {

    private const val ROOT_CACHE_DIR = "magic"
    private var memoryCache = LRUMemoryCache()
    private var diskCache = LRUDiskCache(getCacheDir())

    fun setup(memoryCacheSize: Long = 0, diskCacheSize: Long = 0) {
        if (memoryCacheSize != 0L) {
            memoryCache = LRUMemoryCache()
        }
        when (diskCacheSize == 0L) {
            true -> diskCache = LRUDiskCache(getCacheDir(), diskCacheSize)
            false -> diskCache = LRUDiskCache(getCacheDir())
        }
    }

    fun clearCache(callback: (() -> Unit)? = null) {
        memoryCache.clearCache()
        diskCache.clearCache { callback?.invoke() }
    }

    /** ---- 磁盘缓存 ---- */
    fun cacheInDisk(dir: String = "", key: String, params: Map<String, Any>? = null, content: ByteArray): String? =
        diskCache.cache(cachePath(dir, key, params), content)

    fun cacheInDisk(dir: String = "", key: String, params: Map<String, Any>? = null, content: ByteArray, callback: (String?) -> Unit) =
        diskCache.cache(cachePath(dir, key, params), content, callback)

    fun getFromDisk(dir: String = "", key: String, params: Map<String, Any>? = null): ByteArray? =
        diskCache.get(cachePath(dir, key, params))

    fun getFromDisk(dir: String = "", key: String, params: Map<String, Any>?, callback: (ByteArray?) -> Unit) =
        diskCache.get(cachePath(dir, key, params), callback)

    fun cacheDiskPath(dir: String = "", key: String, params: Map<String, Any>? = null): String? =
        diskCache.exists(cachePath(dir, key, params))

    /** ---- 磁盘缓存 ---- */
    fun cacheInMemory(key: String, params: Map<String, Any>? = null, content: ByteArray) =
        memoryCache.cache(realKey(key, params), content)

    fun getEntryFromMemory(key: String, params: Map<String, Any>? = null): ByteArrayEntry? =
        memoryCache.getEntry(realKey(key, params))

    fun getByteArrayFromMemory(key: String, params: Map<String, Any>? = null): ByteArray? =
        memoryCache.getByteArray(realKey(key, params))

    fun cachePath(dir: String, key: String, params: Map<String, Any>? = null, md5: Boolean = false): String =
        getCacheDir(dir) + File.separator + realKey(key, params, md5)

    private fun realKey(key: String, params: Map<String, Any>? = null, md5: Boolean = true): String {
        val indexOf = key.lastIndexOf(".")
        val key = when (indexOf > 0) {
            true -> key.substring(0, indexOf) + (if (params != null) params?.toString() else "") + key.substring(indexOf)
            false -> key + (if (params != null) params?.toString() else "")
        }
        return key.MD5()
    }

    private fun getCacheDir(dir: String = ""): String {
        val cacheDir = Environment.getExternalStorageDirectory().path + File.separator + ROOT_CACHE_DIR
        return when (dir.isNotEmpty()) {
            true -> "$cacheDir${File.separator}${dir.removeSuffix("/")}"
            else -> cacheDir
        }
    }
}