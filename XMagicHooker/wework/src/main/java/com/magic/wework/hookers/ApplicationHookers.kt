package com.magic.wework.hookers

import com.magic.kernel.core.HookerCenter
import com.magic.wework.hookers.interfaces.IApplicationHooker

object ApplicationHookers : HookerCenter() {

    override val interfaces: List<Class<*>>
        get() = listOf(IApplicationHooker::class.java)

}