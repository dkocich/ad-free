package ch.abertschi.adfree.detector

import android.os.Bundle
import android.util.Log

abstract class AbstractNotificationBundleAndroidTextDetector : AdDetectable, AbstractNotificationDetector() {

    private val TAG: String = "AbstractNotificationBundleAndroidTextDetector"

    open fun extractString(extras: Bundle?, key: String): Pair<String?, Boolean> {
        return try {
            Pair(
                (extras?.getString(key) as CharSequence?)
                    ?.toString()?.trim()?.lowercase(), true
            )
        } catch (e: Exception) {
            Log.w(TAG, e)
            Pair(null, false)
        }
    }


    override fun flagAsAdvertisement(payload: AdPayload): Boolean {
        val extras = payload.statusbarNotification.notification?.extras
        val title = extractString(extras, "android.title")
        val text = extractString(extras, "android.text")
        val subtext = extractString(extras, "android.subText")
        return detectAsAdvertisement(payload, title, text, subtext)
    }


    abstract fun detectAsAdvertisement(
        p: AdPayload,
        title: Pair<String?, Boolean>,
        text: Pair<String?, Boolean>,
        subtext: Pair<String?, Boolean>
    ): Boolean
}