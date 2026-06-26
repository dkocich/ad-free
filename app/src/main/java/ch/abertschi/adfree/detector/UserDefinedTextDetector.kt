package ch.abertschi.adfree.detector

import android.app.Notification
import android.os.Bundle
import android.util.Log
import ch.abertschi.adfree.model.TextRepository
import com.thoughtworks.xstream.XStream

class UserDefinedTextDetector(private val repo: TextRepository) : AdDetectable {

    private val TAG: String = "UserDefinedTextDetector"

    override fun canHandle(p: AdPayload): Boolean {
        var notificationKey: String? =
            p.statusbarNotification?.key?.lowercase() ?: return false

        var canHandle = false;
        for (entry in repo.getAllEntries()) {
            val key = entry.packageName
            if (key.isEmpty() || key.isBlank()) {
                continue
            }
            if (notificationKey?.contains(key.lowercase().trim()) == true) {
                p.matchedTextDetectorEntries.add(entry)
                canHandle = true;
            }
        }
        return canHandle
    }

    private fun extractString(extras: Bundle?, s: String): String? {
        return try {
            (extras?.getString(s) as CharSequence?)
                ?.toString()?.trim()?.lowercase()
        } catch (e: Exception) {
            Log.w(TAG, e)
            null
        }
    }

    // Use a fixed approach and search for predefined fields
    private fun flagAsAdvertisementFixed(payload: AdPayload): Boolean {
        val extras = payload.statusbarNotification.notification?.extras
        val title = extractString(extras, Notification.EXTRA_TITLE)
        val subTitle = extractString(extras, Notification.EXTRA_SUB_TEXT)

        for (entry in payload.matchedTextDetectorEntries) {
            for (entryLine in entry.content) {
                if (entryLine.trim().isEmpty()) {
                    continue
                }
                val matchTitle = title != null && title.contains(entryLine.trim().lowercase())
                val matchSubtitle =
                    subTitle != null && subTitle.contains(entryLine.trim().lowercase())
                if (matchTitle || matchSubtitle) {
                    return true;
                }
            }
        }
        return false;
    }

    private fun flagAsAdvertisementDynamic(payload: AdPayload): Boolean {
        /*
         * XXX: This implementation is inefficient but simple.
         * Will a reflection approach be better?
         */
        val str = XStream().toXML(payload)!!.lowercase()
        for (entry in payload.matchedTextDetectorEntries) {
            for (entryLine in entry.content) {
                if (entryLine.trim().isEmpty()) {
                    continue
                }
                if (str.contains(entryLine.trim().lowercase())) {
                    return true
                }
            }
        }
        return false
    }
    override fun flagAsAdvertisement(payload: AdPayload)  =
        flagAsAdvertisementFixed(payload) || flagAsAdvertisementDynamic(payload)

    override fun getMeta(): AdDetectorMeta = AdDetectorMeta(
        "User defined text", "flag a notification based on the presence of text",
        false,
        category = "General",
        debugOnly = false
    )
}