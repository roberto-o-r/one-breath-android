package com.isscroberto.onebreath.main

import android.os.CountDownTimer
import com.isscroberto.onebreath.data.Config

/**
 * Created by roberto.orozco on 07/02/2018.
 */
class BreathePresenter(view: BreatheContract.View) : BreatheContract.Presenter {

    val view = view
    lateinit var timer: CountDownTimer

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
        view.hideConfiguration();
        // Start animation.
        view.startAnimation();
        // Start timer.
        timer = object : CountDownTimer(config.minutes.toLong() * 60 * 1000, 1000) {
            override fun onFinish() {
                stopBreathing()
            }
            override fun onTick(millisUntilFinished: Long) {

            }
        }.start()
    }

    override fun stopBreathing() {
        // Stop timer.
        timer.cancel()
        // Stop animation.
        view.stopAnimation();
    }

}