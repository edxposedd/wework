package com.magic.wework.apis.com.tencent.wework.foundation.model

import com.magic.wework.mirror.com.tencent.wework.foundation.model.Methods
import de.robv.android.xposed.XposedHelpers

/**
 * @property original com.tencent.wework.foundation.model.Conversation
 */
data class Conversation(var original: Any) {

    /**
     * 针对
     */
    companion object {

        /**
         * @return
         */
        fun getInfo(original: Any): Any =
            XposedHelpers.callMethod(original, Methods.Conversation.getInfo.name)

    }

//    fun getInfo(): WwConversation.Conversation =
//        WwConversation.Conversation.parseFrom(Companion.getInfo(original))
//
//    fun getLocalId(): Long = getInfo().id
//
//    fun getFinancialDisagreeVids(): LongArray = Companion.getFinancialDisagreeVids(original)
//
//    fun getShowTime(): Long = Companion.getShowTime(original)
//
//    fun getSortTime(): Long = Companion.getSortTime(original)
//
//    fun containMember(userId: Long) = getUserList(longArrayOf(userId)).isNotEmpty()

}
