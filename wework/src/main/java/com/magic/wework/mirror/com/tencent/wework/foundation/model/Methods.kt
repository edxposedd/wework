package com.magic.wework.mirror.com.tencent.wework.foundation.model

import com.magic.kernel.MagicGlobal.lazy
import com.magic.kernel.helper.ReflecterHelper.findMethodIfExists
import java.lang.reflect.Method

object Methods {

    object Conversation {

        val getInfo: Method by lazy("${javaClass.name}.getInfo") {
            findMethodIfExists(Classes.Conversation, "getInfo")
        }

    }

    object Message {

        val getInfo: Method by lazy("${javaClass.name}.getInfo") {
            findMethodIfExists(Classes.Message, "getInfo")
        }

    }

}