package com.github.sasgovor.gpblsample.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*
import com.github.sasgovor.gpblsample.R

class MainActivity : AppCompatActivity() {

    val purchasesUpdatedListener = object : PurchasesUpdatedListener {
        override fun onPurchasesUpdated(billingResult: BillingResult?, purchases: MutableList<Purchase>?) {
            if (billingResult?.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
//                    handlePurchase(purchase)
                }
            } else if (billingResult?.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                // handleCancellation()
            } else {
                // handle other codes
            }
        }
    }
    val skuDetailsResponseListener = object : SkuDetailsResponseListener {
        override fun onSkuDetailsResponse(billingResult: BillingResult?, skusDetails: MutableList<SkuDetails>?) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onResume() {
        super.onResume()
//        helper.queryPurchases()
    }
}