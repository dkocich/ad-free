package ch.abertschi.adfree.detector

import org.jetbrains.anko.AnkoLogger
import java.io.File

class SpotifyNotificationDebugTracer(storageFolder: File?) : AdDetectable, AnkoLogger,
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
}
