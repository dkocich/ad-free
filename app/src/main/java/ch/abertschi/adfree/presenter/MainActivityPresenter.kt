/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.presenter

import ch.abertschi.adfree.model.PreferencesFactory
import ch.abertschi.adfree.view.about.AboutView

/**
 * Created by abertschi on 02.09.17.
 */
class MainActivityPresenter(val view: AboutView, val preferencesFactory: PreferencesFactory) {

    private var isInit: Boolean = false

    fun onCreate() {
        isInit = true
    }

    fun onResume() {
    }

}