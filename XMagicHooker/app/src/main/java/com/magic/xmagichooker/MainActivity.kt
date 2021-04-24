package com.magic.xmagichooker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.magic.kernel.MagicHooker
import com.magic.kernel.media.audio.AudioHelper
import com.magic.kernel.okhttp.HttpClients
import com.magic.kernel.okhttp.IHttpConfigs
import com.magic.kernel.utils.CmdUtil
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CmdUtil.isRoot
    }

    override fun onResume() {
        super.onResume()
        if (checkHook()) {
            val path = MagicHooker.getApplicationApkPath("com.magic.xmagichooker")
            sample_text.text = "hooked = true  \n  \n $path"
        }
    }

    fun checkHook(): Boolean {
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
