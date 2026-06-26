/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.crashhandler

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Build
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.UUID

class CrashExceptionHandler(val context: Context) : Thread.UncaughtExceptionHandler {

    private val LINE_SEPARATOR = "\n"
    private val LOG_SUFFIX = "_log.txt"

    override fun uncaughtException(t: Thread, e: Throwable) {
        val summary = getSummary(e)
        val logFile = writeErrorToLog(summary)
        launchErrorActivity(logFile, summary)
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    private fun getSummary(e: Throwable): String {
        val report = ""
        val curDate = SimpleDateFormat("MMM d, yyyy h:m:s a").format(Calendar.getInstance().time)

        return report + curDate + LINE_SEPARATOR +
                "Error: " + e.toString() + LINE_SEPARATOR +
                "Stacktrace: " + LINE_SEPARATOR +
                e.stackTrace.joinToString("\n") +
                LINE_SEPARATOR +
                "\n\n Device Information\n" +
                LINE_SEPARATOR + LINE_SEPARATOR +
                "Brand: " + Build.BRAND + LINE_SEPARATOR +
                "Device: " + Build.DEVICE + LINE_SEPARATOR +
                "Model: " + Build.MODEL + LINE_SEPARATOR +
                "Id: " + Build.ID + LINE_SEPARATOR +
                "Product: " + Build.PRODUCT + LINE_SEPARATOR +
                "\n\n Firmware \n" +
                LINE_SEPARATOR + LINE_SEPARATOR +
                "SDK: " + Build.VERSION.SDK_INT + LINE_SEPARATOR +
                "Release: " + Build.VERSION.RELEASE + LINE_SEPARATOR +
                "Incremental: " + Build.VERSION.INCREMENTAL + LINE_SEPARATOR +
                "App version: " + getAppVersion() + LINE_SEPARATOR
    }

    private fun getAppVersion(): String {
        return try {
            val pInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            pInfo.versionName ?: "n/a"
        } catch (e: Exception) {
            "n/a"
        }
    }

    private fun writeErrorToLog(summary: String): String {
        val logFile = "${UUID.randomUUID()}$LOG_SUFFIX"
        val fos: FileOutputStream = context.openFileOutput(logFile, Context.MODE_PRIVATE)
        fos.write(summary.toByteArray())
        fos.close()
        return logFile
    }

    private fun launchErrorActivity(logFile: String, summary: String) {
        val intent = Intent(context, SendCrashReportActivity::class.java)
        intent.putExtra(SendCrashReportActivity.EXTRA_LOGFILE, logFile)
        intent.putExtra(SendCrashReportActivity.EXTRA_SUMMARY, summary)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}