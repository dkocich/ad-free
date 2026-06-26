package ch.abertschi.adfree.model

import android.content.Context
import ch.abertschi.adfree.detector.*

class AdDetectableFactory(val context: Context, val prefs: PreferencesFactory) {

    private var detectors: List<AdDetectable>

    init {
        this.detectors = listOf(
            BestEffortTextDetector(),
            ch.abertschi.adfree.detector.NotificationActionDetector(),
            ch.abertschi.adfree.detector.MiuiNotificationDetector(),
            ch.abertschi.adfree.detector.SpotifyTitleDetector(TrackRepository(context, prefs.getPreferences())),
            ch.abertschi.adfree.detector.UserDefinedTextDetector(TextRepository(context, prefs.getPreferences())),
            ch.abertschi.adfree.detector.AccuradioDetector(),
            ch.abertschi.adfree.detector.ScDetector(),
            ch.abertschi.adfree.detector.DeezerTextDetector(),
            ch.abertschi.adfree.detector.SpLiteTextDetector(),
            ch.abertschi.adfree.detector.SpLiteTextEnglishDetector(),

            ch.abertschi.adfree.detector.SpotifyNotificationDebugTracer(context.getExternalFilesDir(null)),
            ch.abertschi.adfree.detector.ScNotificationDebugTracer(context.getExternalFilesDir(null)),
            ch.abertschi.adfree.detector.DeezerDebugTracer(context.getExternalFilesDir(null)),
            ch.abertschi.adfree.detector.SpotifyLiteDebugTracer(context.getExternalFilesDir(null)),
            ch.abertschi.adfree.detector.TidalDebugTracer(context.getExternalFilesDir(null)),
            ch.abertschi.adfree.detector.AccuRadioDebugTracer(context.getExternalFilesDir(null)),

            ch.abertschi.adfree.detector.DummySpotifyDetector(),
            ch.abertschi.adfree.detector.DummyGlobal()
        )
    }

    fun getEnabledDetectors(): List<AdDetectable> = detectors.filter { isEnabled(it) }

    fun getVisibleDetectors(): List<AdDetectable> = detectors.filter { !it.getMeta().debugOnly }

    fun getAllDetectors(): List<AdDetectable> = detectors

    fun getCategories(): List<String> = detectors.map { it.getMeta().category }.distinct()

    fun getVisibleCategories(): List<String> {
        var visible = getVisibleDetectors().map { it.getMeta().category }.distinct().toMutableList()
        if (prefs.isDeveloperModeEnabled()) {
            visible.add("Developer")
        }
        return visible
    }

    fun getDetectorsForCategory(category: String) = detectors.filter { it.getMeta().category == category }

    fun isEnabled(d: AdDetectable): Boolean = prefs.isAdDetectableEnabled(d)

    fun setEnable(e: Boolean, d: AdDetectable) = prefs.saveAdDetectableEnable(e, d)

    fun isAdfreeEnabled(): Boolean = prefs.isBlockingEnabled()

    fun setAdfreeEnabled(e: Boolean) = prefs.setBlockingEnabled(e)

    fun persistMeta() = prefs.setBlockingEnabled(prefs.isBlockingEnabled())
}