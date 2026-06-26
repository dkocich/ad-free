package ch.abertschi.adfree

import android.app.Notification
import android.service.notification.StatusBarNotification
import android.util.Log
import ch.abertschi.adfree.model.PreferencesFactory
import java.lang.Exception

class GoogleCastManager(val prefs: PreferencesFactory) {

    private val TAG: String = "GoogleCastManager"

    companion object {
        private val ID = "com.google.android.gms|g:com.google.android.gms.cast.rcn.NOTIFICATIONS"
    }

    private var enabled: Boolean = false
    private var action: Notification.Action? = null

    init {
        enabled = prefs.isGoogleCastEnabled()
    }

    fun setEnabled(e: Boolean) {
        enabled = e
        prefs.setGoogleCastEnabled(e)
    }

    fun isEnabled(): Boolean {
        return enabled
    }

//            val act = sbn.notification.actions.get(1)
//            info { act.title }
//            act?.run {
//                if (act.title.contains("Unmute")) {
//                    act.actionIntent.send()
//                    info { "send intent" }
//                }
//            }
//            recordNotification(sbn)
//            info { "debug this" }

    fun updateNotification(sbn: StatusBarNotification) {
        if (sbn.groupKey.contains(ID)) {
            Log.i(TAG, sbn.groupKey)
            if (sbn.notification?.actions?.size == 4) {
                val act = sbn.notification.actions[1]
                Log.i(TAG, "updating action for chromecast manager")
                Log.i(TAG, "${act.title}")
                Log.i(TAG, "$act")
                action = act
            }
        }
    }

    fun muteAudio() {
        if (!enabled) return
        try {
            Log.i(TAG, "muting google cast audio with action $action")
            action?.run { action?.actionIntent?.send() }
        } catch (e: Exception) {
            Log.w(TAG, "muting failed")
            Log.w(TAG, e)
        }

    }

    fun unmuteAudio() {
        if (!enabled) return
        try {
            Log.i(TAG, "unmuting google cast audio with action $action")
            action?.run { action?.actionIntent?.send() }
        } catch (e: Exception) {
            Log.w(TAG, "unmuting failed")
            Log.w(TAG, e)
        }
    }
}