package com.magic.kernel.utils

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader

object CmdUtil {

    private const val CMD_SU = "su"
    private const val CMD_SH = "sh"
    private const val CMD_EXIT = "exit\n"
    private const val CMD_LINE_END = "\n"

    data class Result(var result: Int, var successMsg: String? = null, var errorMsg: String? = null)

    val isRoot: Boolean
        get() = exec(command = "echo root", isNeedResultMsg = false).result == 0

    fun exec(command: String, isRoot: Boolean = true, isNeedResultMsg: Boolean = true): Result =
        exec(listOf(command), isRoot, isNeedResultMsg)

    fun exec(commands: List<String>?, isRoot: Boolean = true, isNeedResultMsg: Boolean = true): Result =
        exec((commands ?: listOf()).toTypedArray(), isRoot, isNeedResultMsg)

    fun exec(commands: Array<String>?, isRoot: Boolean, isNeedResultMsg: Boolean = true): Result {
        var result = -1
        if (commands == null || commands.isEmpty()) {
            return Result(result, null, null)
        }
        var process: Process? = null
        var successResult: BufferedReader? = null
        var errorResult: BufferedReader? = null
        var successMsg: StringBuilder? = null
        var errorMsg: StringBuilder? = null
        var os: DataOutputStream? = null
        try {
            process = Runtime.getRuntime().exec(if (isRoot) CMD_SU else CMD_SH)
            os = DataOutputStream(process.outputStream)
            for (command in commands) {
                if (command == null) {
                    continue
                }
                // donnot use os.writeBytes(commmand), avoid chinese charset error
                os.write(command.toByteArray())
                os.writeBytes(CMD_LINE_END)
                os.flush()
            }
            os.writeBytes(CMD_EXIT)
            os.flush()
            result = process.waitFor()
            // get command result
            if (isNeedResultMsg) {
                successMsg = StringBuilder()
                errorMsg = StringBuilder()
                successResult = BufferedReader(InputStreamReader(process.inputStream))
                errorResult = BufferedReader(InputStreamReader(process.errorStream))
                var s: String?
                while (successResult.readLine().also { s = it } != null) {
                    successMsg.append(s)
                }
                while (errorResult.readLine().also { s = it } != null) {
                    errorMsg.append(s)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                os?.close()
                successResult?.close()
                errorResult?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            process?.destroy()
        }
        return Result(
            result,
            successMsg?.toString(),
            errorMsg?.toString()
        )
    }
}