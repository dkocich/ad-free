package ch.abertschi.adfree.detector

import android.service.notification.StatusBarNotification
import android.util.Log
import com.thoughtworks.xstream.XStream
import java.io.File
import java.io.FileOutputStream

abstract class AbstractDebugTracer(val storageFolder: File?) : AdDetectable {

    private val TAG: String = "AbstractDebugTracer"

    abstract fun getPackage(): String
    abstract fun getFileName(): String

    override fun canHandle(p: AdPayload): Boolean {
        if (storageFolder == null) {
            Log.w(TAG, "Given storageFolder is null, cant work. Disabling functionality ...")
            return false
        }

        if (p.statusbarNotification?.key?.lowercase()?.contains(getPackage()) == true) {
            recordNotification(p.statusbarNotification)
        }
        return false
    }

    private fun recordNotification(sbn: StatusBarNotification) {
        val file = File(storageFolder, getFileName())
        Log.i(TAG, XStream().toXML(sbn))
        Log.i(TAG, "writing notification content to $file}")

        val stream = FileOutputStream(file, true)
        try {
            stream.write(XStream().toXML(sbn).toByteArray())
        } catch (e: Exception) {
            Log.i(TAG, e.toString())
        } finally {
            stream.close()
        }
    }
}
