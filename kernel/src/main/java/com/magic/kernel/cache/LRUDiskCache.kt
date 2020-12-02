package com.magic.kernel.cache

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.*
import java.util.*
import java.util.concurrent.*
import kotlin.collections.LinkedHashMap

/**
 * 磁盘缓存
 */
class LRUDiskCache(val cacheDir: String, val maxCacheSize: Long = 1024 * 1024 * 1024) {

    private var cachedSize: Long = 0
    private val executorService: ExecutorService =
        Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors())
    private val mHandler = Handler(Looper.getMainLooper())

    /** 用于按照最近最久未使用存储文件的 path 及 lastModified */
    private val linkedHashMap: LinkedHashMap<String, Long> =
        object : LinkedHashMap<String, Long>(16, 0.75f, true) {
            override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, Long>?): Boolean {
                val shouldRemove = cachedSize > maxCacheSize
                if (shouldRemove) {
                    val file = File(eldest?.key ?: "")
                    deleteFile(file) {}
                }
                return shouldRemove
            }
        }

    init {
        val file = File(cacheDir)
        if (!file.exists()) file.mkdirs()
        val futureTask = object : FutureTask<TreeMap<Long, String>>(Callable {
            return@Callable readCache(file)
        }) {
            override fun done() {
                get().forEach { linkedHashMap[it.value] = it.key }
            }
        }
        executorService.submit(futureTask)
    }

    fun cache(path: String, content: ByteArray): String? =
        try {
            val file = File(path).also { if (it.parentFile?.exists() == false) it.parentFile?.mkdirs() }
            val fileOutputStream = FileOutputStream(file)
            BufferedOutputStream(fileOutputStream).use { it.write(content) }
            file.path
        } catch (e: Throwable) {
            Log.e(LRUDiskCache::class.java.name, "cache fail: ${e.message}")
            null
        }

    fun cache(path: String, content: ByteArray, callback: (String?) -> Unit) {
        val futureTask = object : FutureTask<String?>(Callable { cache(path, content) }) {
            override fun done() {
                mHandler.post { callback(get()) }
            }
        }
        executorService.submit(futureTask)
    }

    fun exists(path: String): String? = if (File(path).exists()) path else null

    fun get(path: String): ByteArray? =
        try {
            BufferedInputStream(FileInputStream(File(path))).use { it.readBytes() }
        } catch (_: Throwable) {
            null
        }

    fun get(path: String, callback: (ByteArray?) -> Unit) {
        val futureTask = object : FutureTask<ByteArray?>(Callable { get(path) }) {
            override fun done() {
                mHandler.post { callback(get()) }
            }
        }
        executorService.submit(futureTask)
    }

    fun deleteFile(file: File) {
        if (file.isDirectory) {
            val files = file.listFiles() ?: arrayOf()
            for (subFile in files) {
                deleteFile(subFile)
            }
        } else {
            cachedSize -= if (file.exists()) file.length() else 0
            linkedHashMap.remove(file.path)
            file.deleteOnExit()
        }
    }

    fun deleteFile(file: File, callback: () -> Unit) {
        val futureTask = object : FutureTask<Unit>(Callable { deleteFile(file) }) {
            override fun done() {
                mHandler.post { callback() }
            }
        }
        executorService.submit(futureTask)
    }

    /**
     * 清除指定目录下的缓存，如果file == null，则清除所有缓存
     */
    fun clearCache(file: File? = null, callback: () -> Unit) {
        val futureTask = object : FutureTask<Unit>(Callable {
            if (file == null) {
                cachedSize = 0
                File(cacheDir).deleteOnExit()
            } else {
                deleteFile(file)
            }
        }) {
            override fun done() {
                mHandler.post { callback() }
            }
        }
        executorService.submit(futureTask)
    }

    /**
     * 初始化缓存，将所有文件信息读取，并根据上次lastModified修改排序
     */
    private fun readCache(file: File): TreeMap<Long, String> {
        val treeMap = TreeMap<Long, String> { o1, o2 -> (o1 - o2).toInt() }
        if (file.isDirectory) {
            val files = file.listFiles() ?: arrayOf()
            for (subFile in files) {
                treeMap.putAll(readCache(subFile))
            }
        } else {
            treeMap[file.lastModified()] = file.path
        }
        return treeMap
    }

}






