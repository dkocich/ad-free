/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.view.home

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ch.abertschi.adfree.R
import ch.abertschi.adfree.di.HomeModul
import ch.abertschi.adfree.presenter.HomePresenter
import ch.abertschi.adfree.view.ViewSettings

/**
 * Created by abertschi on 15.04.17.
 */

class HomeActivity() : Fragment(), HomeView {
    private lateinit var typeFace: Typeface
    private lateinit var enjoySloganText: TextView
    private lateinit var homePresenter: HomePresenter
    private lateinit var updateMessageInfo: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homePresenter = HomeModul(this.activity!!, this).provideSettingsPresenter()
        

        typeFace = ViewSettings.instance(this.context!!).typeFace

        enjoySloganText = view.findViewById(R.id.enjoy) as TextView
        updateMessageInfo =
                view.findViewById(R.id.version_update_reminder) as TextView

        view.findViewById<TextView>(R.id.troubleshooting).setOnClickListener {
            homePresenter.onTroubleshooting()
        }

        homePresenter.onCreate(this.context!!)

        // TODO: this is debug code
//        val r: Random = Random()
//        val c: AdFreeApplication = globalContext.applicationContext as AdFreeApplication
//        view.onTouch { view, motionEvent ->
//            info { "AdFree event created" }
//            when (r.nextBoolean()) {
//                true -> c.adDetector.notifyObservers(AdEvent(EventType.IS_AD))
//                else -> c.adDetector.notifyObservers(AdEvent(EventType.NO_AD))
//            }
//            true
//        }
    }

    override fun showUpdateMessage(show: Boolean) {
        if (show ){
            updateMessageInfo.visibility = View.VISIBLE
            updateMessageInfo.setOnClickListener {
                homePresenter.onUpdateMessageClicked()
            }
        } else {
            updateMessageInfo.visibility = View.GONE
        }

    }

    override fun onResume() {
        homePresenter.onResume(this.context!!)
        super.onResume()
    }

    override fun showPermissionRequired() {
        val text = getString(R.string.slogan_grant_permission)
        setSloganText(text)
        enjoySloganText.setOnClickListener {
            showNotificationPermissionSettings()
        }
    }

    override fun showNotificationPermissionSettings() {
        startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
    }

    private fun setSloganText(text: String) {
        enjoySloganText.typeface = typeFace
        enjoySloganText.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(text)
        }
    }

    override fun showEnjoyAdFree() {
        val text = getString(R.string.slogan_enjoy)
        setSloganText(text)
        enjoySloganText.setOnClickListener(null)
    }

//    override fun setPowerState(state: Boolean) {
//        powerButton.isChecked = state
//    }
}