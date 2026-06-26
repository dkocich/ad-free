package ch.abertschi.adfree

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationManagerCompat


class NotificationStatusManager(val context: Context) {

    private val TAG: String = "NotificationStatusManager"

    private val TIMER_INTERVAL_MS: Long = 60 * 1000

    private var lastStatus: ListenerStatus = ListenerStatus.UNKNOWN

    private var observers: MutableList<NotificationStatusObserver> = ArrayList()

    fun addObserver(o: NotificationStatusObserver) {
        observers.add(o)
    }

    fun notifyStatusChanged(s: ListenerStatus) {
        Log.i(TAG, "Notification Listener Status Changed: $s")
        lastStatus = s
        observers.forEach { it.onStatusChanged(s) }
    }

    fun getStatus(): ListenerStatus {
        val names = NotificationManagerCompat.getEnabledListenerPackages(context)
        if (names.contains(context.packageName)) {
            lastStatus = ListenerStatus.CONNECTED
        } else {
            lastStatus = ListenerStatus.DISCONNECTED
        }

        Log.i(TAG, "Notification Listener Status : ${lastStatus}")
        return lastStatus
    }

    fun forceTimedRestart() {
        // TODO: option to remove timer once enabled?
        val serviceintent = Intent(this.context, NotificationsListeners::class.java)
        val pendingintent = PendingIntent.getService(this.context, 0, serviceintent, PendingIntent.FLAG_CANCEL_CURRENT)
        val alarm = this.context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarm.cancel(pendingintent)
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), TIMER_INTERVAL_MS, pendingintent)
        Log.i(TAG, "Setting wakeup with alarmmanager every $TIMER_INTERVAL_MS ms")
    }


    fun restartNotificationListener() {
        Log.i(TAG, "restarting notification listener")
        restartComponentService()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val componentName = ComponentName(context.applicationContext,
                    NotificationsListeners::class.java)

        } else {
            Log.w(TAG, "restart notification listener is not supported for current v. of android")
        }

    }

    private fun restartComponentService() {
        val pm = context.packageManager
        pm.setComponentEnabledSetting(ComponentName(this.context, NotificationsListeners::class.java),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
        pm.setComponentEnabledSetting(ComponentName(this.context, NotificationsListeners::class.java),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
    }
}

enum class ListenerStatus {
    CONNECTED, DISCONNECTED, UNKNOWN
}

interface NotificationStatusObserver {
    fun onStatusChanged(status: ListenerStatus)
}
