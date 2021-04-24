package com.magic.xmagichooker

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.magic.shared.hookers.interfaces.IActivityHooker
import com.magic.wework.hookers.interfaces.IApplicationHooker
import com.magic.wework.hookers.interfaces.IConversationHooker
import com.magic.wework.apis.com.tencent.wework.foundation.model.Conversation
import com.magic.wework.apis.com.tencent.wework.foundation.model.Message

object Plugins: IActivityHooker, IApplicationHooker, IConversationHooker {

    /*  ------------------  IActivityHooker  ----------------- */
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.e(Plugins::class.java.name, "onActivityCreated   class: ${activity.javaClass}")
    }

    /*  ------------------  IConversationHooker  ----------------- */
    override fun onReconvergeConversation() {
        Log.e(Plugins::class.java.name, "onReconvergeConversation")
    }

    override fun onReloadConvsProperty() {
        Log.e(Plugins::class.java.name, "onReloadConvsProperty")
    }

    override fun onSyncStateChanged(i: Int, i2: Int) {
        Log.e(Plugins::class.java.name, "onSyncStateChanged  i: $i   i2: $i2")
    }

    override fun onAddConversations(conversationArr: Array<Any>) {
        for (conv in conversationArr) {
            Log.e(Plugins.javaClass.name, "onAddConversations  ${Conversation.getInfo(conv)}")
        }
    }

    override fun onExitConversation(conversation: Any) {
        Log.e(Plugins.javaClass.name, "onExitConversation  ${Conversation.getInfo(conversation)}")
//        val conv = Conversation(conversation)
//        Log.e(Plugins::class.java.name, "onExitConversation   remoteId: ${conv.getInfo().remoteId}  -  name: ${conv.getInfo().name}  -  type: ${conv.getInfo().type}")
    }

    override fun onSetReadReceipt(conversation: Any) {
        Log.e(Plugins.javaClass.name, "onSetReadReceipt  ${Conversation.getInfo(conversation)}")
//        val conv = Conversation(conversation)
//        Log.e(Plugins::class.java.name, "onSetReadReceipt   remoteId: ${conv.getInfo().remoteId}  -  name: ${conv.getInfo().name}  -  type: ${conv.getInfo().type}")
    }

    override fun onAddMembers(conversation: Any) {
        Log.e(Plugins.javaClass.name, "onAddMembers  ${Conversation.getInfo(conversation)}")
//        val conv = Conversation(conversation)
//        for (member in conv.getMembers()) {
//            Log.e(Plugins::class.java.name, "onAddMembers  remoteId: ${member.operatorRemoteId}  name: ${member.name}   nickname: ${member.nickName}")
//        }
    }

    override fun onAddMessages(conversation: Any, messageArr: Array<Any>, z: Boolean) {
        for (msg in messageArr) {
            Log.e(Plugins.javaClass.name, "onAddMessages  ${Conversation.getInfo(conversation)}     ${Message.getInfo(msg)}")
        }
//        val conv = Conversation(conversation)
//        for (message0 in messageArr) {
//                        Log.e(Plugins.javaClass.name, "emotion消息类型: $contentType   地址：$downloadInfo")
//                        if (downloadInfo != null) {
//                            FileDownloadApiImpl.newInstance().downloadFile(downloadInfo) { i, str ->
//                                Log.e(Plugins.javaClass.name, "下载文件：${if (i == 0) "成功" else "失败"}")
//                            }
//                        }
//                    }
//                                Log.e(Plugins.javaClass.name, "下载文件：${if (i == 0) "成功" else "失败"}")
//                            }
//                        }
//                    }
//                                    val timeInterval = System.currentTimeMillis() - (Message(message0).getInfo().sendTime.toLong() * 1000)
//                                    Log.e(Plugins::class.java.name, "onAddMessages 文本消息  ${textMessage.codeLanguage}  消息内容: ${String(textMessage.content)}   时间: $timeInterval")
//                                    val text = String(textMessage.content)
//                                    val textSplits = text.split(":")
//                                    if (timeInterval > 10000) return
//                                    if (text.startsWith("调试", true)) {
//                                    } else if (text.startsWith("发送文本消息")) {
//                                        if (textSplits.size > 1) {
//                                            val splits = textSplits[1].split(",")
//                                            if (splits.size > 1) {
//                                            }
//                                        } else {
//                                        }
//                                    } else if (text.startsWith("发送图片消息") || text.startsWith("发送语音消息") || text.startsWith("发送视频消息") || text.startsWith("发送文件消息")) {
//                                        var type = IHttpConfigs.Type.DEFAULT
//                                        if (textSplits.first().equals("发送图片消息", true)) {
//                                            type = IHttpConfigs.Type.IMAGE
//                                        } else if (textSplits.first().equals("发送语音消息", true)) {
//                                            type = IHttpConfigs.Type.VOICE
//                                        } else if (textSplits.first().equals("发送视频消息", true)) {
//                                            type = IHttpConfigs.Type.VIDEO
//                                        }
//                                        if (textSplits.size > 1) {
//                                            val splits = text.removeRange(0, 7).split(",")
//                                            var id = conv.getLocalId()
//                                            var urlString = ""
//                                            if (splits.size > 1) {
//                                                id = splits.first().toLong()
//                                                urlString = if (splits.last().toString().startsWith("http")) splits.last().toString() else ""
//                                            } else {
//                                                urlString = splits.last().toString()
//                                            }
//                                            } else {
//                                            }
//                                        } else {
//                                        }
//                                    } else if (text.startsWith("发送定位消息")) {
//                                        var id = conv.getLocalId()
//                                        if (textSplits.size > 1) {
//                                            id = textSplits.last().toString().trim().toLong()
//                                        }
//                                    } else if (text.startsWith("发送链接消息")) {
//                                        var id = conv.getLocalId()
//                                        if (textSplits.size > 1) {
//                                            id = textSplits.last().toString().trim().toLong()
//                                    } else if (text.startsWith("发送小程序消息")) {
//                                        var id = conv.getLocalId()
//                                        if (textSplits.size > 1) {
//                                            id = textSplits.last().toString().trim().toLong()
//                                        }
//                                    } else if (text.startsWith("获取所有群聊")) {
//                                        val conversationInfos = conversations.map {
//                                            val conv = Conversation(it)
//                                            val convName = if (conv.getInfo().name.isEmpty()) conv.getDefaultName(true) else conv.getInfo().name
//                                            return@map "群rId: ${conv.getRemoteId()}  type: ${conv.getType()}  群名称: $convName \n"
//                                        }
//                                    } else if (text.startsWith("获取保存到通讯录的群聊")) {
//                                        val conversationInfos = conversations.map {
//                                            val conv = Conversation(it)
//                                            return@map "群rId: ${conv.getRemoteId()}  群名称: ${conv.getDefaultName(true)}"
//                                        }
//                                    } else if (text.startsWith("获取免打扰及置顶会话")) {
//                                    } else if (text.startsWith("获取群二维码")) {
//                                    } else if (text.startsWith("保存到通讯录")) {
//                                        var convId = conv.getLocalId()
//                                        if (textSplits.size > 1) {
//                                            convId = textSplits.last().trim().toLong()
//                                        }
//                                    } else if (text.startsWith("创建新群聊")) {
//                                        if (textSplits.size > 1) {
//                                            val userIds = textSplits[1].split(",").map { it.trim().toLong() }.toLongArray()
//                                            if (userIds.size > 1) {
//                                            } else {
//                                            }
//                                        } else {
//                                        }
//                                    } else if (text.startsWith("解散群聊")) {
//                                        var convId = conv.getLocalId()
//                                        if (textSplits.size > 1) {
//                                            convId = textSplits.last().trim().toLong()
//                                        }
//                                    } else if (text.startsWith("更新会话信息")) {
//                                        var convId = conv.getLocalId()
//                                        if (textSplits.size > 1) {
//                                            convId = textSplits.last().trim().toLong()
//                                        }
//                                    } else if (text.startsWith("邀请他人入群")) {
//                                        if (textSplits.size > 1) {
//                                            val splits = textSplits[1].split(",")
//                                            if (splits.size > 1) {
//                                                val convId = splits.first().trim().toLong()
//                                                val userId = splits.last().trim().toLong()
//                                            } else {
//                                            }
//                                        } else {
//                                        }
//                                    } else if (text.startsWith("撤回邀请")) {
//                                        if (textSplits.size > 1) {
//                                            val splits = textSplits[1].split(",")
//                                            if (splits.size > 1) {
//                                                val convId = splits.first().trim().toLong()
//                                                val userId = splits.last().trim().toLong()
//                                            } else {
//                                            }
//                                        } else {
//                                        }
//                                    } else if (text.startsWith("添加他人入群")) {
//                                        if (textSplits.size > 1) {
//                                            val splits = textSplits[1].split(",")
//                                            if (splits.size > 1) {
//                                                val convId = splits.first().trim().toLong()
//                                                val userId = splits.last().trim().toLong()
//                                            } else {
//                                            }
//                                        } else {
//                                        }
//                                    } else if (text.startsWith("移除群聊")) {
//                                        if (textSplits.size > 1) {
//                                            val splits = textSplits[1].split(",")
//                                            if (splits.size > 1) {
//                                                val convId = splits.first().trim().toLong()
//                                                val userId = splits.last().trim().toLong()
//                                            } else {
//                                            }
//                                        } else {
//                                        }
//                                    } else if (text.startsWith("获取成员信息")) {
//                                        var convId = conv.getLocalId()
//                                        if (textSplits.size > 1) {
//                                            convId = textSplits.last().trim().toLong()
//                                        }
//                                    } else if (text.startsWith("清除未读消息")) {
//                                        var convId = conv.getLocalId()
//                                        if (textSplits.size > 1) {
//                                            convId = textSplits.last().trim().toLong()
//                                        }
//                                    } else if (text.startsWith("退出群聊")) {
//                                        val conversationId = if (textSplits.size > 1) textSplits[1].trim().toLong() else conv.getInfo().id
//                                    } else if (text.startsWith("修改群名称")) {
//                                        val name = if (textSplits.size > 1) textSplits[1].trim() else "测试修改群名称"
//                                    } else if (text.startsWith("修改群内昵称")) {
//                                        val nickname = if (textSplits.size > 1) textSplits[1].trim() else "测试修改群内昵称"
//                                    } else if (text.startsWith("撤回该条消息")) {
//                                    } else if (text.startsWith("设置群公告")) {
//                                        var convId = conv.getLocalId()
//                                        var notification = "测试群公告"
//                                        if (textSplits.size > 1) {
//                                            val splits = textSplits[1].split(",")
//                                            if (splits.size > 1) {
//                                                convId = splits.first().trim().toLong()
//                                                notification = splits.last().toString()
//                                            } else {
//                                                notification = splits.last().toString()
//                                            }
//                                        }
//                                    } else if (text.startsWith("置顶")) {
//                                    } else if (text.startsWith("取消置顶")) {
//                                    } else if (text.startsWith("免打扰")) {
//                                    } else if (text.startsWith("取消免打扰")) {
//                                    } else if (text.startsWith("获取缓存的联系人")) {
//                                    } else if (text.startsWith("获取我的二维码")) {
//                                    } else if (text.startsWith("获取二维码")) {
//                                        var type = ContactService.GETCONTACT_BY_QR_CODE
//                                        if (textSplits.size > 1) {
//                                            type = textSplits.last().trim().toInt()
//                                        }
//                                    } else if (text.startsWith("获取公司信息")) {
//                                    } else if (text.startsWith("修改客户备注")) {
//                                        if (textSplits.size > 1) {
//                                            val userId = textSplits.last().trim().toLong()
//                                        } else {
//                                        }
//                                    } else if (text.startsWith("修改同事备注")) {
//                                        if (textSplits.size > 1) {
//                                            val splits = textSplits[1].split(",")
//                                            var realRemark = "备注"
//                                            var remarks = "描述"
//                                            val userId = splits.first().trim().toLong()
//                                            if (splits.size > 2) {
//                                                realRemark = splits[1].toString()
//                                                remarks = splits.last().toString()
//                                            } else if (splits.size > 1) {
//                                                realRemark = splits.last().toString()
//                                            }
//                                        }
//                                    } else if (text.startsWith("搜索联系人")) {
//                                        if (textSplits.size > 1) {
//                                            val keyword = textSplits.last().trim()
//                                        }
//                                    } else if (text.startsWith("搜索本地联系人")) {
//                                        if (textSplits.size > 1) {
//                                            val keyword = textSplits.last().trim()
//                                    } else if (text.startsWith("标记联系人")) {
//                                    } else if (text.startsWith("获取被标记的联系人")) {
//                                    } else if (text.startsWith("获取一级部门信息")) {
//                                    } else if (text.startsWith("获取部门用户信息")) {
//                                        var departmentId = 1688852946270840
//                                        if (textSplits.size > 1) {
//                                            departmentId = textSplits.last().trim().toLong()
//                                        }
//                                    } else if (text.startsWith("获取二级部门信息")) {
//                                    } else if (text.startsWith("获取指定部门")) {
//                                    } else if (text.startsWith("获取部门架构")) {
//                                        var departmentId = 1688852946270840
//                                        if (textSplits.size > 1) {
//                                            departmentId = textSplits.last().trim().toLong()
//                                        }
//                                    } else if (text.startsWith("修改职务")) {
//                                        var jobName = "测试"
//                                        if (textSplits.size > 1) {
//                                            jobName = textSplits.last().toString()
//                                        }
//                                    } else if (text.startsWith("修改对外职务")) {
//                                        var jobName = "测试"
//                                        if (textSplits.size > 1) {
//                                            jobName = textSplits.last().toString()
//                                        }
//                                    } else if (text.startsWith("修改头像")) {
//                                        var avatarUrl = "http://b.hiphotos.baidu.com/image/pic/item/0eb30f2442a7d9337119f7dba74bd11372f001e0.jpg"
//                                        if (textSplits.size > 1) {
//                                            avatarUrl = textSplits.last().toString().trim()
//                                        }
//                                        HttpClients.download(avatarUrl, IHttpConfigs.Type.IMAGE, iDownloadCallback = { localPath, _ ->
//                                            if (localPath != null) {
//                                        })
//                                    } else if (text.startsWith("获取绑定微信状态")) {
//                                    } else if (text.startsWith("删除联系人")) {
//                                        when (textSplits.size > 1) {
//                                            true -> {
//                                                val contactIds = textSplits[1].split(",").map { it.trim().toLong() }.toLongArray()
//                                                            }
//                                                        }
//                                                    } else {
//                                                    }
//                                                }
//                                            }
//                                            false -> {
//                                            }
//                                        }
//                                    } else if (text.startsWith("获取")) {
//                                        var contactType = 0
//                                        when (String(textMessage.content)) {
//                                            "获取我的微信联系人" -> contactType
//                                            "获取我的手机联系人" -> contactType
//                                            "获取推荐的好友" -> contactType
//                                            "获取我的同事" -> contactType
//                                            "获取我的客户" -> contactType
//                                            "获取待添加的客户" -> contactType
//                                            "获取内部联系客户" -> contactType =
//                                            "获取联系群组"  -> contactType =
//                                            "获取历史好友"  -> contactType =
//                                            "获取加星联系人"  -> contactType =
//                                            "获取其他组织"  -> contactType =
//                                            "获取保存的群组"  -> contactType = .CONTACT_TYPE_GROUP_MEM
//                                            "获取我的好友" -> contactType = .CONTACT_TYPE_RCT_FRIEND
//                                        }
//                                    } else if (text.startsWith("查看指令集")) {
//                                    }
//                                }
//                            }
//                        }
//                    }

    }

    override fun onChangeOwner(conversation: Any) {
        Log.e(Plugins.javaClass.name, "onChangeOwner  ${Conversation.getInfo(conversation)}")
//        val conv = Conversation(conversation)
//        Log.e(Plugins::class.java.name, "onChangeOwner   remoteId: ${conv.getInfo().remoteId}  -  name: ${conv.getInfo().name}  -  type: ${conv.getInfo().type}")
    }

    override fun onDraftDidChange(conversation: Any) {
        Log.e(Plugins.javaClass.name, "onDraftDidChange  ${Conversation.getInfo(conversation)}")
//        val conv = Conversation(conversation)
//        Log.e(Plugins::class.java.name, "onDraftDidChange   remoteId: ${conv.getInfo().remoteId}  -  name: ${conv.getInfo().name}  -  type: ${conv.getInfo().type}")
    }

    override fun onMessageStateChange(conversation: Any, message: Any, i: Int) {
        Log.e(Plugins.javaClass.name, "onMessageStateChange  ${Conversation.getInfo(conversation)}")
//        val conv = Conversation(conversation)
//        Log.e(Plugins::class.java.name, "onMessageStateChange: ${conv.getInfo().remoteId}  -  ${conv.getInfo().name}  -  ${conv.getInfo().type}")
//        val msg = Message(message)
//        Log.e(Plugins::class.java.name, "onMessageStateChange： ${msg.getInfo().contentType}  -   ${String(msg.getInfo().content)}   状态： ${i}")
    }

    override fun onMessageUpdate(conversation: Any, message: Any) {
        Log.e(Plugins.javaClass.name, "onMessageUpdate  ${Conversation.getInfo(conversation)}")
//        val conv = Conversation(conversation)
//        Log.e(Plugins::class.java.name, "onMessageUpdate   remoteId: ${conv.getInfo().remoteId}  -  name: ${conv.getInfo().name}  -  type: ${conv.getInfo().type}")
//        val msg = Message(message)
//        Log.e(Plugins::class.java.name, "onMessageUpdate  类型： ${msg.getInfo().contentType}  -  内容：  ${String(msg.getInfo().content)}")
    }

    override fun onModifyName(conversation: Any) {
        Log.e(Plugins.javaClass.name, "onModifyName  ${Conversation.getInfo(conversation)}")
//        val conv = Conversation(conversation)
//        Log.e(Plugins::class.java.name, "onMessageUpdate   remoteId: ${conv.getInfo().remoteId}  -  name: ${conv.getInfo().name}  -  type: ${conv.getInfo().type}")
    }

    override fun onPropertyChanged(conversation: Any) {
        Log.e(Plugins.javaClass.name, "onPropertyChanged  ${Conversation.getInfo(conversation)}")
//        val conv = Conversation(conversation)
//        Log.e(Plugins::class.java.name, "onPropertyChanged   remoteId: ${conv.getInfo().remoteId}  -  name: ${conv.getInfo().name}  -  type: ${conv.getInfo().type}")
    }

    override fun onRemoveAllMessages(conversation: Any) {
        Log.e(Plugins.javaClass.name, "onRemoveAllMessages  ${Conversation.getInfo(conversation)}")
//        val conv = Conversation(conversation)
//        Log.e(Plugins::class.java.name, "onRemoveAllMessages   remoteId: ${conv.getInfo().remoteId}  -  name: ${conv.getInfo().name}  -  type: ${conv.getInfo().type}")
    }

    override fun onRemoveMembers(conversation: Any) {
        Log.e(Plugins.javaClass.name, "onRemoveMembers  ${Conversation.getInfo(conversation)}")
//        val conv = Conversation(conversation)
//        Log.e(Plugins::class.java.name, "onRemoveMembers   remoteId: ${conv.getInfo().remoteId}  -  name: ${conv.getInfo().name}  -  type: ${conv.getInfo().type}")
//        for (member in conv.getMembers()) {
//            Log.e(Plugins::class.java.name, "onRemoveMembers  remoteId: ${member.operatorRemoteId}  name: ${member.name}   nickname: ${member.nickName}")
//        }
    }

    override fun onRemoveMessages(conversation: Any, message: Any) {
        Log.e(Plugins.javaClass.name, "onRemoveMessages  ${Conversation.getInfo(conversation)}")
//        val conv = Conversation(conversation)
//        Log.e(Plugins::class.java.name, "onRemoveMessages: ${conv.getInfo().remoteId}  -  ${conv.getInfo().name}  -  ${conv.getInfo().type}")
//        val msg = Message(message)
//        Log.e(Plugins::class.java.name, "onRemoveMessages： ${msg.getInfo().contentType}  -   ${String(msg.getInfo().content)}")
    }

    override fun onSetAllBan(conversation: Any) {
        Log.e(Plugins.javaClass.name, "onSetAllBan  ${Conversation.getInfo(conversation)}")
//        val conv = Conversation(conversation)
//        Log.e(Plugins::class.java.name, "onSetAllBan: ${conv.getInfo().remoteId}  -  ${conv.getInfo().name}  -  ${conv.getInfo().type}")
    }

    override fun onSetCollect(conversation: Any) {
        Log.e(Plugins.javaClass.name, "onSetCollect  ${Conversation.getInfo(conversation)}")
//        val conv = Conversation(conversation)
//        Log.e(Plugins::class.java.name, "onSetCollect: ${conv.getInfo().remoteId}  -  ${conv.getInfo().name}  -  ${conv.getInfo().type}")
    }

    override fun onSetConfirmAddMember(conversation: Any) {
        Log.e(Plugins.javaClass.name, "onSetConfirmAddMember  ${Conversation.getInfo(conversation)}")
//        val conv = Conversation(conversation)
//        Log.e(Plugins::class.java.name, "onSetConfirmAddMember: ${conv.getInfo().remoteId}  -  ${conv.getInfo().name}  -  ${conv.getInfo().type}")
    }

    override fun onSetMembersBan(conversation: Any) {
        Log.e(Plugins.javaClass.name, "onSetMembersBan  ${Conversation.getInfo(conversation)}")
//        val conv = Conversation(conversation)
//        Log.e(Plugins::class.java.name, "onSetMembersBan: ${conv.getInfo().remoteId}  -  ${conv.getInfo().name}  -  ${conv.getInfo().type}")
    }

    override fun onSetOwnerManager(conversation: Any) {
        Log.e(Plugins.javaClass.name, "onSetOwnerManager  ${Conversation.getInfo(conversation)}")
//        val conv = Conversation(conversation)
//        Log.e(Plugins::class.java.name, "onSetOwnerManager: ${conv.getInfo().remoteId}  -  ${conv.getInfo().name}  -  ${conv.getInfo().type}")
    }

    override fun onSetShield(conversation: Any) {
        Log.e(Plugins.javaClass.name, "onSetShield  ${Conversation.getInfo(conversation)}")
//        val conv = Conversation(conversation)
//        Log.e(Plugins::class.java.name, "onSetShield: ${conv.getInfo().remoteId}  -  ${conv.getInfo().name}  -  ${conv.getInfo().type}")
    }

    override fun onSetTop(conversation: Any) {
        Log.e(Plugins.javaClass.name, "onSetTop  ${Conversation.getInfo(conversation)}")
//        val conv = Conversation(conversation)
//        Log.e(Plugins::class.java.name, "onSetTop: ${conv.getInfo().remoteId}  -  ${conv.getInfo().name}  -  ${conv.getInfo().type}")
    }

    override fun onTypingStateUpdate(conversation: Any) {
        Log.e(Plugins.javaClass.name, "onTypingStateUpdate  ${Conversation.getInfo(conversation)}")
//        val conv = Conversation(conversation)
//        Log.e(Plugins::class.java.name, "onTypingStateUpdate: ${conv.getInfo().remoteId}  -  ${conv.getInfo().name}  -  ${conv.getInfo().type}")
    }

    override fun onUnReadCountChanged(conversation: Any, i: Int, i2: Int) {
        Log.e(Plugins.javaClass.name, "onUnReadCountChanged  ${Conversation.getInfo(conversation)}   $i   $i2")
//        val conv = Conversation(conversation)
//        Log.e(Plugins::class.java.name, "onUnReadCountChanged: ${conv.getInfo().remoteId}  -  ${conv.getInfo().name}  -  ${conv.getInfo().type}")
    }

    /*  ------------------  IContactHooker  ----------------- */
//
//    override fun onApplyUnReadCountChanged(i: Int) {
//        Log.e(Plugins::class.java.name, "onApplyUnReadCountChanged: i: $i")
//    }
//
//    override fun onContactListUnradCountChanged(i: Int, i2: Int, i3: Int) {
//        Log.e(Plugins::class.java.name, "onContactListUnradCountChanged: i: $i  i2: $i2  i3: $i3")
//    }
//
//    override fun onSyncContactFinish(i: Int, z: Boolean) {
//        Log.e(Plugins::class.java.name, "onSyncContactFinish: i: $i  z: $z")
//    }
}
