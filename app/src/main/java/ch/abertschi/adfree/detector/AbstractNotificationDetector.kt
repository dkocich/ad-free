package ch.abertschi.adfree.detector


abstract class AbstractNotificationDetector : AdDetectable {

    // get package lower case
    abstract fun getPackageName(): String

    override fun canHandle(p: AdPayload): Boolean {
        return p.statusbarNotification?.key?.lowercase()?.contains(getPackageName())
            ?: false
    }


}
