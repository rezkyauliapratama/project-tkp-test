package android.rezkyauliapratama.com.tokopedia_newsapp.ui.main

import android.arch.lifecycle.MutableLiveData
import android.rezkyauliapratama.com.tokopedia_newsapp.base.BaseViewModel
import android.rezkyauliapratama.com.tokopedia_newsapp.data.network.ApiRepository
import android.rezkyauliapratama.com.tokopedia_newsapp.data.network.api.SourceApi
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.error
import javax.inject.Inject

class MainViewModel @Inject constructor(val apiRepository: ApiRepository) : BaseViewModel(){

    val sourceResponseLD: MutableLiveData<SourceApi.SourcesResponse> = MutableLiveData()

    init {
        retrieveData()
    }

    fun retrieveData() {
        compositeDisposable.add(apiRepository.source
                .getAll().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    error { Gson().toJson(response) }
                    if (response != null){
                        sourceResponseLD.value = response
                    }


                }, { throwable ->
                    error { "error : "+ Gson().toJson(throwable) }

                }))

    }
}