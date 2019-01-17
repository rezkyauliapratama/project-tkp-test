package android.rezkyauliapratama.com.tokopedia_newsapp.ui.main

import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import android.os.Parcelable
import android.rezkyauliapratama.com.tokopedia_newsapp.base.BaseViewModel
import android.rezkyauliapratama.com.tokopedia_newsapp.data.datamodel.Source
import android.rezkyauliapratama.com.tokopedia_newsapp.data.network.ApiRepository
import android.rezkyauliapratama.com.tokopedia_newsapp.data.network.api.SourceApi
import android.rezkyauliapratama.com.tokopedia_newsapp.ui.state.UiStatus
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.error
import java.util.regex.Pattern
import javax.inject.Inject


class MainViewModel @Inject constructor(val apiRepository: ApiRepository) : BaseViewModel(){

    val ARG1 : String = "ARG1"

    val sourceResponseLD: MutableLiveData<SourceApi.SourcesResponse> = MutableLiveData()
    val rvStateLD: MutableLiveData<Parcelable> = MutableLiveData()
    val uiStatusLD: MutableLiveData<UiStatus> = MutableLiveData()

    init {
        error { "init" }
    }

    fun retrieveData() {
        uiStatusLD.value = UiStatus.HIDE_STATUS
        uiStatusLD.value = UiStatus.SHOW_LOADER
        compositeDisposable.add(apiRepository.source
                .getAll().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { response ->
                    for (source : Source in response.sources){
                        val pattern = Pattern.compile("(https?://)([^:^/]*)(:\\d*)?(.*)?")
                        val matcher = pattern.matcher(source.url)

                        matcher.find()

                        val domain = matcher.group(2)
                        source.url = domain
                    }

                    response
                }
                .subscribe({ response ->
                    if (response != null){
                        response
                                .also {
                                    uiStatusLD.value = UiStatus.HIDE_LOADER
                                    sourceResponseLD.value = it
                                }
                    }else{
                        uiStatusLD.value = UiStatus.EMPTY
                    }


                }, { throwable ->
                    uiStatusLD.value = UiStatus.HIDE_LOADER

                    if (throwable.localizedMessage.toLowerCase().contains("connection")){
                        uiStatusLD.value =UiStatus.NO_NETWORK
                    }else{
                        uiStatusLD.value =UiStatus.ERROR_LOAD
                    }
                }))

    }

    fun saveToBundle(outState: Bundle) {
        if (sourceResponseLD.value != null) {
            outState.putParcelable(ARG1, sourceResponseLD.value)
        }
    }

    fun restoreFromBundle(savedInstanceState: Bundle?) {
        error { "restoreFromBUndle" }
        if (savedInstanceState != null){
            error { "savedInstanceState != null" }

            if (sourceResponseLD.value == null) {
                error { "sourceResponseLD.value == null" }

                if(savedInstanceState.containsKey(ARG1) && savedInstanceState.containsKey("liststate")){
                    val sources : SourceApi.SourcesResponse = savedInstanceState.getParcelable(ARG1)
                    error { "sources != null" }

                    sourceResponseLD.value = sources
                    rvStateLD.value = savedInstanceState.getParcelable("liststate")
                }else{
                   uiStatusLD.value = UiStatus.EMPTY
                }
            }
        }else{
            retrieveData()
        }

    }
}