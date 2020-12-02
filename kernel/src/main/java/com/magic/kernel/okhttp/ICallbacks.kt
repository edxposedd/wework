package com.magic.kernel.okhttp

/** 上传进度回调 */
typealias IProgressRequestCallback = (bytesWriten: Long, bytesTotal: Long, done: Boolean) -> Unit

/** 下载进度回调 */
typealias IProgressResponseCallback = (bytesRead: Long, bytesTotal: Long, done: Boolean) -> Unit

/** 下载回调 */
typealias IDownloadCallback = (localPath: String?, type: IHttpConfigs.Type) -> Unit

/** 下载回调 */
typealias IDownloadCallback2 = (bArr: ByteArray?, localPath: String?, type: IHttpConfigs.Type) -> Unit

