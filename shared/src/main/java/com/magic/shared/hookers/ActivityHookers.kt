package com.magic.shared.hookers

import com.magic.kernel.core.Clazz
import com.magic.kernel.core.HookerCenter
import com.magic.shared.hookers.interfaces.IActivityHooker

object ActivityHookers : HookerCenter() {

    override val interfaces: List<Class<*>>
        get() = listOf(IActivityHooker::class.java)

    override fun provideEventHooker(event: String) = when (event) {
        "onActivityCreating",
        "onActivityCreated" ->
            iMethodNotifyHooker(
                clazz = Clazz.Activity,
                method = "onCreate",
                iClazz = IActivityHooker::class.java,
                iMethodBefore = "onActivityCreating",
                iMethodAfter = "onActivityCreated",
                needObject = true,
                parameterTypes = *arrayOf(Clazz.Bundle)
            )
        "onActivityStarting",
        "onActivityStarted" ->
            iMethodNotifyHooker(
                clazz = Clazz.Activity,
                method = "onStart",
                iClazz = IActivityHooker::class.java,
                iMethodBefore = "onActivityStarting",
                iMethodAfter = "onActivityStarted",
                needObject = true
            )
        "onActivityResuming",
        "onActivityResumed" ->
            iMethodNotifyHooker(
                clazz = Clazz.Activity,
                method = "onResume",
                iClazz = IActivityHooker::class.java,
                iMethodBefore = "onActivityResuming",
                iMethodAfter = "onActivityResumed",
                needObject = true
            )
        else -> throw IllegalArgumentException("Unknown event: $event")
    }

}