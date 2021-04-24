package com.magic.wework.apis.com.tencent.wework.foundation.model

import com.magic.wework.mirror.com.tencent.wework.foundation.model.Methods
import de.robv.android.xposed.XposedHelpers

/**
 * @param original com.tencent.wework.foundation.model.Message
 */
data class Message(var original: Any) {

    companion object {

        /**
         * @return
         */
        fun getInfo(original: Any): Any =
            XposedHelpers.callMethod(original, Methods.Message.getInfo.name)

    }

}