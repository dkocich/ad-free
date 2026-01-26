package ch.abertschi.adfree.detector

import org.jetbrains.anko.AnkoLogger
import java.io.File

class ScNotificationDebugTracer(storageFolder: File?) : AdDetectable, AnkoLogger,
    AbstractDebugTracer(storageFolder) {

    val SOUNDCLOUD_PACKAGE = "com.soundcloud.android"
    val FILENAME = "adfree-soundcloud.txt"

    override fun getPackage() = SOUNDCLOUD_PACKAGE
    override fun getFileName() = FILENAME

    override fun getMeta(): AdDetectorMeta = AdDetectorMeta(
        "Soundcloud tracer",
        "dump soundcloud notifications to a file. This is for debugging only and drains more battery",
        false,
        category = "Developer",
        debugOnly = true
    )
}
