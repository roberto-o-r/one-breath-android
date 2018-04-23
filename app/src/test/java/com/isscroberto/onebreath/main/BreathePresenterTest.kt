package com.isscroberto.onebreath.main

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test

class BreathePresenterTest {

    private val mBreatheView: BreatheContract.View = mock()

    private lateinit var mBreathePresenter: BreatheContract.Presenter

    @Before
    fun setUp() {
        // Get a reference to the class under test
        mBreathePresenter = BreathePresenter(mBreatheView)
    }

    @Test
    fun start_PopulatesTimesAndLoadConfiguration() {
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


}