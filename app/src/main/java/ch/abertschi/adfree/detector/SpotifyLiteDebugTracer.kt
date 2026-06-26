package ch.abertschi.adfree.detector

import java.io.File

class SpotifyLiteDebugTracer(storageFolder: File?) : AdDetectable,
    AbstractDebugTracer(storageFolder) {

    private val PACKAGE = "com.spotify.lite"
    private val FILENAME = "adfree-spotify-lite.txt"

    override fun getPackage() = PACKAGE
    override fun getFileName() = FILENAME

    override fun getMeta(): AdDetectorMeta = AdDetectorMeta(
        "Spotify Lite tracer",
        "dump spotify lite notifications to a file. This is for debugging only. ", false,
        category = "Developer",
        debugOnly = true
    )

    override fun canHandle(p: AdPayload): Boolean {
        return super.canHandle(p)
    }
}
