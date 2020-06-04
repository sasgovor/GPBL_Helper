package com.github.sasgovor.gpblsample.helpers

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import com.github.sasgovor.gpblsample.extensions.connectToGooglePlay
import com.github.sasgovor.gpblsample.extensions.makePurchase
import com.github.sasgovor.gpblsample.extensions.querySkuDetailsAsync
import kotlinx.coroutines.flow.*

object GPBLHelper {

    private var billingClient: BillingClient? = null

//    var state:  Flow<List<Purchase>> = emptyFlow()
//    var state1:  Flow<Purchase>? = emptyFlow()

    fun getBillingClient(context: Context): BillingClient {
        if (billingClient == null) {
            billingClient = BillingClient.newBuilder(context)
                .setListener(object : PurchasesUpdatedListener {
                    override fun onPurchasesUpdated(
                        billingResult: BillingResult?, purchases: MutableList<Purchase>?
                    ) {
//                        state1 = purchases?.asFlow()
                    }
                }).build()
        }
        return billingClient as BillingClient
    }

    suspend fun startConnectionToGP(): BillingResult? =
        billingClient?.connectToGooglePlay()

    suspend fun querySkuDetailsAsync(
        skuList: ArrayList<String>,
        skuType: String
    ): Map<String, SkuDetails>? {
        try {
            return billingClient?.querySkuDetailsAsync(skuList, skuType)
        } catch (t: Throwable) {
            throw t
        }
    }


    fun queryPurchases(skuType: String): List<Purchase>? {
        val purchasesResult = billingClient?.queryPurchases(skuType)
        try {
            return purchasesResult?.purchasesList
        } catch (t: Throwable) {
            throw t
        }
    }

    fun buySomething(activity: Activity, skuDetails: SkuDetails) =
        billingClient?.makePurchase(activity, skuDetails)

    fun initPurchases(purchases: List<Purchase>) {
//        add bought functionality to user
    }
}