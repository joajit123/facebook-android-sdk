/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.appevents.iap

import com.facebook.FacebookPowerMockTestCase
import com.facebook.FacebookSdk
import android.content.Context
import com.facebook.appevents.iap.InAppPurchaseUtils.getClass
import com.facebook.appevents.iap.InAppPurchaseUtils.getMethod
import com.facebook.appevents.iap.InAppPurchaseUtils.invokeMethod
import java.lang.reflect.Method
import java.util.concurrent.Executor
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.reflect.Whitebox


@PrepareForTest(
    FacebookSdk::class,
    InAppPurchaseUtils::class,
    InAppPurchaseSkuDetailsWrapper::class
)
class InAppPurchaseBillingClientWrapperV2V4Test : FacebookPowerMockTestCase() {
    private val mockExecutor: Executor = FacebookSerialExecutor()
    private lateinit var inAppPurchaseBillingClientWrapperV2V4: InAppPurchaseBillingClientWrapperV2V4
    private lateinit var inAppPurchaseSkuDetailsWrapper: InAppPurchaseSkuDetailsWrapper
    private val exampleClassName =
        "com.facebook.appevents.iap.InAppPurchaseBillingClientWrapperV2V4Test"
    private val METHOD_ON_BILLING_SETUP_FINISHED = "onBillingSetupFinished"
    private val exampleMethodName = "setup"
    private val exampleResponse = "response"
    private val exampleListener = "com.facebook.appevents.iap.PurchasesUpdatedListener"

    @Before
    override fun setup() {
        super.setup()
        PowerMockito.spy(InAppPurchaseBillingClientWrapperV2V4::class.java)
        PowerMockito.mockStatic(FacebookSdk::class.java)
        PowerMockito.mockStatic(InAppPurchaseSkuDetailsWrapper::class.java)
        PowerMockito.mockStatic(InAppPurchaseUtils::class.java)


        inAppPurchaseSkuDetailsWrapper = mock()
        inAppPurchaseBillingClientWrapperV2V4 = mock()
        Whitebox.setInternalState(
            InAppPurchaseBillingClientWrapperV2V4::class.java,
            "instance",
            inAppPurchaseBillingClientWrapperV2V4
        )
        Whitebox.setInternalState(
            InAppPurchaseSkuDetailsWrapper::class.java,
            "instance",
            inAppPurchaseSkuDetailsWrapper
        )
        val listenerClazz =
            Class.forName(exampleListener)

        whenever(FacebookSdk.isInitialized()).thenReturn(true)
        whenever(inAppPurchaseSkuDetailsWrapper.skuDetailsParamsClazz).thenReturn(listenerClazz)
        whenever(FacebookSdk.getExecutor()).thenReturn(mockExecutor)
        whenever(FacebookSdk.getApplicationContext()).thenReturn(mock())
        whenever(getClass(anyOrNull())).thenReturn(listenerClazz)
        whenever(
            getMethod(
                any(),
                any(),
                any()
            )
        ).thenReturn(
            Class.forName(exampleClassName)
                .getMethod(exampleMethodName)
        )
        whenever(
            invokeMethod(
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull()
            )
        ).thenReturn(exampleResponse)
        whenever(
            invokeMethod(
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
            )
        ).thenReturn(0)
    }

    @Test
    fun testHelperClassCanSuccessfullyCreateWrapper() {
        Whitebox.setInternalState(
            InAppPurchaseBillingClientWrapperV2V4::class.java,
            "instance",
            null as? InAppPurchaseBillingClientWrapperV2V4
        )
        val mockContext: Context = mock()
        whenever(mockContext.packageName).thenReturn("packageName")
        val inAppPurchaseBillingClientWrapper =
            InAppPurchaseBillingClientWrapperV2V4.getOrCreateInstance(mockContext)
        Assertions.assertThat(inAppPurchaseBillingClientWrapper).isNotNull()
    }

    @Test
    fun testCantGetClass() {
        whenever(getClass(anyOrNull())).thenReturn(null)
        Whitebox.setInternalState(
            InAppPurchaseBillingClientWrapperV2V4::class.java,
            "instance",
            null as? InAppPurchaseBillingClientWrapperV2V4
        )
        val mockContext: Context = mock()
        val inAppPurchaseBillingClientWrapper =
            InAppPurchaseBillingClientWrapperV2V4.getOrCreateInstance(mockContext)
        Assertions.assertThat(inAppPurchaseBillingClientWrapper).isNull()
    }

    @Test
    fun testCantGetMethod() {
        PowerMockito.mockStatic(InAppPurchaseUtils::class.java)
        whenever(
            getMethod(
                any(),
                any(),
                any()
            )
        ).thenReturn(
            null
        )
        Whitebox.setInternalState(
            InAppPurchaseBillingClientWrapperV2V4::class.java,
            "instance",
            null as? InAppPurchaseBillingClientWrapperV2V4
        )
        val mockContext: Context = mock()
        whenever(mockContext.packageName).thenReturn("packageName")
        val inAppPurchaseBillingClientWrapper =
            InAppPurchaseBillingClientWrapperV2V4.getOrCreateInstance(mockContext)
        Assertions.assertThat(inAppPurchaseBillingClientWrapper).isNull()
    }

