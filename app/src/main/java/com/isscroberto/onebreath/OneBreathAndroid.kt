package com.isscroberto.onebreath

import android.app.Application
import com.github.stkent.amplify.feedback.DefaultEmailFeedbackCollector
import com.github.stkent.amplify.feedback.GooglePlayStoreFeedbackCollector
import com.github.stkent.amplify.tracking.Amplify
import com.google.android.gms.ads.MobileAds

/**
 * Created by roberto.orozco on 11/02/2018.
 */
class OneBreathAndroid: Application() {

    override fun onCreate() {
        super.onCreate()

        // Setup AdMob.
        MobileAds.initialize(this, getString(R.string.ad_mob_app_id));

        // Setup feedback collector.
        Amplify.initSharedInstance(this)
                .setPositiveFeedbackCollectors(GooglePlayStoreFeedbackCollector())
                .setCriticalFeedbackCollectors(DefaultEmailFeedbackCollector(getString(R.string.my_email)))
                .applyAllDefaultRules()
                //.setAlwaysShow(BuildConfig.DEBUG)
    }
}