/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.model

import android.util.Log
import ch.abertschi.adfree.view.ViewSettings
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by abertschi on 28.04.17.
 */

class RemoteManager(prefFactory: PreferencesFactory) {

    private val TAG: String = "RemoteManager"

    private var URL: String = ViewSettings.AD_FREE_RESOURCE_ADRESS +
            "settings.yaml" + ViewSettings.GITHUB_RAW_SUFFIX

    var remoteSettings: RemoteSetting? = null
    var configFactory: YamlRemoteConfigFactory<RemoteSetting> =
            YamlRemoteConfigFactory(URL, RemoteSetting::class.java, prefFactory.getPreferences())

    fun getRemoteSettingsObservable(): Observable<RemoteSetting> {
        Log.i(TAG, "feting settings getRemoteSettingsObservable")
        remoteSettings = configFactory.loadFromLocalStore()
        return Observable.create<RemoteSetting> { source ->
            configFactory.downloadObservable()
                    .map { it.first }
                    .doOnNext { remoteSettings = it }
                    .doOnNext { configFactory.storeToLocalStore(it) }
                    .subscribe({ _-> source.onNext(remoteSettings!!) },
                            { err ->
                                Log.i(TAG, err.toString())
                                //source.onError(err)
//                                source.onNext(remoteSettings)
                            })
        }
                .observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}