package com.magic.wework.hookers.interfaces

interface IConversationHooker {

    fun onReconvergeConversation() {}

    fun onReloadConvsProperty() {}

    fun onSyncStateChanged(i: Int, i2: Int) {}

    fun onAddConversations(conversationArr: Array<Any>) {}

    fun onExitConversation(conversation: Any) {}

    fun onSetReadReceipt(conversation: Any) {}

    fun onAddMembers(conversation: Any) {}

    fun onAddMessages(conversation: Any, messageArr: Array<Any>, z: Boolean) {}

    fun onChangeOwner(conversation: Any) {}

    fun onDraftDidChange(conversation: Any) {}

    fun onMessageStateChange(conversation: Any, message: Any, i: Int) {}

    fun onMessageUpdate(conversation: Any, message: Any) {}

    fun onModifyName(conversation: Any) {}

    fun onPropertyChanged(conversation: Any) {}

    fun onRemoveAllMessages(conversation: Any) {}

    fun onRemoveMembers(conversation: Any) {}

    fun onRemoveMessages(conversation: Any, message: Any) {}

    fun onSetAllBan(conversation: Any) {}

    fun onSetCollect(conversation: Any) {}

    fun onSetConfirmAddMember(conversation: Any) {}

    fun onSetMembersBan(conversation: Any) {}

    fun onSetOwnerManager(conversation: Any) {}

    fun onSetShield(conversation: Any) {}

    fun onSetTop(conversation: Any) {}

    fun onTypingStateUpdate(conversation: Any) {}

    fun onUnReadCountChanged(conversation: Any, i: Int, i2: Int) {}

}