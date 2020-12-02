package com.magic.wework.apis.com.tencent.wework.foundation.logic

import com.magic.wework.mirror.com.tencent.wework.foundation.logic.Classes.ConversationService
import com.magic.wework.mirror.com.tencent.wework.foundation.logic.Methods
import de.robv.android.xposed.XposedHelpers

object ConversationService {

    /**
     * @return
     */
    private fun getService(): Any =
        XposedHelpers.callStaticMethod(
            ConversationService,
            Methods.ConversationService.getService.name
        )

    /**
     * 获取所有会话
     * @param conversation  可以为空
     * @param type 类型 -1 为全部
     *
     * @return []com.tencent.wework.foundation.model.Conversation
     */

    /**
     * @param com.tencent.wework.foundation.observer.IConversationListObserver
     */
    fun addObserver(iConversationListObserver: Any) =
        XposedHelpers.callMethod(
            getService(),
            Methods.ConversationService.AddObserver.name,
            iConversationListObserver
        )

}