package com.magic.kernel.async

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message

/**
 * 用于异步处理消息
 */
open class AsyncHandler : Handler() {
    private val mWorkThreadHandler: Handler
    private var mWorkLooper: Looper? = null

    companion object {
        const val TAG = "AsyncHandler"
    }

    /**
     * 内部工作Handler，用于处理异步消息
     */
    private inner class WorkHandler(looper: Looper, private val replyHandler: Handler) :
        Handler(looper) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            // 处理异步消息
            onAsyncDeal(msg)
            // 处理完成后重新将数据发送到之前的线程处理
            val reply = Message.obtain()
            reply.copyFrom(msg)
            reply.target = replyHandler
            replyHandler.post { onPostComplete(reply) }
        }

    }

    private fun createHandler(looper: Looper): Handler {
        return WorkHandler(looper, this)
    }

    /**
     * 移除还未开始的操作
     * @param what  msg.what
     */
    fun cancelOperation(what: Int) {
        mWorkThreadHandler.removeMessages(what)
    }

    /**
     * 移除还未开始的操作
     * @param what msg.what
     * @param obj  msg.obj
     */
    fun cancelOperation(what: Int, obj: Any?) {
        mWorkThreadHandler.removeMessages(what, obj)
    }

    /**
     * 发送消息
     * @param msg
     */
    fun sendAsyncMessage(msg: Message) {
        sendAsyncMessageDelay(msg, 0)
    }

    /**
     * 延时发送异步消息
     * @param msg          消息内容
     * @param delayMillis  多少时长后发送
     */
    fun sendAsyncMessageDelay(msg: Message, delayMillis: Long) {
        val workMsg = Message.obtain()
        workMsg.copyFrom(msg)
        mWorkThreadHandler.sendMessageDelayed(workMsg, delayMillis)
    }

    /**
     * 延时循环发送异步消息
     * @param asyncMSg            消息内容等
     * @param delayMillis     多少时长后发送
     * @param intervalMillis  间隔多少时长
     */
    fun sendScheduleAsyncMessage(msg: Message, delayMillis: Long, intervalMillis: Long) {
        mWorkThreadHandler.postDelayed({
            sendAsyncMessage(msg)
            mWorkThreadHandler.post {
                sendScheduleAsyncMessage(
                    msg,
                    delayMillis,
                    intervalMillis
                )
            }
        }, delayMillis)
    }

    /**
     * 异步处理消息，该消息中所有参数将原本返回至onPostComplete中，也可以在某些处理完成后重赋值该asyncMsg，
     * 如果需要监听处理完成的方法，请重写onPostComplete
     * @param msg
     */
    protected open fun onAsyncDeal(msg: Message) {}

    /**
     * 异步处理完成后执行方法
     * @param msg
     */
    protected open fun onPostComplete(msg: Message) {}

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        //        onAsyncComplete(msg);
    }

    init {
        synchronized(AsyncHandler::class.java) {
            val thread = HandlerThread(TAG)
            thread.start()
            mWorkLooper = thread.looper
        }
        mWorkThreadHandler = createHandler(mWorkLooper!!)
    }
}