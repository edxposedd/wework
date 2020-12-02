package com.magic.kernel.core

import android.util.Log
import com.magic.kernel.utils.XposedUtil
import de.robv.android.xposed.XC_MethodHook
import java.util.concurrent.ConcurrentHashMap
import com.magic.kernel.helper.TryHelper.tryVerbosely
import com.magic.kernel.helper.ReflecterHelper.findMethodsByExactName
import com.magic.kernel.utils.ParallelUtil.parallelForEach
import de.robv.android.xposed.XposedHelpers
import java.lang.reflect.Method

abstract class HookerCenter : IHookerProvider {

    abstract val interfaces: List<Class<*>>

    private val observers: MutableMap<String, Set<Any>> = ConcurrentHashMap()

    private fun Any.hasEvent(event: String) =
        this::class.java.declaredMethods.any { it.name == event }

    private fun register(event: String, observer: Any) {
        if (observer.hasEvent(event)) {
            val hooker = provideEventHooker(event)
            if (hooker != null && !hooker.hasHooked) {
                XposedUtil.postHooker(hooker)
            }
            val existing = observers[event] ?: emptySet()
            observers[event] = existing + observer
        }
    }

    fun register(iClazz: Class<*>, plugin: Any) {
        iClazz.methods.forEach { method ->
            register(method.name, plugin)
        }
    }

    fun findObservers(event: String): Set<Any>? = observers[event]

    /**
     * 通知所有正在观察某个事件的观察者
     *
     * @param event 具体发生的事件
     * @param action 对观察者进行通知的回调函数
     */
    inline fun notify(event: String, action: (Any) -> Unit) {
        findObservers(event)?.forEach {
            tryVerbosely { action(it) }
        }
    }

    fun iMethodNotifyHooker(
        clazz: Class<*>?, method: Method?,
        iClazz: Class<*>?, iMethodBefore: String? = null, iMethodAfter: String? = null,
        needObject: Boolean = false, needResult: Boolean = false, vararg parameterTypes: Class<*>
    ): Hooker =
        iMethodHooker(
            clazz, method?.name,
            iClazz, iMethodBefore, iMethodAfter,
            needObject, needResult, "notify", *parameterTypes
        )

    fun iMethodNotifyHooker(
        clazz: Class<*>?, method: String?,
        iClazz: Class<*>?, iMethodBefore: String? = null, iMethodAfter: String? = null,
        needObject: Boolean = false, needResult: Boolean = false, vararg parameterTypes: Class<*>
    ): Hooker =
        iMethodHooker(
            clazz, method,
            iClazz, iMethodBefore, iMethodAfter,
            needObject, needResult, "notify", *parameterTypes
        )
    /**
     * 通知所有正在观察某个事件的观察者(并行）
     *
     * @param event 具体发生的事件
     * @param action 对观察者进行通知的回调函数
     */
    inline fun notifyParallel(event: String, crossinline action: (Any) -> Unit) {
        findObservers(event)?.parallelForEach {
            tryVerbosely { action(it) }
        }
    }

    /**
     * 通知所有正在观察某个事件的观察者, 并收集它们的反馈
     *
     * @param event 具体发生的事件
     * @param action 对观察者进行通知的回调函数
     */
    inline fun <T : Any> notifyForResults(event: String, action: (Any) -> T?): List<T> {
        return findObservers(event)?.mapNotNull {
            tryVerbosely { action(it) }
        } ?: emptyList()
    }

    /**
     * 通知所有正在观察某个事件的观察者, 并收集它们的反馈, 以确认是否需要拦截该事件
     *
     * 如果有任何一个观察者返回了 true, 我们就认定当前事件是一个需要被拦截的事件. 例如当微信写文件的时候, 某个观察者
     * 检查过文件路径后返回了 true, 那么框架就会拦截这次写文件操作, 向微信返回一个默认值
     *
     * @param event 具体发生的事件
     * @param param 拦截函数调用后得到的 [XC_MethodHook.MethodHookParam] 对象
     * @param default 跳过函数调用之后, 仍然需要向 caller 提供一个返回值
     * @param action 对观察者进行通知的回调函数
     */
    inline fun notifyForBypassFlags(
        event: String,
        param: XC_MethodHook.MethodHookParam,
        default: Any? = null,
        action: (Any) -> Boolean
    ) {
        val shouldBypass = notifyForResults(event, action).any()
        if (shouldBypass) {
            param.result = default
        }
    }

