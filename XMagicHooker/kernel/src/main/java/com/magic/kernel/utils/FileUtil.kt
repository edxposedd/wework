package com.magic.kernel.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.os.SystemClock
import de.robv.android.xposed.XposedBridge
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

object FileUtil {

    @JvmStatic
    private fun isAccessExternal(context: Context): Boolean =
        (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                && context.externalCacheDir != null)

    @JvmStatic
    fun getCacheDir(context: Context): String {
        val cacheDir = File(if (isAccessExternal(context)) context.externalCacheDir?.path else context.cacheDir?.path)
        if (!cacheDir.exists()) cacheDir.mkdirs()
        return cacheDir.path
    }

    @JvmStatic
    fun mkDirIfNotExists(context: Context, dirPath: String?): String {
        val cacheDir = getCacheDir(context)
        val file = File(cacheDir, dirPath)
        if (!file.exists()) file.mkdirs()
        return file.path
    }

    @JvmStatic
    fun writeBytesToDisk(path: String, content: ByteArray) {
        val file = File(path).also { it.parentFile.mkdirs() }
        val fout = FileOutputStream(file)
        BufferedOutputStream(fout).use { it.write(content) }
    }

    @JvmStatic
    fun readBytesFromDisk(path: String): ByteArray {
        val fin = FileInputStream(path)
        return BufferedInputStream(fin).use { it.readBytes() }
    }

    @JvmStatic
    fun writeObjectToDisk(path: String, obj: Serializable) {
        val out = ByteArrayOutputStream()
        ObjectOutputStream(out).use {
            it.writeObject(obj)
        }
        writeBytesToDisk(path, out.toByteArray())
    }

    @JvmStatic
    fun readObjectFromDisk(path: String): Any? {
        val bytes = readBytesFromDisk(path)
        val ins = ByteArrayInputStream(bytes)
        return ObjectInputStream(ins).use {
            it.readObject()
        }
    }

    @JvmStatic
    fun writeInputStreamToDisk(path: String, ins: InputStream, bufferSize: Int = 8192) {
        val file = File(path)
        file.parentFile.mkdirs()
        val fout = FileOutputStream(file)
        BufferedOutputStream(fout).use {
            val buffer = ByteArray(bufferSize)
            var length = ins.read(buffer)
            while (length != -1) {
                it.write(buffer, 0, length)
                length = ins.read(buffer)
            }
        }
    }

    @JvmStatic
    fun writeBitmapToDisk(path: String, bitmap: Bitmap) {
        val out = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        writeBytesToDisk(path, out.toByteArray())
    }

    @JvmStatic
    inline fun writeOnce(path: String, writeCallback: (String) -> Unit) {
        val file = File(path)
        if (!file.exists()) {
            writeCallback(path)
            return
        }
        val bootAt = System.currentTimeMillis() - SystemClock.elapsedRealtime()
        val modifiedAt = file.lastModified()
        if (modifiedAt < bootAt) {
            writeCallback(path)
        }
    }

    @JvmStatic
    fun createTimeTag(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd-HHmmss", Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }

    @JvmStatic
    fun notifyNewMediaFile(path: String, context: Context?) {
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        context?.sendBroadcast(intent.apply {
            data = Uri.fromFile(File(path))
        })
    }

    fun copyAssets(context: Context, appDir: String, dir: String, cover: Boolean = false) {
        val assetManager = context.assets
        var files: Array<String>? = null
        try {
            files = assetManager.list(dir)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (files != null) {
            File(appDir + File.separator + dir + File.separator).mkdirs()
            for (filename in files) {
                var `in`: InputStream? = null
                var out: OutputStream? = null
                try {
                    `in` = assetManager.open(dir + File.separator + filename)
                    val outFile = File(appDir + File.separator + dir + File.separator + filename)
                    if (outFile.exists()) {
                        if (!cover) {
                            continue
                        } else {
                            outFile.delete()
                        }
                    }
                    out = FileOutputStream(outFile)
                    copyFile(`in`, out)
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    if (`in` != null) {
                        try {
                            `in`!!.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    }
                    if (out != null) {
                        try {
                            out!!.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    @Throws(IOException::class)
    fun copyFile(`in`: InputStream, out: OutputStream) {
        val buffer = ByteArray(1024)
        var read: Int
        do {
            read = `in`.read(buffer)
            if (read == -1) {
                break
            }
            out.write(buffer, 0, read)
        } while (true)
    }

    fun write(fileName: String, content: String, append: Boolean = false) {
        var writer: FileWriter? = null
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = FileWriter(fileName, append)
            writer.write(content)
        } catch (e: IOException) {
            XposedBridge.log(e)
        } finally {
            try {
                writer?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @Throws(IOException::class)
    fun bytesToFile(bytes: ByteArray?, result: File?) {
        val bos =
            BufferedOutputStream(FileOutputStream(result))
        bos.write(bytes)
        bos.flush()
        bos.close()
    }

    @Throws(IOException::class)
    fun toByteArray(input: InputStream): ByteArray {
        val output = ByteArrayOutputStream()
        copy(input, output)
        return output.toByteArray()
    }

    @Throws(IOException::class)
    fun copy(input: InputStream, output: OutputStream): Int {
        val count = copyLarge(input, output)
        return if (count > Int.MAX_VALUE) {
            -1
        } else count.toInt()
    }

    @Throws(IOException::class)
    fun copy(sourcePath: String, destPath: String): Int {
        val sourceFile = File(sourcePath)
        if (!sourceFile.exists()) return -1
        val destFile = File(destPath).also { if (it.parentFile?.exists() == false) it.parentFile?.mkdirs() }
        return copy(FileInputStream(sourceFile), FileOutputStream(destFile))
    }

    private const val DEFAULT_BUFFER_SIZE = 1024 * 4
    @Throws(IOException::class)
    fun copyLarge(input: InputStream, output: OutputStream): Long {
        val buffer =
            ByteArray(DEFAULT_BUFFER_SIZE)
        var count: Long = 0
        var n = 0
        while (-1 != input.read(buffer).also { n = it }) {
            output.write(buffer, 0, n)
            count += n.toLong()
        }
        return count
    }
}
