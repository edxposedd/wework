package com.magic.wework.mirror.com.tencent.wework.foundation.model

import com.magic.kernel.MagicGlobal
import com.magic.kernel.MagicGlobal.lazy
import com.magic.kernel.MagicGlobal.classLoader
import com.magic.kernel.helper.ReflecterHelper.findClassIfExists

object Classes {
    private val packageName = "${MagicGlobal.packageName}.${javaClass.name.replaceBeforeLast("foundation", "")}".removeSuffix(".${javaClass.simpleName}")

    val Conversation: Class<*> by lazy("${javaClass.name}.Conversation") {
        findClassIfExists("$packageName.Conversation", classLoader!!)
    }

    val Message: Class<*> by lazy("${javaClass.name}.Message") {
        findClassIfExists("$packageName.Message", classLoader!!)
    }

}

