package cc.sdkutil.controller.util

import android.util.Log

/**
 * Created by wangcong on 14-12-26.
 * 在控制台打印Log,发布版本时在Application中设置答应Log 为false
 */
object LogUtil {

    private var isDebug = true

    private const val TAG = "LogUtil"
    fun i(msg: String?) {
        if (isDebug) Log.i(TAG, msg)
    }

    fun d(msg: String?) {
        if (isDebug) Log.d(TAG, msg)
    }

    fun e(msg: String?) {
        if (isDebug) Log.e(TAG, msg)
    }

    fun v(msg: String?) {
        if (isDebug) Log.v(TAG, msg)
    }

    fun i(_class: Class<*>, msg: String?) {
        if (isDebug) Log.i(_class.getName(), msg)
    }

    fun d(_class: Class<*>, msg: String?) {
        if (isDebug) Log.d(_class.getName(), msg)
    }

    fun e(_class: Class<*>, msg: String?) {
        if (isDebug) Log.e(_class.getName(), msg)
    }

    fun v(_class: Class<*>, msg: String?) {
        if (isDebug) Log.v(_class.getName(), msg)
    }

    fun i(tag: String?, msg: String?) {
        if (isDebug) Log.i(tag, msg)
    }

    fun d(tag: String?, msg: String?) {
        if (isDebug) Log.d(tag, msg)
    }

    fun d(_class: Class<*>?, methodName: String?, msg: String?) {
        if (isDebug && (_class != null || methodName != null) && msg != null) Log.d(_class?.name + "--" + methodName, msg)
    }

    fun e(tag: String?, msg: String?) {
        if (isDebug) Log.e(tag, msg)
    }

    fun v(tag: String?, msg: String?) {
        if (isDebug) Log.v(tag, msg)
    }

    /**
     * 此方法用于框架内部调试
     * @param debug
     * @param clazz
     * @param method
     * @param msg
     */
    fun d(debug: Boolean, clazz: Class<*>, method: String, msg: String?) {
        if (!isDebug) return
        if (debug && msg != null) Log.d(clazz.getName().toString() + " -- " + method, msg)
    }

    /**
     *
     * @param debug
     * @param clazz
     * @param msg
     */
    fun d(debug: Boolean, clazz: Class<*>, msg: String?) {
        if (!isDebug) return
        if (debug && msg != null) Log.d(clazz.getName(), msg)
    }
}