package com.magic.wework.apis.com.tencent.wework.foundation.logic

import de.robv.android.xposed.XposedHelpers
import com.magic.wework.mirror.com.tencent.wework.foundation.logic.Classes.Application
import com.magic.wework.mirror.com.tencent.wework.foundation.logic.Methods

/**
 * com.tencent.wework.foundation.logic.Application
 */
object Application {

    fun getInstance(): Any =
        XposedHelpers.callStaticMethod(Application, Methods.Application.getInstance.name)

}