package ch.abertschi.adfree.crashhandler

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.abertschi.adfree.R
import java.io.File


// TODO: refator this into presenter and view
class SendCrashReportActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG: String = "SendCrashReportActivity"

    companion object {
        val ACTION_NAME = "ch.abertschi.adfree.SEND_LOG_CRASH"
        val EXTRA_LOGFILE = "ch.abertschi.adfree.extra.logfile"
        val EXTRA_SUMMARY = "ch.abertschi.adfree.extra.summary"
        val MAIL_ADDR = "apps@abertschi.ch"
        val SUBJECT = "[ad-free-crash-report]"
    }

    private var logfile: String? = null
    private var summary: String? = null


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            parseIntent(this.intent)
            doOnCreate()
        } catch (e: Exception) {
            Log.w(TAG, e)
            Toast.makeText(this, "Error: $e", Toast.LENGTH_LONG).show()
        }
    }

    fun parseIntent(i: Intent?) {
        logfile = i?.extras?.getString(EXTRA_LOGFILE)
        summary = i?.extras?.getString(EXTRA_SUMMARY) ?: ""

    }

    fun sendReport() {
        try {
            val file = File(applicationContext.filesDir, logfile)
            val log = file.readText()
            Log.i(TAG, "sending report with $file $log")
            launchSendIntent(summary!!)
        } catch (e: Exception) {
            Log.w(TAG, e.toString())

        }
    }

    private fun launchSendIntent(msg: String) {
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(MAIL_ADDR))
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg)
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT)
        sendIntent.type = "text/plain"
        this.applicationContext
                .startActivity(Intent.createChooser(sendIntent, getString(R.string.toast_choose_email)))
    }


    private fun doOnCreate() {
        setupUI()
    }

    // TODO: Send logcat output and summary
    private fun setupUI() {
        setContentView(R.layout.crash_view)
        setFinishOnTouchOutside(false)
        val v = findViewById<View>(R.id.crash_container) as View
        v.setOnClickListener(this)

        var typeFace: Typeface = Typeface.createFromAsset(baseContext.assets, "fonts/Raleway-ExtraLight.ttf")


        val title = findViewById<TextView>(R.id.crash_Title) as TextView
        title.typeface = typeFace

        title.setOnClickListener(this)

        val text =
                "success is not final, failure is not fatal: it is the " +
                        "<font color=#FFFFFF>courage</font> to <font color=#FFFFFF>continue</font> that counts. -- " +
                        "Winston Churchill"

        title.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(text)
        }

        val subtitle = findViewById<TextView>(R.id.debugSubtitle) as TextView
        subtitle.typeface = typeFace

        subtitle.setOnClickListener(this)

        val subtitletext =
                "<font color=#FFFFFF>ad-free</font> crashed. be courageous and continue. " +
                        "send the <font color=#FFFFFF>crash report </font>. tab here, choose your mail application and send the report.</font>"


        subtitle.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(subtitletext, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(subtitletext)
        }
    }

    override fun onClick(v: View) {
        Log.i(TAG, "clicking view for crashreport")
        logfile?.let {
            try {
                sendReport()
            } catch (e: Exception) {
                Log.w(TAG, "cant send crash report", e)
                e.printStackTrace()
                Toast.makeText(this, getString(R.string.toast_no_crash_report),
                        Toast.LENGTH_LONG).show()
            }
        } ?: run {
            Toast.makeText(this, getString(R.string.toast_no_crash_report),
                    Toast.LENGTH_LONG).show()
        }
    }
}