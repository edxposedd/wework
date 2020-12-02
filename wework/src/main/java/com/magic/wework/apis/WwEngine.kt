package com.magic.wework.apis

import com.magic.kernel.core.HookerCenter
import com.magic.wework.hookers.*

object WwEngine {

    var hookerCenters: List<HookerCenter> = listOf(
        ApplicationHookers,
//        ContactHookers,
        ConversationHookers
//        CustomerHookers,
//        NotificationHookers
//        DepartmentHookers
//        UserLabelHookers
    )

}