    @Test
    fun testHelperClassCanSuccessfullyCreateListenerWrappers() {

        // Test BillingClientStateListenerWrapper
        val billingClientStateListenerWrapper =
            InAppPurchaseBillingClientWrapperV2V4.BillingClientStateListenerWrapper(mock())
        Assertions.assertThat(billingClientStateListenerWrapper).isNotNull

        // Test PurchasesUpdatedListenerWrapper
        val purchasesUpdatedListenerWrapper =
            InAppPurchaseBillingClientWrapperV2V4.PurchasesUpdatedListenerWrapper()
        Assertions.assertThat(purchasesUpdatedListenerWrapper).isNotNull
    }

    @Test
    fun testBillingClientStateListenerWrapper() {
        val proxy: Any = mock()
        var test_var = 1
        val runnable = Runnable {
            test_var = 0
        }
        val billingResult: Any = mock()
        val args = arrayOf(billingResult)

        val mockMethod: Method = mock()
        whenever(mockMethod.name).thenReturn(METHOD_ON_BILLING_SETUP_FINISHED)
        PowerMockito.mockStatic(InAppPurchaseUtils::class.java)
        whenever(getClass(anyOrNull())).thenReturn(
            Class.forName(exampleClassName)
        )
        whenever(getMethod(anyOrNull(), anyOrNull())).thenReturn(mockMethod)
        whenever(invokeMethod(anyOrNull(), anyOrNull(), eq(billingResult))).thenReturn(
            0
        )
        InAppPurchaseBillingClientWrapperV2V4.BillingClientStateListenerWrapper(runnable)
            .invoke(proxy, mockMethod, args)
        Assertions.assertThat(test_var).isEqualTo(0)

    }

    @Test
    fun testBillingClientWrapper() {
        val runnable: Runnable = mock()
        val purchaseHistoryResponseListenerWrapper =
            inAppPurchaseBillingClientWrapperV2V4.PurchaseHistoryResponseListenerWrapper(runnable)
        Assertions.assertThat(purchaseHistoryResponseListenerWrapper).isNotNull

        Whitebox.setInternalState(
            inAppPurchaseBillingClientWrapperV2V4, "historyPurchaseSet", HashSet<Any>()
        )

        val purchaseHistoryRecord =
            "{\"productId\":\"coffee\",\"purchaseToken\":\"aedeglbgcjhjcjnabndchooe.AO-J1Oydf8j_hBxWxvsAvKHLC1h8Kw6YPDtGERpjCWDKSB0Hd6asHyo5E_NjbPg1u1hW5rW-s4go3d0D_DjFstxDA6zn9H_85ReDVbQBdgb2VAAyTX39jcM\",\"purchaseTime\":1614677061238,\"developerPayload\":null}\n"
        val mockList: MutableList<Any> = arrayListOf(purchaseHistoryRecord)
        PowerMockito.mockStatic(InAppPurchaseUtils::class.java)
        whenever(invokeMethod(anyOrNull(), anyOrNull(), any())).thenReturn(purchaseHistoryRecord)
        Whitebox.setInternalState(inAppPurchaseBillingClientWrapperV2V4, "packageName", "value")

        val mockMethod: Method = mock()
        whenever(mockMethod.name).thenReturn("onPurchaseHistoryResponse")
        purchaseHistoryResponseListenerWrapper.invoke(
            mock(), mockMethod, arrayOf(listOf<String>(), mockList)
        )

        val purchaseDetailsMap = InAppPurchaseBillingClientWrapperV2V4.purchaseDetailsMap
        Assertions.assertThat(purchaseDetailsMap).isNotEmpty
    }

    @Test
    fun testSkuDetailsResponseListenerWrapper() {
        // Test can successfully create skuDetailsResponseListenerWrapper
        val runnable: Runnable = mock()
        val skuDetailsResponseListenerWrapper =
            inAppPurchaseBillingClientWrapperV2V4.SkuDetailsResponseListenerWrapper(runnable)
        Assertions.assertThat(skuDetailsResponseListenerWrapper).isNotNull

        val skuDetailExample =
            "{\"productId\":\"coffee\",\"type\":\"inapp\",\"price\":\"$0.99\",\"price_amount_micros\":990000,\"price_currency_code\":\"USD\",\"title\":\"cf (coffeeshop)\",\"description\":\"cf\",\"skuDetailsToken\":\"AEuhp4I4Fby7vHeunJbyRTraiO-Z04Y5GPKRYgZtHVCTfmiIhxHj41Rt7kgywkTtIRxP\"}\n"
        val mockList: MutableList<Any> = arrayListOf(skuDetailExample)
        PowerMockito.mockStatic(InAppPurchaseUtils::class.java)
        whenever(invokeMethod(anyOrNull(), anyOrNull(), any())).thenReturn(skuDetailExample)

        val mockMethod: Method = mock()
        whenever(mockMethod.name).thenReturn("onSkuDetailsResponse")
        skuDetailsResponseListenerWrapper.invoke(
            mock(), mockMethod, arrayOf(listOf<String>(), mockList)
        )
        val skuDetailsMap = InAppPurchaseBillingClientWrapperV2V4.skuDetailsMap
        Assertions.assertThat(skuDetailsMap).isNotEmpty
    }
}
