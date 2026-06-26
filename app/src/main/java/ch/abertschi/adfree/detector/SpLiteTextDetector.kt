package ch.abertschi.adfree.detector

open class SpLiteTextDetector : AdDetectable, AbstractNotificationDetector() {

    override fun getPackageName() = "com.spotify.lite"

    open fun detectAsAdvertisement(
        payload: AdPayload,
        title: Pair<String?, Boolean>,
        text: Pair<String?, Boolean>,
        subtext: Pair<String?, Boolean>
    ): Boolean {
        if (listOf(title.second, text.second, subtext.second).contains(false)) {
            return false
        }
        // title first is advertisement in english version
        return title.first != null && title.first!!.isNotEmpty()
                && text.first == null && subtext.first != null
    }

    override fun getMeta(): AdDetectorMeta = AdDetectorMeta(
        "Generic Text detector", "detector for presence of text for spotify lite",
        true,
        category = "Spotify Lite",
        debugOnly = false
    )

    override fun flagAsAdvertisement(payload: AdPayload): Boolean {
        return false
    }
}