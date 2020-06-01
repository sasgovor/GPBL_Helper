package com.github.sasgovor.gpblsample.helpers

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GPBLHelper(context: Context, listener: PurchasesUpdatedListener) {

    val billingClient = BillingClient.newBuilder(context).setListener(listener).build()

    fun startConnectionToGP() {
        billingClient.startConnection(
            object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
//                       queryPurchases(BillingClient.SkuType.INAPP)
//                       queryPurchases(BillingClient.SkuType.SUBS)
//                       handle bought purchases
                    }
                }

                override fun onBillingServiceDisconnected() {
                    billingClient.startConnection(this)
                }
            })
    }

    suspend fun querySkuDetailsAsync(
        skuList: ArrayList<String>,
        skuType: String
    ): Map<String, SkuDetails> {
        val skuDetailsMap: HashMap<String, SkuDetails> = HashMap()
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(skuType)

        withContext(Dispatchers.IO) {
            billingClient.querySkuDetailsAsync(params.build(), object : SkuDetailsResponseListener {
                override fun onSkuDetailsResponse(
                    billingResult: BillingResult?,
                    skusDetails: MutableList<SkuDetails>?
                ) {
                    if (billingResult?.responseCode == BillingClient.BillingResponseCode.OK && skusDetails != null) {
                        for (skuDetails in skusDetails) {
//                          getting data about purchases
//                            val sku = skuDetails.sku
//                            val title = skuDetails.title
//                            val description = skuDetails.description
//                            val price = skuDetails.price
                            skuDetailsMap[skuDetails.sku] = skuDetails
                        }
                    }
                }
            })
        }

        return skuDetailsMap
    }

    fun queryPurchases(skuType: String): List<Purchase>? {
        val purchasesResult = billingClient.queryPurchases(skuType)

        if (purchasesResult.responseCode == BillingClient.BillingResponseCode.OK) {
            return purchasesResult.purchasesList
        }

        return null
    }

    fun buySomething(activity: Activity, skuDetails: SkuDetails) {
        val flowParams: BillingFlowParams = BillingFlowParams
            .newBuilder()
            .setSkuDetails(skuDetails)
            .build()

        billingClient.launchBillingFlow(activity, flowParams)
    }

    fun initPurchases(purchases: List<Purchase>) {
//        add bought functionality to user
    }
}