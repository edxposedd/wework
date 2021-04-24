package com.magic.kernel.helper

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.FutureTask

object TryHelper {

    val mExecutorService: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2)
    val mHandler: Handler = Handler(Looper.getMainLooper())

    @JvmStatic
    inline fun <T : Any> trySilently(func: () -> T?): T? =
        try { func() } catch (t: Throwable) { null }

    @JvmStatic
    inline fun <T : Any> tryVerbosely(func: () -> T?): T? =
        try { func() } catch (t: Throwable) {
            Log.e(TryHelper.javaClass.name, "tryVerbosely error: $t   ${t.message}"); null
        }

    @JvmStatic
    inline fun <T: Any> tryMainThreadly(delayMillis: Long = 0, crossinline func: () -> T?) =
        mHandler.postDelayed({
            try { func() } catch (t: Throwable) {
                Log.e(TryHelper.javaClass.name, "tryMainThreadly error: ${t.message}")
            }
        }, delayMillis)

    @JvmStatic
    inline fun <T: Any> tryMainThreadly(delayMillis: Long = 0, crossinline func: () -> T?, crossinline callback: (T?) -> Unit) =
        mHandler.postDelayed({
            callback(try { func() } catch (t: Throwable) {
                Log.e(TryHelper.javaClass.name, "tryMainThreadly callback error: ${t.message}"); null
            })
        }, delayMillis)

    @JvmStatic
    inline fun <T: Any> tryAsynchronously(crossinline func: () -> T?) =
        mExecutorService.submit {
            try { func() } catch (t: Throwable) {
                Log.e(TryHelper.javaClass.name, "tryMainThreadly error: ${t.message}")
            }
        }

    @JvmStatic
    inline fun <T: Any> tryAsynchronously(crossinline  func: () -> T?, crossinline callback: (T?) -> Unit) {
        val futureTask = object : FutureTask<T?>(Callable {
            return@Callable try { func() } catch (t: Throwable) {
                Log.e(TryHelper.javaClass.name, "tryAsynchronously callback error: ${t.message}"); null
            }
        }) {
            override fun done() {
                callback(get())
            }
        }
        mExecutorService.submit(futureTask)
    }

    @JvmStatic
    inline fun <T: Any> tryAsynchronously(retryTimes: Int, crossinline func: () -> Pair<Boolean, T?>) =
        mExecutorService.submit {
            var currentTimes = 0
            try {
                while (currentTimes <= retryTimes) {
                    val result = func()
                    currentTimes = if (result.first) retryTimes + 1 else currentTimes + 1
                }
            } catch (t: Throwable) {
                Log.e(TryHelper.javaClass.name, "tryAsynchronously retryTimes error: ${t.message}")
            }
        }

    @JvmStatic
    inline fun <T: Any> tryAsynchronously(retryTimes: Int, crossinline func: () -> Pair<Boolean, T?>, crossinline callback: (T?) -> Unit){
        val futureTask = object : FutureTask<T?>(Callable {
            var currentTimes = 0
            var t: T? = null
            try {
                while (currentTimes <= retryTimes) {
                    val result = func()
                    t = result.second
                    currentTimes = if (result.first) retryTimes + 1 else currentTimes + 1
                }
            } catch (t: Throwable) {
                Log.e(TryHelper.javaClass.name, "tryAsynchronously retryTimes callback error: ${t.message}")
            }
            return@Callable t
        }) {
            override fun done() {
                callback(get())
            }
        }
        mExecutorService.submit(futureTask)
    }

}