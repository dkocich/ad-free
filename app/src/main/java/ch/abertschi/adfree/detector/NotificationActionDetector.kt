package ch.abertschi.adfree.detector

import android.app.Notification

class NotificationActionDetector : AbstractSpStatusBarDetector() {

    override fun flagAsAdvertisement(payload: AdPayload): Boolean {
        // If no actions are present, we probably have an ad
        val notification = payload.statusbarNotification.notification
        return notification?.actions == null || notification.actions.isEmpty()
    }

    override fun getMeta(): AdDetectorMeta = AdDetectorMeta(
        "Notification actions", "spotify detector for notification actions",
        category = "Spotify"
    )
}