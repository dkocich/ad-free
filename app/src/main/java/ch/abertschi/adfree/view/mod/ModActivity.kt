package ch.abertschi.adfree.view.mod

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import ch.abertschi.adfree.AdFreeApplication
import ch.abertschi.adfree.R


class ModActivity : AppCompatActivity() {

    private lateinit var delayDialog: AlertDialog

    private lateinit var delayLayout: View
    private var enabledSwitch: SwitchCompat? = null
    private var alwaysOnSwitch: SwitchCompat? = null
    private lateinit var presenter: ModPresenter
    private var onCreateActive = true

    override fun onCreate(savedInstanceState: Bundle?) {
        onCreateActive = true;
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mod_activity)

        presenter = ModPresenter(this,
                (application as AdFreeApplication).prefs)

        val textView = findViewById(R.id.modTitle) as TextView
        val text =
                "change how <font color=#FFFFFF>ad-free</font> " +
                        "internally works."
        textView.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(text)
        }

        val factory = LayoutInflater.from(this)
        delayLayout = factory.inflate(R.layout.mod_delay_unmute, null)

        enabledSwitch = findViewById(R.id.enableAdfreeSwitch)

        findViewById<View>(R.id.enableText).setOnClickListener { presenter.onEnableToggleChanged() }
        findViewById<View>(R.id.enableSubtext).setOnClickListener { presenter.onEnableToggleChanged() }
        findViewById<View>(R.id.enabledLayout).setOnClickListener { presenter.onEnableToggleChanged() }
        findViewById<View>(R.id.enableAdfreeSwitch).setOnClickListener { presenter.onEnableToggleChanged() }


        findViewById<View>(R.id.delay_unmute_mod_layout).setOnClickListener { presenter.onDelayUnmute() }
        findViewById<View>(R.id.delay_unmute_mod_title).setOnClickListener { presenter.onDelayUnmute() }
        findViewById<View>(R.id.delay_unmute_mod_subtitle).setOnClickListener { presenter.onDelayUnmute() }

        findViewById<View>(R.id.always_on_layout).setOnClickListener { presenter.onToggleAlwaysOnChanged() }
        findViewById<View>(R.id.always_on_text).setOnClickListener { presenter.onToggleAlwaysOnChanged() }
        findViewById<View>(R.id.always_on_subtext).setOnClickListener { presenter.onToggleAlwaysOnChanged() }
        findViewById<View>(R.id.always_on_switch).setOnClickListener { presenter.onToggleAlwaysOnChanged() }

        findViewById<View>(R.id.active_detectors_layout).setOnClickListener { presenter.onLaunchActiveDetectorsView() }
        findViewById<View>(R.id.active_detectors_title).setOnClickListener { presenter.onLaunchActiveDetectorsView() }
        findViewById<View>(R.id.active_detectors_subtitle).setOnClickListener { presenter.onLaunchActiveDetectorsView() }

        findViewById<TextView>(R.id.mod_status_service).setOnClickListener {
            presenter.onLaunchNotificationListenerSystemSettings()
        }

        val versionView = findViewById<TextView>(R.id.mod_version1)
        versionView.text =
                "> version 1.0 / 1"

        versionView.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/abertschi/ad-free/blob/master/CHANGELOG.md"))
            this.startActivity(browserIntent)
        }


        val alert = AlertDialog.Builder(this)
        alert.setTitle("> delay unmute")
        alert.setView(delayLayout)
        delayDialog = alert.create()
        alwaysOnSwitch = findViewById<SwitchCompat>(R.id.always_on_switch)

        val seek = delayLayout.findViewById(R.id.delay_unmute_seekbar) as SeekBar
        seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (!onCreateActive){
                    presenter.onDelayChanged(progress)
                }

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (!onCreateActive) {
                    presenter.onDelayChanged(seekBar!!.progress)
                }
            }
        })

        presenter.onCreate(this)
        onCreateActive = false;
    }

    fun showDetectorCount(active: Int, total: Int) {
        findViewById<TextView>(R.id.active_detectors_subtitle).text =
                "choose active detectors ( $active/$total )"
    }

    fun showDelayUnmute() {
        delayDialog.show()
        delayDialog.window?.setBackgroundDrawableResource(R.color.colorBackground)
    }

    fun setDelayValue(p: Int) {
        val view = delayLayout.findViewById(R.id.unmutetext2) as TextView
        val text = "${p} seconds"
        view.text = text

        val seek = delayLayout.findViewById(R.id.delay_unmute_seekbar) as SeekBar
        seek.progress = p
        findViewById<TextView>(R.id.delay_unmute_mod_subtitle).text = text
    }

    fun setEnableToggle(b: Boolean) {
        enabledSwitch?.isChecked = b
        findViewById<TextView>(R.id.enableSubtext)?.text = if (b) "enabled" else "disabled"
    }

    fun setGoogleCastToggle(b: Boolean) {
        findViewById<SwitchCompat>(R.id.google_cast_switch).isChecked = b
    }

    fun setNotificationEnabled(b: Boolean) {
        alwaysOnSwitch?.isChecked = b
    }

    fun showPowerEnabled() {
        this.runOnUiThread {
            Toast.makeText(this, "ad-free enabled", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    fun showNotifiationListenerConnected() {
        findViewById<TextView>(R.id.mod_status_service).text = "notification service is connected"

    }

    fun showNotificationListenerDisconnected() {
        findViewById<TextView>(R.id.mod_status_service).text = "notification service is disconnected"
    }

    fun hideDeveloperModeFeatures() {
        val view = findViewById<View>(R.id.google_cast_layout)
        view.visibility = View.GONE
    }
    fun showDeveloperModeFeatures() {
        val view = findViewById<View>(R.id.google_cast_layout)
        view.visibility = View.VISIBLE
        view.setOnClickListener {
            presenter.onGoogleCastToggle()
        }
        findViewById<View>(R.id.google_cast_title).setOnClickListener { presenter.onGoogleCastToggle() }
        findViewById<View>(R.id.google_cast_subtitle).setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://support.google.com/chromecast/answer/7206638?hl=en"))
            this.startActivity(browserIntent)
        }
        findViewById<View>(R.id.google_cast_switch).setOnClickListener { presenter.onGoogleCastToggle() }
    }
}