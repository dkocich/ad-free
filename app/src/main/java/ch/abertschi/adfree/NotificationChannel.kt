/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree

import android.app.Notification
import ch.abertschi.adfree.model.PreferencesFactory
import ch.abertschi.adfree.util.NotificationUtils


/**
 * Created by abertschi on 01.09.17.
 */
class NotificationChannel(val notificationUtils: NotificationUtils,
                          val prefs: PreferencesFactory) {

    private val defaultAdNotificationId: Int = 1000
    private val alwaysOnNotificationId: Int = 1001

    fun buildAlwaysOnNotification(): Pair<Notification, Int> {
        val not = notificationUtils.showTextNotification(alwaysOnNotificationId,
                notificationUtils.context.getString(R.string.app_name),
                notificationUtils.context.getString(R.string.notif_enjoy), {
            }, notifiy = false)

        return Pair(not , alwaysOnNotificationId)
    }

    fun hideAlwaysOnNotification() {
        notificationUtils.hideNotification(alwaysOnNotificationId)
    }

    fun hideDefaultAdNotification() {
        notificationUtils.hideNotification(defaultAdNotificationId)
    }

    fun showDefaultAdNotification(dismissCallable: () -> Unit = {}) {
        notificationUtils.showTextNotification(defaultAdNotificationId, notificationUtils.context.getString(R.string.notif_ad_detected),
                "touch to unmute", dismissCallable)
    }

    fun updateAdNotification(title: String? = null, content: String? = null ) {
        notificationUtils.updateTextNotificationIfAvailable(defaultAdNotificationId, title, content)
    }


}