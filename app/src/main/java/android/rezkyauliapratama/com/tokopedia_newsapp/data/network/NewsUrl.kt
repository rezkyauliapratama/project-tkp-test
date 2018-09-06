package android.rezkyauliapratama.com.tokopedia_newsapp.data.network

import android.net.Uri
import android.rezkyauliapratama.com.tokopedia_newsapp.BuildConfig
import android.rezkyauliapratama.com.tokopedia_newsapp.util.TimeUtility
import org.jetbrains.anko.AnkoLogger
import java.util.*
import javax.inject.Inject

object NewsUrl : AnkoLogger {


    fun getSources(): String{
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
                .appendPath("v2")
                .appendPath("sources")
                .appendQueryParameter("apiKey",BuildConfig.API_KEY)
                .build()
                .toString()
    }

    fun getArticles(source:String, start: String, end: String): String{
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
                .appendPath("v2")
                .appendPath("everything")
                .appendQueryParameter("domains",source)
                /*.appendQueryParameter("from",start)
                .appendQueryParameter("to",end)*/
                .appendQueryParameter("apiKey",BuildConfig.API_KEY)
                .build()
                .toString()
    }

}