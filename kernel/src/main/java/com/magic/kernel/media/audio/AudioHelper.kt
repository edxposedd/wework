package com.magic.kernel.media.audio

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.magic.kernel.BuildConfig
import java.io.File
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.FutureTask

object AudioHelper {

    private val mExecutorService: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    private val mHandler: Handler = Handler(Looper.getMainLooper())

    /**
     * 将文件编码成pcm音频源文件
     */
    fun encodeToPcm(sourcePath: String, destPath: String, start: Long = 0, callback: ((Boolean) -> Unit)? = null) {
    }

    /**
     * 将文件编码成silk音频文件
     */
    fun encodeToSilk(sourcePath: String, destPath: String, start: Long, callback: ((Boolean) -> Unit)? = null) {
    }

    /**
     * 解码silk音频格式文件到pcm
     */
    fun decodeSilkToPcm(sourcePath: String, destPath: String, callback: ((Boolean) -> Unit)? = null) {
    }

    /**
     * 解码silk音频到Amr
     */
//    fun decodeSilkToAmr(sourcePath: String, destPath: String, callback: ((Boolean) -> Unit)? = null) {
//
//    }

}