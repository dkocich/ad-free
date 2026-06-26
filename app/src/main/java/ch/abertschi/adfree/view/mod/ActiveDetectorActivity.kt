package ch.abertschi.adfree.view.mod

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.abertschi.adfree.R
import ch.abertschi.adfree.detector.AdDetectable
import java.lang.IllegalStateException


class ActiveDetectorActivity : AppCompatActivity() {

    private lateinit var detectorRecyclerView: RecyclerView
    private lateinit var detectorViewAdapter: RecyclerView.Adapter<*>
    private lateinit var detectorViewManager: RecyclerView.LayoutManager

    private lateinit var presenter: ActiveDetectorPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mod_active_detectors)

        val textView = findViewById<TextView>(R.id.detectors_activity_title)

        presenter = ActiveDetectorPresenter(this)

        val category: String = intent.extras?.getString(CategoriesPresenter.BUNDLE_CATEGORY_KEY)
            ?: throw  IllegalStateException("must set category")

        val text =
            "fine-tune detectors for <font color=#FFFFFF>$category</font>."
        textView.text = Html.fromHtml(text)


        findViewById<ScrollView>(R.id.mod_active_scroll).scrollTo(0, 0)

        detectorViewManager = LinearLayoutManager(this)
        detectorViewAdapter = DetectorAdapter(presenter.getDetectors(category), presenter)
        detectorRecyclerView = findViewById<RecyclerView>(R.id.detector_recycle_view).apply {
            layoutManager = detectorViewManager
            adapter = detectorViewAdapter

        }
    }

    fun showInfo(info: String) {
        Toast.makeText(this, info, Toast.LENGTH_LONG).show()
    }
}

class DetectorAdapter(
    private val detectors: List<AdDetectable>,
    private val presenter: ActiveDetectorPresenter
) :
    RecyclerView.Adapter<DetectorAdapter.MyViewHolder>() {

    class MyViewHolder(
        val view: View,
        val title: TextView,
        val subtitle: TextView,
        val switch: SwitchCompat,
        val sepView: View
    ) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.mod_active_detectors_view_element, parent, false)
        val title = view.findViewById(R.id.det_title) as TextView
        val subtitle = view.findViewById(R.id.det_subtitle) as TextView
        val switch = view.findViewById<TextView>(R.id.det_switch) as SwitchCompat
        val sep = view.findViewById<View>(R.id.mod_det_seperation)
        return MyViewHolder(view, title, subtitle, switch, sep)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = "> " + detectors[position].getMeta().title
        holder.subtitle.text = detectors[position].getMeta().description
        holder.switch.isChecked = presenter.isEnabled(detectors[position])

        holder.title.setOnClickListener {
            holder.switch.toggle()
        }
        holder.subtitle.setOnClickListener {
            holder.switch.toggle()
        }

        holder.view.setOnClickListener {
            holder.switch.toggle()
        }

        holder.switch.setOnCheckedChangeListener { _, isChecked ->
            presenter.onDetectorToggled(isChecked, detectors[position])
        }
        holder.sepView.visibility =
            if (position == detectors.size - 1) View.INVISIBLE else View.VISIBLE
    }

    override fun getItemCount() = detectors.size
}
