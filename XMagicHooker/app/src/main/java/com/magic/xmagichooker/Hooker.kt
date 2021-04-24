package com.magic.xmagichooker

import android.app.Application
import android.content.Context
import android.util.Log
import com.magic.kernel.MagicGlobal
import com.magic.kernel.MagicHooker
import de.robv.android.xposed.callbacks.XC_LoadPackage
import com.magic.kernel.helper.TryHelper.tryVerbosely
import com.magic.shared.apis.SharedEngine
import com.magic.wework.apis.WwEngine
import dalvik.system.PathClassLoader
import de.robv.android.xposed.*
import java.io.File

class Hooker : IXposedHookLoadPackage, IXposedHookZygoteInit {

    private val TARGET_PACKAGE = "com.magic.xmagichooker"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        tryVerbosely {
            when (lpparam.packageName) {
                TARGET_PACKAGE ->
                    hookAttachBaseContext(lpparam.classLoader) {
                        hookLoadHooker(lpparam.classLoader)
                    }
                else -> if (MagicHooker.isImportantWechatProcess(lpparam)) {
                    hookAttachBaseContext(lpparam.classLoader) {
                        hookTencent(lpparam, it)
                    }
                }
            }
        }
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam?) {
        Log.e(Hooker::class.java.name, "initZygote   ${startupParam?.modulePath}   ${startupParam?.startsSystemServer}")
    }

    private fun hookAttachBaseContext(classLoader: ClassLoader, callback: (Context) -> Unit) {
        XposedHelpers.findAndHookMethod(
            "android.content.ContextWrapper",
            classLoader,
            "attachBaseContext",
            Context::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    callback(param?.thisObject as? Application ?: return)
                }
            })
    }

    private fun hookLoadHooker(classLoader: ClassLoader) {
        XposedHelpers.findAndHookMethod(
            "$TARGET_PACKAGE.MainActivity", classLoader,
            "checkHook", object : XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam): Any = true
            })
    }

    private fun hookTencent(lpparam: XC_LoadPackage.LoadPackageParam, context: Context) {
        when (lpparam.packageName) {
            "com.tencent.wework" -> {
                MagicHooker.startup(
                    lpparam = lpparam,
                    plugins = listOf(Plugins),
                    centers = WwEngine.hookerCenters + SharedEngine.hookerCenters
                )
            }
            "com.tencent.mm" -> {
                Log.e(Hooker::class.java.name, "开始启动个人微信插件")
            }
        }
    }

}