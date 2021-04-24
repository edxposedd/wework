package com.magic.kernel.okhttp

interface IHttpConfigs {

    /** 缓存策略 */
    enum class CachePolicy {
        NONE, MEMORY, DISK, ALL
    }

    /** 文件类型 */
    enum class Type(var value: String) {
        DEFAULT("file"),
        FILE("file"),
        IMAGE("image"),
        VOICE("voice"),
        VIDEO("video")
    }

    /** 请求方法 常用配置 */
    enum class HttpMethod {
        GET, POST, DELETE, PUT,
    }

}