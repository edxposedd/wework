package com.magic.wework.mirror.com.tencent.wework.foundation.logic

import com.magic.kernel.MagicGlobal
import com.magic.kernel.MagicGlobal.lazy
import com.magic.kernel.MagicGlobal.classLoader
import com.magic.kernel.helper.ReflecterHelper.findClassIfExists

object Classes {
    private val packageName =
        "${MagicGlobal.packageName}.${javaClass.name.replaceBeforeLast("foundation", "")}".removeSuffix(".${javaClass.simpleName}")

    val Application: Class<*> by lazy("${javaClass.name}.Application") {
        findClassIfExists("$packageName.Application", classLoader!!)
    }

    val ConversationService: Class<*> by lazy("${javaClass.name}.ConversationService") {
        findClassIfExists("$packageName.ConversationService", classLoader!!)
    }

}

