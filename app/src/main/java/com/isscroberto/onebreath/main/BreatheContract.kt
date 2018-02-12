package com.isscroberto.onebreath.main

import com.isscroberto.onebreath.BasePresenter
import com.isscroberto.onebreath.BaseView
import com.isscroberto.onebreath.data.Config

/**
 * Created by roberto.orozco on 07/02/2018.
 */
interface BreatheContract {

    interface View : BaseView<Presenter> {
        fun loadConfiguration(list: ArrayList<String>);
        fun startAnimation()
        fun stopAnimation()
        fun hideConfiguration()
        fun showConfiguration()
    }

    interface Presenter : BasePresenter {
        fun startBreathing(config: Config)
        fun stopBreathing()
    }

}