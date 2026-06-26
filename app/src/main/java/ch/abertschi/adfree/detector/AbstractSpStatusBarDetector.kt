/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.detector


/**
 * Created by abertschi on 13.12.16.
 */
abstract class AbstractSpStatusBarDetector : AdDetectable {

    companion object {
        private val SPOTIFY_PACKAGE = "com.spotify"
    }

    override fun canHandle(p: AdPayload): Boolean {
        return p.statusbarNotification?.key?.lowercase()?.contains(SPOTIFY_PACKAGE) ?: false
    }


}
