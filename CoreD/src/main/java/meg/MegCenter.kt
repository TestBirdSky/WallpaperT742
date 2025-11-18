package meg

import android.app.Activity
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ap.A
import com.orchids.oplz.Cop
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * Date：2025/7/16
 * Describe:
 */

// 单聚合
class MegCenter {
    private val mPAH = ToponImpl("")// 高价值
    private val mPangleAdImpl = ToponImpl("1") // 低价值
    private var idH = ""
    private var idL = ""

    fun setAdId(high: String, lowId: String) {
        idH = high
        idL = lowId
    }

    fun loadAd() {
        mPAH.lAd(idH)
        mPangleAdImpl.lAd(idL)
    }

    private var job: Job? = null
    fun showAd(ac: Activity) {
        GoStartMe.sNumJump(0)
        if (ac is AppCompatActivity) {
            ac.onBackPressedDispatcher.addCallback {}
            job?.cancel()
            job = ac.lifecycleScope.launch {
                Cop.pE("ad_done")
                delay(Random.nextLong(GoStartMe.gDTime()))
                if (GoStartMe.isLoadH) {
                    A.c(ac)
                }
                var isS = mPAH.shAd(ac)
                if (isS.not()) {
                    isS = mPangleAdImpl.shAd(ac)
                }
                if (isS.not()) {
                    delay(500)
                    ac.finishAndRemoveTask()
                }
            }
        }
    }
}