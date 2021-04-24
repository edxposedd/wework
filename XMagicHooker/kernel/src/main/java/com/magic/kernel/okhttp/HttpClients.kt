package com.magic.kernel.okhttp

import android.util.Log
import com.magic.kernel.cache.LRUCache
import com.magic.kernel.helper.MD5
import com.magic.kernel.helper.TryHelper.tryMainThreadly
import okhttp3.*
import java.io.IOException

object HttpClients {

    /**
     * 下载资源文件，这里由于企业微信发送文件必须是本地路径，故缓存策略固定为DISK
     * @param urlString 下载地址
     * @param userInfo 用户传递的其他数据，将会在回调中原样返回
     * @param type 文件类型
     * @param iDownloadCallback 下载回调
     * @param iProgressRequestCallback 上传进度
     * @param iProgressResponseCallback 下载进度
     */
    fun download(
        urlString: String, type: IHttpConfigs.Type = IHttpConfigs.Type.DEFAULT,
        iDownloadCallback: IDownloadCallback,
        iProgressRequestCallback: IProgressRequestCallback? = null,
        iProgressResponseCallback: IProgressResponseCallback? = null
    ) {
        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor(Interceptors.getRetryInterceptor())
            .addInterceptor(Interceptors.getCacheInterceptor(IHttpConfigs.CachePolicy.DISK, type))

        val request = Request.Builder().url(urlString).build()
        clientBuilder.build().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                tryMainThreadly {
                    iDownloadCallback(null, type)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                Log.e(HttpClients.javaClass.name, "onResponse : ${response.message}")
                val cacheKey = call.request().url.toString().MD5()
                tryMainThreadly {
                    iDownloadCallback(LRUCache.cacheDiskPath(type.value, cacheKey), type)
                }
            }
        })
    }

}