    fun iMethodNotifyForBypassFlagsHooker(
        clazz: Class<*>?, method: String?,
        iClazz: Class<*>?, iMethodBefore: String? = null, iMethodAfter: String? = null,
        needObject: Boolean = false, needResult: Boolean = false, vararg parameterTypes: Class<*>
    ): Hooker =
        iMethodHooker(
            clazz, method,
            iClazz, iMethodBefore, iMethodAfter,
            needObject, needResult, "notifyForBypassFlags", *parameterTypes
        )


    /**
     * 通知所有正在观察某个事件的观察者, 并收集它们的反馈, 以确认该对这次事件采取什么操作
     *
     * 在获取了观察者建议的操作之后, 我们会对这些操作的优先级进行排序, 从优先级最高的操作中选择一个予以采纳
     *
     * @param event 具体发生的事件
     * @param param 拦截函数调用后得到的 [XC_MethodHook.MethodHookParam] 对象
     * @param action 对观察者进行通知的回调函数
     */
    inline fun notifyForOperations(
        event: String,
        param: XC_MethodHook.MethodHookParam,
        action: (Any) -> Operation<*>
    ) {
        val operations = notifyForResults(event, action)
        val result = operations.filter { it.returnEarly }.maxBy { it.priority }
        if (result != null) {
            if (result.value != null) {
                param.result = result.value
            }
            if (result.error != null) {
                param.throwable = result.error
            }
        }
    }

    /**
     * hookMethod
     */
    private fun iMethodHooker(
        clazz: Class<*>?, method: String?,
        iClazz: Class<*>?, iMethodBefore: String? = null, iMethodAfter: String? = null,
        needObject: Boolean = false, needResult: Boolean = false, notifyType: String = "notify",
        vararg parameterTypes: Class<*>
    ): Hooker {
        return Hooker {
            if (clazz == null || method == null) return@Hooker
            XposedHelpers.findAndHookMethod(clazz, method, *parameterTypes,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam?) {
                        if (iClazz == null || iMethodBefore == null) return
                        iInvoke(iClazz, iMethodBefore, needObject, needResult, param, notifyType)
                    }

                    override fun afterHookedMethod(param: MethodHookParam?) {
                        Log.e(HookerCenter::class.java.name, "afterHookedMethod: ${param}")
                        if (iClazz == null || iMethodAfter == null) return
                        iInvoke(iClazz, iMethodAfter, needObject, needResult, param, notifyType)
                    }
                })
        }
    }

    /**
     * hookMethod
     */
    private fun iConstructorHooker(
        clazz: Class<*>?,
        iClazz: Class<*>?, iMethodBefore: String? = null, iMethodAfter: String? = null,
        needObject: Boolean = false, needResult: Boolean = false, notifyType: String = "notify",
        vararg parameterTypes: Class<*>
    ): Hooker {
        return Hooker {
            if (clazz == null) return@Hooker
            XposedHelpers.findAndHookConstructor(clazz, *parameterTypes,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam?) {
                        if (iClazz == null || iMethodBefore == null) return
                        iInvoke(iClazz, iMethodBefore, needObject, needResult, param, notifyType)
                    }

                    override fun afterHookedMethod(param: MethodHookParam?) {
                        if (iClazz == null || iMethodAfter == null) return
                        Log.e(
                            HookerCenter::class.java.name,
                            "afterHook   ${clazz.name}  :  $iMethodAfter"
                        )
                        iInvoke(iClazz, iMethodAfter, needObject, needResult, param, notifyType)
                    }
                })
        }
    }

    /**
     * 调用Ixxx回调方法
     */
    private fun iInvoke(
        iClazz: Class<*>, method: String, needObject: Boolean, needResult: Boolean,
        param: XC_MethodHook.MethodHookParam?, notifyType: String
    ) {
        val iMethod = findMethodsByExactName(iClazz, method).firstOrNull()
        var args = param?.args.orEmpty().toList().toTypedArray().toMutableList()
        if (needObject && param?.thisObject != null) {
            args.add(0, param!!.thisObject)
        }
        if (needResult) {
            args.add(param!!.result)
        }
        when (notifyType) {
            "notify" ->
                notify(method) {
                    iMethod?.invoke(it, *args.toTypedArray())
                }
            else -> {}
        }
    }
}