package com.magic.kernel.media.image

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.Log
import com.magic.kernel.cache.LRUCache
import java.io.*
import java.nio.ByteBuffer
import java.nio.channels.Channels
import java.nio.channels.ReadableByteChannel

object BitmapHelper {

    /**
     * 获取到图片的方向
     * @param path   图片路径
     * @return
     */
    fun getDegress(path: String?): Float {
        var degree = 0F
        try {
            val exifInterface = ExifInterface(path)
            val orientation: Int = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90F
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180F
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270F
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return degree
    }

    /**
     * 旋转图片
     * @param bitmap  图片
     * @param degress  旋转角度
     * @return
     */
    fun rotateBitmap(bitmap: Bitmap?, degress: Float): Bitmap? {
        var bitmap: Bitmap? = bitmap
        if (bitmap != null) {
            val m = Matrix()
            m.postRotate(degress)
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
            return bitmap
        }
        return bitmap
    }

    /**
     * 计算需要缩放的SampleSize
     * @param options
     * @param rqsW
     * @param rqsH
     * @return
     */
    fun caculateInSampleSize(options: Options, rqsW: Int, rqsH: Int): Int {
        val height: Int = options.outHeight
        val width: Int = options.outWidth
        var inSampleSize = 1
        if (rqsW == 0 || rqsH == 0) return 1
        if (height > rqsH || width > rqsW) {
            val heightRatio: Int = Math.round(height.toFloat() / rqsH.toFloat())
            val widthRatio: Int = Math.round(width.toFloat() / rqsW.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        return inSampleSize
    }

    /**
     * 压缩指定路径的图片，并得到图片对象
     * @param path
     * @param rqsW
     * @param rqsH
     * @return
     */
    fun compressBitmap(path: String?, rqsW: Int, rqsH: Int): Bitmap {
        val options = Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        options.inSampleSize = caculateInSampleSize(options, rqsW, rqsH)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(path, options)
    }

    /**
     *
     * @param descriptor
     * @param resW
     * @param resH
     * @return
     */
    fun compressBitmap(descriptor: FileDescriptor?, resW: Int, resH: Int): Bitmap {
        val options = Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFileDescriptor(descriptor, null, options)
        options.inSampleSize = caculateInSampleSize(options, resW, resH)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFileDescriptor(descriptor, null, options)
    }

    /**
     * 压缩指定路径图片，并将其保存在缓存目录中，通过isDelSrc判定是否删除源文件，并获取到缓存后的图片路径
     * @param srcPath
     * @param rqsW
     * @param rqsH
     * @param isDelSrc
     * @return
     */
    fun compressBitmap(srcPath: String?, rqsW: Int, rqsH: Int, isDelSrc: Boolean): String? {
        var bitmap: Bitmap? = compressBitmap(srcPath, rqsW, rqsH)
        val srcFile = File(srcPath)
        val desPath: String = LRUCache.cachePath("image", srcFile.name)
        val degree = getDegress(srcPath)
        return try {
            if (degree != 0F) bitmap = rotateBitmap(bitmap, degree)
            val file = File(desPath)
            val fos = FileOutputStream(file)
            bitmap!!.compress(Bitmap.CompressFormat.PNG, 70, fos)
            fos.close()
            if (isDelSrc) srcFile.deleteOnExit()
            desPath
        } catch (e: Exception) {
            Log.d(BitmapHelper.javaClass.name, "compressBitmap error: ${e.message}"); null
        }
    }

    /**
     * 压缩某个输入流中的图片，可以解决网络输入流压缩问题，并得到图片对象
     * @param is
     * @param reqsW
     * @param reqsH
     * @return
     */
    fun compressBitmap(`is`: InputStream, reqsW: Int, reqsH: Int): Bitmap? {
        return try {
            val baos = ByteArrayOutputStream()
            val channel: ReadableByteChannel = Channels.newChannel(`is`)
            val buffer: ByteBuffer = ByteBuffer.allocate(1024)
            while (channel.read(buffer) !== -1) {
                buffer.flip()
                while (buffer.hasRemaining()) baos.write(buffer.array())
                buffer.clear()
            }
            val bts: ByteArray = baos.toByteArray()
            val bitmap: Bitmap = compressBitmap(bts, reqsW, reqsH)
            `is`.close()
            channel.close()
            baos.close()
            bitmap
        } catch (e: Exception) {
            Log.d(BitmapHelper.javaClass.name, "compressBitmap-is-reqsw-reqsh error: ${e.message}")
            null
        }
    }

    /**
     * 压缩制定byte[]图片，并得到压缩后的图像
     * @param bts
     * @param reqsW
     * @param reqsH
     * @return
     */
    fun compressBitmap(bts: ByteArray, reqsW: Int, reqsH: Int): Bitmap {
        val options = Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeByteArray(bts, 0, bts.size, options)
        options.inSampleSize = caculateInSampleSize(options, reqsW, reqsH)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeByteArray(bts, 0, bts.size, options)
    }

    /**
     * 压缩已存在的图片对象，并返回压缩后的图片
     * @param bitmap
     * @param reqsW
     * @param reqsH
     * @return
     */
    fun compressBitmap(bitmap: Bitmap, reqsW: Int, reqsH: Int): Bitmap {
        return try {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val bts: ByteArray = baos.toByteArray()
            val res: Bitmap = compressBitmap(bts, reqsW, reqsH)
            baos.close()
            res
        } catch (e: IOException) {
            Log.d(BitmapHelper.javaClass.name, "compressBitmap-is-reqsw-reqsh error: ${e.message}")
            bitmap
        }
    }

    /**
     * 压缩资源图片，并返回图片对象
     * @param res [Resources]
     * @param resID
     * @param reqsW
     * @param reqsH
     * @return
     */
    fun compressBitmap(res: Resources?, resID: Int, reqsW: Int, reqsH: Int): Bitmap {
        val options = Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resID, options)
        options.inSampleSize = caculateInSampleSize(options, reqsW, reqsH)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(res, resID, options)
    }

    /**
     * 基于质量的压缩算法， 此方法未 解决压缩后图像失真问题
     * <br></br> 可先调用比例压缩适当压缩图片后，再调用此方法可解决上述问题
     * @param bitmap
     * @param maxBytes
     * @return
     */
    fun compressBitmap(bitmap: Bitmap, maxBytes: Long): Bitmap? {
        return try {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            var options = 90
            while (baos.toByteArray().size > maxBytes) {
                baos.reset()
                bitmap.compress(Bitmap.CompressFormat.PNG, options, baos)
                options -= 10
            }
            val bts: ByteArray = baos.toByteArray()
            val bmp: Bitmap = BitmapFactory.decodeByteArray(bts, 0, bts.size)
            baos.close()
            bmp
        } catch (e: IOException) {
            Log.d(BitmapHelper.javaClass.name, "compressBitmap-bitmap-maxbytes error: ${e.message}")
            null
        }
    }

    /**
     * 得到制定路径图片的options
     * @param srcPath
     * @return Options [Options]
     */
    fun getBitmapOptions(srcPath: String?): Options {
        val options = Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(srcPath, options)
        return options
    }

}