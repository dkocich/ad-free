package ch.abertschi.adfree.util

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast

/**
 * Replacement functions for deprecated Anko library
 */

// Click listener extension
inline fun View.onClick(crossinline action: (view: View) -> Unit) {
    setOnClickListener { action(it) }
}

// Toast extensions
fun Activity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.toast(message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.longToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Activity.longToast(message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

// UI thread extension
fun Activity.runOnUiThread(action: () -> Unit) {
    if (Thread.currentThread() == mainLooper.thread) {
        action()
    } else {
        runOnUiThread(action)
    }
}

// Collections extension for forEachWithIndex replacement
inline fun <T> Array<T>.forEachWithIndex(action: (index: Int, T) -> Unit) {
    for (index in indices) {
        action(index, this[index])
    }
}

inline fun <T> List<T>.forEachWithIndex(action: (index: Int, T) -> Unit) {
    for (index in indices) {
        action(index, this[index])
    }
}

// Spinner item selected listener extension
inline fun Spinner.onItemSelectedListener(crossinline init: _OnItemSelectedListener.() -> Unit) {
    val listener = _OnItemSelectedListener()
    listener.init()
    onItemSelectedListener = listener
}

class _OnItemSelectedListener : AdapterView.OnItemSelectedListener {
    private var _onItemSelected: ((parent: AdapterView<*>?, view: View?, position: Int, id: Long) -> Unit)? =
        null
    private var _onNothingSelected: ((parent: AdapterView<*>?) -> Unit)? = null

    fun onItemSelected(listener: (parent: AdapterView<*>?, view: View?, position: Int, id: Long) -> Unit) {
        _onItemSelected = listener
    }

    fun onNothingSelected(listener: (parent: AdapterView<*>?) -> Unit) {
        _onNothingSelected = listener
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        _onItemSelected?.invoke(parent, view, position, id)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        _onNothingSelected?.invoke(parent)
    }
}

// Logger interface replacement for AnkoLogger
interface AnkoLogger {
    val loggerTag: String get() = javaClass.simpleName

    fun info(message: String) = Log.i(loggerTag, message)
    fun debug(message: String) = Log.d(loggerTag, message)
    fun warn(message: String) = Log.w(loggerTag, message)
    fun error(message: String) = Log.e(loggerTag, message)
    fun verbose(message: String) = Log.v(loggerTag, message)
}
