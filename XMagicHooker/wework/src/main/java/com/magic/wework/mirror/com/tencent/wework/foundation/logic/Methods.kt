package com.magic.wework.mirror.com.tencent.wework.foundation.logic

import com.magic.kernel.MagicGlobal.lazy
import com.magic.kernel.helper.ReflecterHelper.findMethodIfExists
import com.magic.kernel.helper.ReflecterHelper.findMethodsByExactParameters
import com.magic.wework.mirror.com.tencent.wework.foundation.observer.Classes.IConversationListObserver
import java.lang.reflect.Method

object Methods {

    /** --------- Application -------- */
    object Application {

        val getInstance: Method by lazy("${javaClass.name}.getInstance") {
            findMethodIfExists(Classes.Application, "getInstance")
        }

    }

    object ConversationService {

        val getService: Method by lazy("${javaClass.name}.gs") {
            findMethodsByExactParameters(
                Classes.ConversationService, Classes.ConversationService
            ).firstOrNull()
        }

        val AddObserver: Method by lazy("${javaClass.name}.ao") {
            findMethodsByExactParameters(
                Classes.ConversationService,
                null,
                IConversationListObserver
            ).firstOrNull()
        }

    }

}