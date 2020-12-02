package com.magic.wework.hookers

import android.util.Log
import com.magic.kernel.MagicGlobal
import com.magic.kernel.core.HookerCenter
import com.magic.kernel.core.Hooker
import com.magic.kernel.MagicGlobal.classLoader
import com.magic.kernel.core.Clazz
import com.magic.kernel.helper.ReflecterHelper.findMethodsByExactName
import com.magic.wework.apis.com.tencent.wework.foundation.logic.ConversationService
import com.magic.wework.hookers.interfaces.IConversationHooker
import com.magic.wework.mirror.com.tencent.wework.foundation.model.Classes.Conversation
import com.magic.wework.mirror.com.tencent.wework.foundation.model.Classes.Message
import com.magic.wework.mirror.com.tencent.wework.foundation.observer.Classes.IConversationObserverImpl
import com.magic.wework.mirror.com.tencent.wework.foundation.observer.Classes.IConversationListObserver
import com.magic.wework.mirror.com.tencent.wework.foundation.observer.Methods.IConversationObserver
import java.lang.reflect.Array
import java.lang.reflect.Proxy

object ConversationHookers : HookerCenter() {

    override val interfaces: List<Class<*>>
        get() = listOf(IConversationHooker::class.java)

    override fun provideEventHooker(event: String): Hooker? {
        return when (event) {
            "onReconvergeConversation",
            "onReloadConvsProperty",
            "onSyncStateChanged",
            "onAddConversations",
            "onExitConversation" ->
                iConversationListObserverHooker

            "onSetReadReceipt",
            "onAddMembers",
            "onChangeOwner",
            "onDraftDidChange",
            "onModifyName",
            "onPropertyChanged",
            "onRemoveAllMessages",
            "onRemoveMembers",
            "onSetAllBan",
            "onSetCollect",
            "onSetConfirmAddMember",
            "onSetMembersBan",
            "onSetOwnerManager",
            "onSetShield",
            "onSetTop",
            "onTypingStateUpdate" ->
                iMethodNotifyHooker(
                    clazz = IConversationObserverImpl,
                    method = IConversationObserver.getMethodByName(event),
                    iClazz = IConversationHooker::class.java,
                    iMethodAfter = event,
                    parameterTypes = *arrayOf(Conversation)
                )
            "onAddMessages" ->
                iMethodNotifyHooker(
                    clazz = IConversationObserverImpl,
                    method = IConversationObserver.getMethodByName(event),
                    iClazz = IConversationHooker::class.java,
                    iMethodAfter = event,
                    parameterTypes = *arrayOf(Conversation, Array.newInstance(Message, 0).javaClass, Clazz.Boolean)
                )
            "onMessageStateChange" ->
                iMethodNotifyHooker(
                    clazz = IConversationObserverImpl,
                    method = IConversationObserver.getMethodByName(event),
                    iClazz = IConversationHooker::class.java,
                    iMethodAfter = event,
                    parameterTypes = *arrayOf(Conversation, Message, Clazz.Int)
                )
            "onUnReadCountChanged" ->
                iMethodNotifyHooker(
                    clazz = IConversationObserverImpl,
                    method = IConversationObserver.getMethodByName(event),
                    iClazz = IConversationHooker::class.java,
                    iMethodAfter = event,
                    parameterTypes = *arrayOf(Conversation, Clazz.Int, Clazz.Int)
                )
            else -> {
                if (MagicGlobal.unitTestMode) {
                    throw IllegalArgumentException("Unknown event: $event")
                }
                Log.e(ConversationHookers::class.java.name, "function not found: ${event}")
                return null
            }
        }
    }

    private val iConversationListObserverHooker = Hooker {
        val observer = Proxy.newProxyInstance(
            classLoader,
            arrayOf(IConversationListObserver)
        ) { _, method, args ->
            val iMethodName = (method.name)
            notify(iMethodName) {
                val iMethod = findMethodsByExactName(
                    IConversationHooker::class.java,
                    iMethodName
                ).firstOrNull()
                iMethod?.invoke(it, *args.orEmpty())
            }
        }
        ConversationService.addObserver(observer)
    }

}