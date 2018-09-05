package android.rezkyauliapratama.com.tokopedia_newsapp.data.network

import android.net.Uri
import android.rezkyauliapratama.com.tokopedia_newsapp.BuildConfig
import org.jetbrains.anko.AnkoLogger

object NewsUrl : AnkoLogger {

    fun getSources(): String{
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
                .appendPath("v2")
                .appendPath("sources")
                .appendQueryParameter("apiKey",BuildConfig.API_KEY)
                .build()
                .toString()
    }

}