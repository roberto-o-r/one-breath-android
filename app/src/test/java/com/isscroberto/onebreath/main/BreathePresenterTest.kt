package com.isscroberto.onebreath.main

import android.graphics.Bitmap
import android.os.CountDownTimer
import com.isscroberto.onebreath.data.Config
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class BreathePresenterTest {

    private val mBreatheView: BreatheContract.View = mock()

    private lateinit var mBreathePresenter: BreatheContract.Presenter

    @Before
    fun setUp() {
        // Get a reference to the class under test
        mBreathePresenter = BreathePresenter(mBreatheView)
    }

    @Test
    fun start_PopulatesTimesLoadConfiguration() {
        // List of breathing times.
        val list = ArrayList<String>()
        list.add("2 min")
        list.add("5 min")
        list.add("10 min")

        // When the presenter starts.
        mBreathePresenter.start();

        // View loads configuration with populated breathing times.
        verify(mBreatheView).loadConfiguration(list)
    }

    @Test
    fun startBreathing_startAnimation() {
        val config = mock<Config>()

        // When the view request breathing start.
        mBreathePresenter.startBreathing(config);

        // Hide configuration elements in view.
        verify(mBreatheView).hideConfiguration()
        // Start animation in view.
        verify(mBreatheView).startAnimation()
        // Start countdown timer in view.
        verify(mBreatheView).startTimer()
    }

    @Test
    fun stopBreathing_stopTimerStopAnimation() {
        // When the view request breathing stops.
        mBreathePresenter.stopBreathing()

        // Stop timer in view.
        verify(mBreatheView).stopTimer()
        // Stop animations in view.
        verify(mBreatheView).stopAnimation()
    }

}