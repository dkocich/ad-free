package ch.abertschi.adfree

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class StartOnBootListener: BroadcastReceiver() {

    private val TAG = "StartOnBootListener"

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(TAG, "launching ad-free on boot. Hello world")

        val app = context?.applicationContext as AdFreeApplication
        // launching ad-free application class on boot to initialize ad-free
        // see AdFreeApplication
        Log.i(TAG, "$app")
    }
}