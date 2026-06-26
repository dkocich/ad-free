package ch.abertschi.adfree.model

import android.content.SharedPreferences
import com.github.kittinunf.fuel.Fuel
import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.security.AnyTypePermission
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.yaml.snakeyaml.Yaml

class YamlRemoteConfigFactory<T> constructor(val url: String,
                                              val type: Class<T>,
                                              val preferences: SharedPreferences) {

    private val KEY_CACHED_CONFIG = "cached_config"
    private var _localStore: T? = null

    // XStream 1.4.18+ blocks deserialization unless types are allowlisted.
    // This cache is data the app itself wrote, so allow all types.
    private fun xstream(): XStream {
        val x = XStream()
        x.addPermission(AnyTypePermission.ANY)
        return x
    }

    fun downloadObservable(): Observable<Pair<T, String>> {
        return Observable.create<Pair<T, String>> { source ->
            Fuel.get(url).responseString { _, _, result ->
                val (data, error) = result
                if (error == null) {
                    val config = Yaml().loadAs(data, type)
                    val pair = Pair(config, data!!)
                    source.onNext(pair)
                } else {
                    source.onError(error.exception)
                }
            }
        }.subscribeOn(Schedulers.io())
    }

    fun loadFromLocalStore(): T? {
        if (_localStore != null) {
            return _localStore
        }
        val data = preferences.getString(KEY_CACHED_CONFIG, "")
        if (data != "") {
            _localStore = xstream().fromXML(data) as T
            return _localStore
        } else {
            return null
        }
    }

    fun storeToLocalStore(config: T): T {
        val data = xstream().toXML(config)
        preferences.edit().putString(KEY_CACHED_CONFIG, data).commit()
        return config
    }
}