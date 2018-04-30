package com.isscroberto.onebreath.main

import com.isscroberto.onebreath.data.Config

/**
 * Created by roberto.orozco on 07/02/2018.
 */
class BreathePresenter(view: BreatheContract.View) : BreatheContract.Presenter {

    val view = view

    init {
        view.setPresenter(this)
    }

    override fun start() {
        // Create list for times.
        val list = ArrayList<String>()
        list.add("2 min")
        list.add("5 min")
        list.add("10 min")

        // Load configuration values.
        view.loadConfiguration(list)
    }

    override fun startBreathing(config: Config) {
        // Hide configuration.
        view.hideConfiguration()
        // Start animation.
        view.startAnimation()
        // Start timer.
        view.startTimer()
    }

    override fun stopBreathing() {
        // Stop timer.
        view.stopTimer()
        // Stop animation.
        view.stopAnimation()
    }

}