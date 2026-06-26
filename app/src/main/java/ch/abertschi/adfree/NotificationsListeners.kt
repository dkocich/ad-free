/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import ch.abertschi.adfree.detector.AdPayload
import com.thoughtworks.xstream.XStream
import java.io.File
import java.io.FileOutputStream
import android.app.Service
import android.content.Intent

import android.util.Log

/**
 * Created by abertschi on 11.12.16.
 */
class NotificationsListeners : NotificationListenerService() {

    private val TAG = "NotificationsListeners"

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val context = applicationContext as AdFreeApplication
        val cast = context.googleCast
        if (cast.isEnabled()) cast.updateNotification(sbn)
        context.adDetector.applyDetectors(AdPayload(sbn))
    }

    override fun onListenerDisconnected() {
        Log.i(TAG, "on notification listener disconnected")
        super.onListenerDisconnected()
        val context = applicationContext as AdFreeApplication
        context.notificationStatus.notifyStatusChanged(ListenerStatus.DISCONNECTED)
    }

    override fun onListenerConnected() {
        Log.i(TAG, "on notification listener connected")
        super.onListenerConnected()
        val context = applicationContext as AdFreeApplication
        context.notificationStatus.notifyStatusChanged(ListenerStatus.CONNECTED)

        if (context.prefs.isAlwaysOnNotificationEnabled()) {
            Log.i(TAG, "showing always-on notification")
            val pair = context.notificationChannel.buildAlwaysOnNotification()
            startForeground(pair.second, pair.first)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "Starting ad-free notificationsListener")
        return Service.START_STICKY
    }

    @Deprecated("for testing only")
    private fun recordNotification(sbn: StatusBarNotification) {
        val path = this.getExternalFilesDir(null)
        val file = File(path, "adfree-new.txt")
        val ids = File(path, "adfree-ids-new.txt")

        Log.w(TAG, XStream().toXML(sbn))
        val stream = FileOutputStream(file, true)
        try {
            stream.write(XStream().toXML(sbn).toByteArray())
        } finally {
            stream.close()
        }
        val stream2 = FileOutputStream(ids, true)
        try {
            stream2.write((sbn.id.toString() + " / " + sbn.groupKey + "\n").toByteArray())
        } finally {
            stream2.close()
        }
    }
}