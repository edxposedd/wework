package com.magic.kernel.core

interface IHookerProvider {

    fun provideStaticHookers(): List<Hooker>? = null

    fun provideEventHooker(event: String): Hooker? = null

}