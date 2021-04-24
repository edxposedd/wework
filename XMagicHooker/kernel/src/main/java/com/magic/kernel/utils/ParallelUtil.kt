package com.magic.kernel.utils

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import com.magic.kernel.helper.TryHelper.tryVerbosely

object ParallelUtil {

    val processors: Int = Runtime.getRuntime().availableProcessors()

    @JvmStatic
    fun createThreadPool(nThread: Int = processors): ExecutorService =
        Executors.newFixedThreadPool(nThread)

    @JvmStatic
    inline fun <T, R> List<T>.parallelMap(crossinline transform: (T) -> R): List<R> {
        val sectionSize = size / processors

        val main = List(processors) { mutableListOf<R>() }
        (0 until processors).map { section ->
            thread(start = true) {
                for (offset in 0 until sectionSize) {
                    val idx = section * sectionSize + offset
                    main[section].add(transform(this[idx]))
                }
            }
        }.forEach { it.join() }

        val rest = (0 until size % processors).map { offset ->
            val idx = processors * sectionSize + offset
            transform(this[idx])
        }

        return main.flatten() + rest
    }

    @JvmStatic
    inline fun <T> Iterable<T>.parallelForEach(crossinline action: (T) -> Unit) {
        val pool = createThreadPool()
        val iterator = iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            pool.execute {
                tryVerbosely { action(item) } // 避免进程崩溃
            }
        }
        pool.shutdown()
        pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)
    }
}