package com.magic.kernel.helper

import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

/** ------- String Extension ------ */
fun String.firstLetterToLowerCase() =
    if (this.isEmpty()) this else this.substring(0, 1).toLowerCase() + this.substring(1)

fun String.MD5() = toHexString(MessageDigest.getInstance(("MD5")).digest(this.toByteArray()))

fun String.SHA1() = toHexString(MessageDigest.getInstance("SHA-1").digest(this.toByteArray()))

fun String.SHA256() =toHexString( MessageDigest.getInstance("SHA-256").digest(this.toByteArray()))

/** ------- ByteArray Extension ------ */
fun ByteArray.MD5() = toHexString(MessageDigest.getInstance("MD5").digest(this))

fun ByteArray.SHA1() = toHexString(MessageDigest.getInstance("SHA-1").digest(this))

fun ByteArray.SHA256() = toHexString(MessageDigest.getInstance("SHA-256").digest(this))

/** ------- Date Extension ------ */
fun Date.defaultFormat() = SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(this)

fun toHexString(bytes: ByteArray) =
    with(StringBuffer()) {
        bytes.forEach {
            val hex = it.toInt() and 0xFF
            val hexString = Integer.toHexString(hex)
            when (hexString.length) {
                1 -> this.append("0").append(hexString)
                else -> this.append(hexString)
            }
        }
        return@with this.toString()
    }
