package ch.abertschi.adfree.detector

import java.io.File

class TidalDebugTracer(storageFolder: File?) : AdDetectable,
    AbstractDebugTracer(storageFolder) {

    private val PACKAGE = "com.aspiro.tidal"
    private val FILENAME = "adfree-tidal.txt"

    override fun getPackage() = PACKAGE
    override fun getFileName() = FILENAME

    override fun getMeta(): AdDetectorMeta = AdDetectorMeta(
        "Tidal tracer",
        "dump tidal notifications to a file. This is for debugging only. ", false,
        category = "Developer",
        debugOnly = true
    )

    override fun canHandle(p: AdPayload): Boolean {
        return super.canHandle(p)
    }
}
