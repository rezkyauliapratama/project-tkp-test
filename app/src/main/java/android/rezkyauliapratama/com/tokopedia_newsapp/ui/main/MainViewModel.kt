package android.rezkyauliapratama.com.tokopedia_newsapp.ui.main

import android.arch.lifecycle.MutableLiveData
import android.rezkyauliapratama.com.tokopedia_newsapp.base.BaseViewModel
import android.rezkyauliapratama.com.tokopedia_newsapp.data.network.ApiRepository
import android.rezkyauliapratama.com.tokopedia_newsapp.data.network.api.SourceApi
import android.rezkyauliapratama.com.tokopedia_newsapp.ui.state.UiStatus
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.error
import javax.inject.Inject

class MainViewModel @Inject constructor(val apiRepository: ApiRepository) : BaseViewModel(){

    val sourceResponseLD: MutableLiveData<SourceApi.SourcesResponse> = MutableLiveData()
    val uiStatusLD: MutableLiveData<UiStatus> = MutableLiveData()

    init {
        retrieveData()
    }

    fun retrieveData() {
        uiStatusLD.value = UiStatus.SHOW_LOADER
        compositeDisposable.add(apiRepository.source
                .getAll().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    response
                            .also {
                                uiStatusLD.value = UiStatus.HIDE_LOADER
                                error { Gson().toJson(it) }
                            }
                            ?.let { sourceResponseLD.value = it }
                }, { throwable ->
                    uiStatusLD.value = UiStatus.HIDE_LOADER
                    error { "error : "+ Gson().toJson(throwable) }
                }))

    }
}