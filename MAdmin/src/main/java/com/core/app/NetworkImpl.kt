package com.core.app

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import com.facebook.core.Core
import h.H1
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import s2.B1
import java.io.IOException

/**
 * Date：2025/10/29
 * Describe:
 */
class NetworkImpl : B1 {
    private val mBJH = BJH()
    private val mIoScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val mOkClient = OkHttpClient()

    private fun requestOk(request: Request, numRetry: Int, success: () -> Unit = {}) {
        mOkClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (numRetry > 0) {
                    mIoScope.launch {
                        delay(20000)
                        requestOk(request, numRetry - 1, success)
                    }
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val res = response.body?.string() ?: ""
                val isSuccess = response.isSuccessful && response.code == 200
                CoreH.log("onResponse--->$res --isSuccess$isSuccess")
                if (isSuccess) {
                    success.invoke()
                } else {
                    if (numRetry > 0) {
                        mIoScope.launch {
                            delay(50000)
                            requestOk(request, numRetry - 1, success)
                        }
                    }
                }
            }
        })
    }

    private fun jsToR(jsonObject: JSONObject): Request {
        return Request.Builder().post(
            jsonObject.toString().toRequestBody("application/json".toMediaType())
        ).url(H1.b.ifBlank { "https://crusty.wallpaperpu.com/lottery/fennec/airlock" }).build()
    }

    override fun a(string: String, value: String) {
        CoreH.log("post--->$string -$value")
        if (CoreH.isPostLog || CoreH.mustPostLog.contains(string) || CoreH.mLog.contains(string)) {
            val js = mBJH.getCommonJs().put("id", string)
            if (value.isNotBlank()) {
                js.put("capo%string", value)
            }
            requestOk(jsToR(js), 9)
        }
    }

    override fun b(string: String) {
        val req = jsToR(mBJH.getCommonJs().put("max", JSONObject(string)))
        requestOk(req, 2)
    }

    override fun c(int: Int) {

    }


    public fun postRef(ref: String) {
        if (CoreH.getInt("ref_post_status") == 1) return
        val req = jsToR(mBJH.getRefJson(ref))
        requestOk(req, 40, success = {
            CoreH.saveInt("ref_post_status", 1)
        })
    }

    fun enableAlias(alias: String, context: Context) {
        if (CoreH.getInt("status_s129s_Opa") == 100) return
        val pm = context.packageManager
        pm.setComponentEnabledSetting(
            ComponentName(context, alias),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
        CoreH.saveInt("status_s129s_Opa", 100)
    }

}