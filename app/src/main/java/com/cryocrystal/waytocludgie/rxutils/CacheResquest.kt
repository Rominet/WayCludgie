package com.cryocrystal.waytocludgie.rxutils

import android.content.Context
import com.cryocrystal.mvp.rxutils.onIO
import com.cryocrystal.waytocludgie.contractbehaviour.DataLoading
import com.cryocrystal.waytocludgie.statics.Config
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy

class CacheRequest<T : Any>(private val onCache: () -> T?, private val onWeb: () -> Observable<T>, private val onGetNextUpdate: () -> Long, private val onSetNextUpdate: () -> Unit) {
    private var noData = false
    private var webSubscription: Disposable? = null

    val observable: Observable<RequestedResponse<T>>
        get() {
            return Observable.create<RequestedResponse<T>> { subscriber ->

                val response = onCache()

                subscriber.onNext(RequestedResponse.Cache<T>(response))
                noData = response == null

                if (!noData && isUpToDate()) {
                    subscriber.onComplete()
                    return@create
                }

                webSubscription = onWeb()
                        .doOnTerminate { subscriber.onComplete() }
                        .subscribeBy(
                                onNext = {
                                    subscriber.onNext(RequestedResponse.Remote(it))
                                    onSetNextUpdate()
                                },
                                onError = {
                                    if (noData) {
                                        subscriber.onError(it)
                                    }
                                }
                        )
            }
                    .doOnDispose {
                        webSubscription?.dispose()
                        webSubscription = null
                    }
                    .onIO()
        }

    fun isUpToDate(): Boolean {
        return System.currentTimeMillis() < onGetNextUpdate()
    }

    companion object {
        fun <T : Any> createFromConfig(onCache: () -> T?, onWeb: () -> Observable<T>, context: Context): CacheRequest<T> {
            return CacheRequest(onCache = onCache,
                    onWeb = onWeb,
                    onGetNextUpdate = { context.getSharedPreferences(Config.SHARED_PREF, Context.MODE_PRIVATE).getLong(Config.KEY_NEXT_UPDATE, 0) },
                    onSetNextUpdate = {
                        val prefs = context.getSharedPreferences(Config.SHARED_PREF, Context.MODE_PRIVATE)
                        prefs.edit().putLong(Config.KEY_NEXT_UPDATE, System.currentTimeMillis() + Config.CACHE_DURATION).apply()
                    }
            )
        }
    }
}

sealed class RequestedResponse<T> {
    class Cache<T>(val apiResponse: T?) : RequestedResponse<T>()
    class Remote<T>(val apiResponse: T) : RequestedResponse<T>()
}

fun <T> RequestedResponse<T>.treatSuccess(contract: DataLoading<T>) {
    when (this) {
        is RequestedResponse.Cache -> {
            if (apiResponse == null) {
                contract.onDisplayLoader()
            } else {
                contract.onSuccess(apiResponse)
            }
        }
        is RequestedResponse.Remote -> contract.onSuccess(apiResponse)
    }
}

