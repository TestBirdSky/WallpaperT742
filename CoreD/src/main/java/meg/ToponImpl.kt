package meg

import android.app.Activity
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustAdRevenue
import com.orchids.oplz.Cop
import com.thinkup.core.api.AdError
import com.thinkup.core.api.TUAdInfo
import com.thinkup.interstitial.api.TUInterstitial
import com.thinkup.interstitial.api.TUInterstitialListener
import org.json.JSONObject

/**
 * Date：2025/10/13
 * Describe:
 */

class ToponImpl(val tag: String) : TUInterstitialListener {

    private var isLoading = false
    private var lT = 0L
    private var mAd: TUInterstitial? = null


    // 加载广告
    fun lAd(id: String) {
        if (id.isBlank()) return
        if (isLoading && System.currentTimeMillis() - lT < 61000) return
        if (isReadyAd()) return
        isLoading = true
        lT = System.currentTimeMillis()
        Cop.pE("advertise_req$tag")
        mAd = TUInterstitial(Cop.mApp, id)
        mAd?.setAdListener(this)
        mAd?.load()
    }

    fun isReadyAd(): Boolean {
        return mAd?.isAdReady == true
    }

    private var call: (() -> Unit)? = null
    private var time = 0L

    // 显示广告
    fun shAd(a: Activity): Boolean {
        val ad = mAd
        if (ad?.isAdReady == true) {
            call = {
                a.finishAndRemoveTask()
                GoStartMe.isSAd = false
            }
            time = System.currentTimeMillis()
            Cop.pE("advertise_show")
            ad.show(a)
            mAd = null
            return true
        }
        return false
    }

    override fun onInterstitialAdLoaded() {
        isLoading = false
        Cop.pE("advertise_get$tag")
    }

    override fun onInterstitialAdLoadFail(p0: AdError?) {
        isLoading = false
        Cop.pE("advertise_fail$tag", "${p0?.code}")
    }

    override fun onInterstitialAdClicked(p0: TUAdInfo?) {}

    override fun onInterstitialAdShow(p0: TUAdInfo?) {
        Cop.pE("advertise_show_t", "${(System.currentTimeMillis() - time) / 1000}")
        p0?.let {
            postP(it)
        }
        GoStartMe.adShow()
    }

    override fun onInterstitialAdClose(p0: TUAdInfo?) {
        call?.invoke()
        call = null
    }

    override fun onInterstitialAdVideoStart(p0: TUAdInfo?) {}

    override fun onInterstitialAdVideoEnd(p0: TUAdInfo?) {}

    override fun onInterstitialAdVideoError(p0: AdError?) {
        Cop.pE("advertise_fail_api", "${p0?.code}_${p0?.desc}")
        call?.invoke()
        call = null
        GoStartMe.mAdC.loadAd()
    }

    private fun postP(ad: TUAdInfo) {
        Cop.postAd(JSONObject().apply {
            put("veronica", ad.publisherRevenue * 1000000)//ad_pre_ecpm
            put("invalid", ad.currency)//currency
            put("sony", ad.networkName)//ad_network
            put("went", "topon")//ad_source_client
            put("guilt", ad.placementId)//ad_code_id
            put("holly", ad.adsourceId)//ad_pos_id
            put("shovel", ad.format)//ad_format
        }.toString())

        val cpm = ad.publisherRevenue


        val adjustAdRevenue = AdjustAdRevenue("topon_sdk")

//        val adjustAdRevenue = AdjustAdRevenue("topon_sdk")
        adjustAdRevenue.setRevenue(ad.publisherRevenue, ad.currency)
        //可选配置
        adjustAdRevenue.adRevenueUnit = ad.adsourceId
        adjustAdRevenue.adRevenueNetwork = ad.networkName
        adjustAdRevenue.adRevenuePlacement = ad.placementId
        //发送收益数据
        Adjust.trackAdRevenue(adjustAdRevenue)

        GoStartMe.postEcpm(cpm)
    }

}