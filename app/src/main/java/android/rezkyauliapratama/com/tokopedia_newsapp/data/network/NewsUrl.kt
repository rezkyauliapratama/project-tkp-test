package android.rezkyauliapratama.com.tokopedia_newsapp.data.network

import android.net.Uri
import android.rezkyauliapratama.com.tokopedia_newsapp.BuildConfig
import android.rezkyauliapratama.com.tokopedia_newsapp.util.TimeUtility
import org.jetbrains.anko.AnkoLogger
import java.util.*
import javax.inject.Inject

//class yang berisi url untuk API
object NewsUrl : AnkoLogger {


    fun getSources(): String{
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
                .appendPath("v2")
                .appendPath("sources")
                .appendQueryParameter("apiKey",BuildConfig.API_KEY)
                .build()
                .toString()
    }

    fun getArticles(source : String): String{
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
                .appendPath("v2")
                .appendPath("everything")
                .appendQueryParameter("domains",source)
                .appendQueryParameter("apiKey",BuildConfig.API_KEY)
                .build()
                .toString()
    }

    fun getArticles(source : String, q : String): String{
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
                .appendPath("v2")
                .appendPath("everything")
                .appendQueryParameter("domains",source)
                .appendQueryParameter("q",q)
                .appendQueryParameter("apiKey",BuildConfig.API_KEY)
                .build()
                .toString()
    }

}