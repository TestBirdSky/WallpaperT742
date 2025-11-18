package com.papaya.fig

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.applovin.sdk.AppLovinMediationProvider
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkInitializationConfiguration
import com.thinkup.core.api.TUBiddingListener
import com.thinkup.core.api.TUBiddingResult
import com.thinkup.interstitial.unitgroup.api.CustomInterstitialAdapter
import java.util.UUID

abstract class BaseMaxAdapter : CustomInterstitialAdapter() {

    private var placementId: String = ""
    private var interstitialAd: MaxInterstitialAd? = null
    private var maxAd: MaxAd? = null
    override fun loadCustomNetworkAd(
        p0: Context?, p1: MutableMap<String, Any>?, p2: MutableMap<String, Any>?
    ) {}


    override fun startBiddingRequest(
        context: Context?,
        serverExtra: MutableMap<String, Any>?,
        localExtra: MutableMap<String, Any>?,
        biddingListener: TUBiddingListener?,
    ): Boolean {
        startBid(serverExtra, biddingListener)
        return true
    }

    private fun startBid(
        serverExtra: MutableMap<String, Any>?, biddingListener: TUBiddingListener?
    ) {
        if (serverExtra == null || serverExtra["unit_id"].toString().isEmpty()) {
            biddingListener?.onC2SBiddingResultWithCache(TUBiddingResult.fail("unit_id null"), null)
            mLoadListener?.onAdLoadError("", "unit_id null")
            return
        }
        placementId = serverExtra["unit_id"].toString()
        interstitialAd = MaxInterstitialAd(placementId)
        interstitialAd?.setListener(object : MaxAdListener {
            override fun onAdLoaded(p0: MaxAd) {
                maxAd = p0
                val ec = p0.revenue * 1000
                biddingListener?.onC2SBiddingResultWithCache(
                    TUBiddingResult.success(ec, UUID.randomUUID().toString(), null), null
                )
                mLoadListener?.onAdCacheLoaded()
            }

            override fun onAdDisplayed(p0: MaxAd) {
                mImpressListener?.onInterstitialAdShow()
            }

            override fun onAdHidden(p0: MaxAd) {
                mImpressListener?.onInterstitialAdClose()
            }

            override fun onAdClicked(p0: MaxAd) {
                mImpressListener?.onInterstitialAdClicked()
            }

            override fun onAdLoadFailed(p0: String, p1: MaxError) {
                biddingListener?.onC2SBiddingResultWithCache(TUBiddingResult.fail(p1.message), null)
                mLoadListener?.onAdLoadError("${p1.code}", p1.message)
            }

            override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
                mImpressListener?.onInterstitialAdVideoError(p1.code.toString(), p1.message)
            }

        })
        interstitialAd?.loadAd()
    }

    override fun destory() {
        interstitialAd?.setListener(null);
        interstitialAd?.destroy();
        interstitialAd = null
        maxAd = null
    }

    override fun isAdReady(): Boolean {
        return interstitialAd?.isReady ?: false
    }

    override fun getNetworkPlacementId(): String {
        return placementId
    }

    override fun getNetworkSDKVersion(): String {
        runCatching {
            return AppLovinSdk.VERSION
        }
        return ""
    }

    override fun getNetworkName(): String {
        return "mx_${maxAd?.networkName}"
    }

    override fun show(p0: Activity?) {
        if (p0 != null) {
            interstitialAd?.showAd(
                p0.window.decorView as ViewGroup, (p0 as AppCompatActivity).lifecycle, p0
            )
        } else mImpressListener?.onInterstitialAdClose()
    }

}