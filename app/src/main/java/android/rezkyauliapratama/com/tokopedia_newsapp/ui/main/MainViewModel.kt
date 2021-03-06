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
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.error
import java.util.regex.Pattern
import javax.inject.Inject
import android.provider.ContactsContract.CommonDataKinds.Note
import io.reactivex.internal.util.NotificationLite.disposable



class MainViewModel @Inject constructor(val apiRepository: ApiRepository) : BaseViewModel(){

    val ARG1 : String = "ARG1"

    val sourceResponseLD: MutableLiveData<SourceApi.SourcesResponse> = MutableLiveData()
    val rvStateLD: MutableLiveData<Parcelable> = MutableLiveData()
    val uiStatusLD: MutableLiveData<UiStatus> = MutableLiveData()


    fun retrieveData() {


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

    fun saveToBundle(outState: Bundle) {
        if (sourceResponseLD.value != null) {
            error { "put parcelable" }
            outState.putParcelable(ARG1, sourceResponseLD.value)
        }
    }

    fun restoreFromBundle(savedInstanceState: Bundle?) {
        error { "restore from bundle" }
        if (savedInstanceState != null){
            if (sourceResponseLD.value == null) {
                error { "sourceResponseLD.value == null" }
                if(savedInstanceState.containsKey(ARG1) && savedInstanceState.containsKey("liststate")){
                    val sources : SourceApi.SourcesResponse = savedInstanceState.getParcelable(ARG1)
                    error { "restore : "+ Gson().toJson(sources) }
                    sourceResponseLD.value = sources
                    rvStateLD.value = savedInstanceState.getParcelable("liststate")
                }
            }
        }else{
            retrieveData()
        }

    }
}