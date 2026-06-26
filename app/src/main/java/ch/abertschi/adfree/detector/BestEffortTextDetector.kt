package ch.abertschi.adfree.detector

import android.os.Bundle

class BestEffortTextDetector : AdDetectable {

    override fun canHandle(p: AdPayload): Boolean {
        return p.statusbarNotification?.key?.lowercase()?.contains(getPackageName()) ?: false
    }

    fun detectAsAdvertisement(
        payload: AdPayload,
        title: Pair<String?, Boolean>,
        text: Pair<String?, Boolean>,
        subtext: Pair<String?, Boolean>
    ): Boolean {
        // implementation removed as it was empty
        return false
    }

    override fun getMeta(): AdDetectorMeta = AdDetectorMeta(
        "Best effort text detector",
        "a general purpose detector for text in notifications",
        false,
        category = "General",
        debugOnly = true
    )

    fun getPackageName(): String = ""
    override fun flagAsAdvertisement(payload: AdPayload): Boolean {
        return false
    }
}