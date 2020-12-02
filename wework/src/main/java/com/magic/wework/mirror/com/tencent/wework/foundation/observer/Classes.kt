package com.magic.wework.mirror.com.tencent.wework.foundation.observer

import com.magic.kernel.MagicGlobal
import com.magic.kernel.MagicGlobal.lazy
import com.magic.kernel.MagicGlobal.classes
import com.magic.kernel.MagicGlobal.classLoader
import com.magic.kernel.helper.ReflecterHelper.findClassIfExists
import com.magic.kernel.helper.ReflecterHelper.findClassesInPackage

object Classes {
    private val packageName =
        "${MagicGlobal.packageName}.${javaClass.name.replaceBeforeLast("foundation", "").removeSuffix(".${javaClass.simpleName}")}"

    val IConversationListObserver: Class<*> by lazy("${javaClass.name}.IConversationListObserver") {
        findClassIfExists("$packageName.IConversationListObserver", classLoader!!)
    }

    val IConversationListObserverImpl: Class<*> by lazy("${javaClass.name}.icloi_fct$32hch$44") {
        findClassesInPackage(classLoader!!, classes!!, "")
            .filterByInterfaces(IConversationListObserver)
            .firstOrNull()
    }

    val IConversationObserver: Class<*> by lazy("${javaClass.name}.IConversationObserver") {
        findClassIfExists("$packageName.IConversationObserver", classLoader!!)
    }

    val IConversationObserverImpl: Class<*> by lazy("${javaClass.canonicalName}.icoifct$22") {
        findClassesInPackage(classLoader!!, classes!!, "")
            .filterByInterfaces(IConversationObserver)
            .firstOrNull()
    }

}

