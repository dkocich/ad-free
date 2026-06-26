package ch.abertschi.adfree.detector

import android.app.Notification

class DeezerTextDetector : AdDetectable {

    private val keyword: String = "deezer"
    private val pack = "deezer.android"

    override fun canHandle(p: AdPayload): Boolean {
        return p.statusbarNotification?.key?.lowercase()?.contains(pack) ?: false
    }

//    Format:
//    <string>android.title</string>
//    <string>Deezer</string>
//    <string>android.reduced.images</string>
//    <boolean>true</boolean>
//    <string>android.subText</string>
//    <null/>

    override fun flagAsAdvertisement(payload: AdPayload): Boolean {
        val extras = payload.statusbarNotification.notification?.extras
        val title: String? = extras?.getString(Notification.EXTRA_TITLE)?.trim()?.lowercase()
        val subTitle: String? = extras?.getString(Notification.EXTRA_SUB_TEXT)

        return title != null && title == keyword
                && subTitle == null
    }

    override fun getMeta(): AdDetectorMeta = AdDetectorMeta(
        "Deezer", "notification text based detector for deezer",
        true,
        category = "Deezer",
        debugOnly = false
    )
}