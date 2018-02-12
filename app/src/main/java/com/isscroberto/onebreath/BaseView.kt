package com.isscroberto.onebreath

/**
 * Created by roberto.orozco on 07/02/2018.
 */
interface BaseView<T> {
    fun setPresenter(presenter: T);
}