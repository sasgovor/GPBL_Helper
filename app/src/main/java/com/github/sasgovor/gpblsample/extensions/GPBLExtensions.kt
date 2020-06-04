package com.github.sasgovor.gpblsample.extensions

import android.app.Activity
import com.android.billingclient.api.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun BillingClient.connectToGooglePlay(): BillingResult =
    suspendCoroutine { continuation ->
        this@connectToGooglePlay.startConnection(
            object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
//                    queryPurchases(BillingClient.SkuType.INAPP)
//                    queryPurchases(BillingClient.SkuType.SUBS)
//                    handle bought purchases
                        continuation.resume(billingResult)
                    }
                }

                override fun onBillingServiceDisconnected() {
                    this@connectToGooglePlay.startConnection(this)
                }
            }
        )
    }

suspend fun BillingClient.querySkuDetailsAsync(
    skuList: ArrayList<String>,
    skuType: String
): Map<String, SkuDetails> =
    suspendCoroutine { continuation ->
        val skuDetailsMap: HashMap<String, SkuDetails> = HashMap()
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(skuType)
        this.querySkuDetailsAsync(params.build(), object : SkuDetailsResponseListener {
            override fun onSkuDetailsResponse(
                billingResult: BillingResult?,
                skusDetails: MutableList<SkuDetails>?
            ) {
                try {
                    if (billingResult?.responseCode == BillingClient.BillingResponseCode.OK && skusDetails != null) {
                        for (skuDetails in skusDetails) {
//                            getting data about purchases
//                            val sku = skuDetails.sku
//                            val title = skuDetails.title
//                            val description = skuDetails.description
//                            val price = skuDetails.price
                            skuDetailsMap[skuDetails.sku] = skuDetails
                        }
                    }
                    continuation.resume(skuDetailsMap)
                } catch (t: Throwable) {
                    continuation.resumeWithException(t)
                }
            }
        })
    }

fun BillingClient.makePurchase(activity: Activity, skuDetails: SkuDetails) {
    val flowParams: BillingFlowParams = BillingFlowParams
        .newBuilder()
        .setSkuDetails(skuDetails)
        .build()

    this.launchBillingFlow(activity, flowParams)
}