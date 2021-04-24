package com.magic.kernel.okhttp

import com.magic.kernel.cache.LRUCache
import com.magic.kernel.helper.MD5
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

object Interceptors {

    /** 重试 */
    fun getRetryInterceptor(maxRetryTimes: Int = 3): Interceptor =
        object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                var retryTimes = 0

                val request = chain.request()
                var response = chain.proceed(request)

                while (!response.isSuccessful && retryTimes < maxRetryTimes) {
                    retryTimes += 1
                    response = chain.proceed(request)
                }
                return response
            }
        }

    /**
     * 缓存拦截器
     * @param cachePolicy IConfigs.CachePolicy
     * @param type IConfigs.Type
     */
    fun getCacheInterceptor(cachePolicy: IHttpConfigs.CachePolicy = IHttpConfigs.CachePolicy.ALL, type: IHttpConfigs.Type = IHttpConfigs.Type.DEFAULT) =
        object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val cacheKey = chain.request().url.toString().MD5()
                var cacheBytes: ByteArray? =
                    when (cachePolicy) {
                        IHttpConfigs.CachePolicy.NONE -> null
                        IHttpConfigs.CachePolicy.MEMORY -> LRUCache.getByteArrayFromMemory(cacheKey)
                        IHttpConfigs.CachePolicy.DISK -> LRUCache.getFromDisk(type.value, cacheKey)
                        IHttpConfigs.CachePolicy.ALL -> {
                            var bytes = LRUCache.getByteArrayFromMemory(cacheKey)
                            when (bytes == null) {
                                true -> LRUCache.getFromDisk(type.name, cacheKey)
                                false -> bytes
                            }
                        }
                    }

                return when (cacheBytes != null) {
                    true -> {
                        Response.Builder()
                            .request(chain.request())
                            .protocol(Protocol.HTTP_1_0)
                            .code(200)
                            .message("cache response success")
                            .body(cacheBytes.toResponseBody())
                            .build()
                    }
                    false -> {
                        val response = chain.proceed(chain.request())
                        val bytes = response.body?.bytes()
                        if (bytes != null) {
                            when (cachePolicy) {
                                IHttpConfigs.CachePolicy.NONE -> {}
                                IHttpConfigs.CachePolicy.MEMORY -> LRUCache.cacheInMemory(cacheKey, content = bytes)
                                IHttpConfigs.CachePolicy.DISK -> LRUCache.cacheInDisk(type.value, cacheKey, content = bytes)
                                IHttpConfigs.CachePolicy.ALL -> {
                                    LRUCache.cacheInMemory(cacheKey, content = bytes)
                                    LRUCache.cacheInDisk(type.value, cacheKey, content = bytes)
                                }
                            }
                        }
                        response
                    }
                }
            }
        }
}