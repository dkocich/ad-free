package ch.abertschi.adfree.detector

import java.io.File

class SpotifyNotificationDebugTracer(storageFolder: File?) : AdDetectable,
    AbstractDebugTracer(storageFolder) {

    val SPOTIFY_PACKAGE = "com.spotify"
    val FILENAME = "adfree-spotify.txt"

    override fun getPackage() = SPOTIFY_PACKAGE
    override fun getFileName() = FILENAME
    override fun getMeta(): AdDetectorMeta = AdDetectorMeta(
        "Spotify tracer",
        "dump spotify notifications to a file. This is for debugging only. ", false,
        category = "Developer",
        debugOnly = true
    )

    override fun canHandle(p: AdPayload): Boolean {
        return super.canHandle(p)
    }
}
