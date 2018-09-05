package android.rezkyauliapratama.com.tokopedia_newsapp.data.network

import android.rezkyauliapratama.com.tokopedia_newsapp.data.network.api.SourceApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRepository @Inject constructor(){

    @Inject
    lateinit var source: SourceApi


}