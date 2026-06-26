package ch.abertschi.adfree.detector

import android.app.Notification

class ScDetector : AdDetectable {

    private val keyword: String = "advertisement"
    private val pack = "com.soundcloud.android"

    override fun canHandle(p: AdPayload): Boolean {
        return p.statusbarNotification?.key?.lowercase()?.contains(pack) ?: false
    }

    override fun flagAsAdvertisement(payload: AdPayload): Boolean {
        val extras = payload.statusbarNotification.notification?.extras
        val title: String? = extras?.getString(Notification.EXTRA_TITLE)?.trim()?.lowercase()
        val subTitle: String? = extras?.getString(Notification.EXTRA_SUB_TEXT)

        return title != null && title == keyword
                && subTitle == null
    }

    override fun getMeta(): AdDetectorMeta = AdDetectorMeta(
        "Soundcloud", "experimental detector for soundcloud (english)",
        true,
        category = "Soundcloud",
        debugOnly = false
    )
}