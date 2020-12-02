package com.magic.shared.hookers.interfaces

import android.app.Activity
import android.content.Intent
import android.os.Bundle

interface IActivityHooker {

    /**
     * onCreate
     */
    fun onActivityCreating(activity: Activity, savedInstanceState: Bundle?) {}
    fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    /**
     * onStart
     */
    fun onActivityStarting(activity: Activity) {}
    fun onActivityStarted(activity: Activity) {}

    /**
     * onResume
     */
    fun onActivityResuming(activity: Activity) {}
    fun onActivityResumed(activity: Activity) {}

    fun onActivityResulting(activity: Activity, requestCode: Int, resultCode: Int, data: Intent) {}
    fun onActivityResulted(activity: Activity, requestCode: Int, resultCode: Int, data: Intent) {}
}