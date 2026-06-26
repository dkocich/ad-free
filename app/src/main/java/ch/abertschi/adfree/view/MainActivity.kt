/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import ch.abertschi.adfree.AdFreeApplication
import ch.abertschi.adfree.R
import ch.abertschi.adfree.view.home.HomeActivity
import ch.abertschi.adfree.view.setting.AboutActivity
import ch.abertschi.adfree.view.setting.SettingsActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Created by abertschi on 21.04.17.
 */

class MainActivity : FragmentActivity() {

    companion object {
        private val NUM_PAGES = 3
    }

    private var mPager: ViewPager2? = null
    private var mPagerAdapter: FragmentStateAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)


        mPager = findViewById(R.id.pager)
        mPagerAdapter = ScreenSlidePagerAdapter(this)
        mPager!!.adapter = mPagerAdapter

        val tabLayout = findViewById<TabLayout>(R.id.tabDots)
        TabLayoutMediator(tabLayout, mPager!!) { _, _ -> }.attach()
        window.navigationBarColor = Color.parseColor("#252A2E")

        // XXX: Workaround, global access to activity to prevent detached fragments
        var app = applicationContext as AdFreeApplication
        app.mainActivity = this
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return HomeActivity()
                1 -> return SettingsActivity()
                else -> return AboutActivity()
            }
        }

        override fun getItemCount(): Int {
            return NUM_PAGES
        }
    }
}