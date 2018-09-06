package android.rezkyauliapratama.com.tokopedia_newsapp.data.network.api

import android.rezkyauliapratama.com.tokopedia_newsapp.data.datamodel.Article
import android.rezkyauliapratama.com.tokopedia_newsapp.data.network.NewsUrl
import android.rezkyauliapratama.com.tokopedia_newsapp.util.TimeUtility
import com.google.gson.Gson
import com.rezkyaulia.android.light_optimization_data.NetworkClient
import io.reactivex.Single
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import java.util.*
import javax.inject.Inject

class ArticleApi @Inject constructor(private val networkClient: NetworkClient, private val timeUtility: TimeUtility) : AnkoLogger{
    val TAG : String  = ArticleApi::class.java.simpleName


    fun getAllArticles(source: String, date: Date) : Single<ArticleResponse>{
        return Single.create<ArticleResponse> { emitter ->
            try {
                retrieveAllArticles(source,date)
                        .also { Gson().toJson(it) }
                        .apply { emitter.onSuccess(this) }

            } catch (e: Exception) {
                emitter.onError(e)
            }
        }

    }

    private fun retrieveAllArticles(source : String, date: Date) : ArticleResponse
    {
        try
        {
            error { NewsUrl.getArticles(source, timeUtility.convertDateToString(date)) }
            return with(networkClient){
                cancelByTag(TAG)
                withUrl(NewsUrl.getArticles(source, timeUtility.convertDateToString(date)))
                        .initAs(ArticleResponse::class.java)
                        .setTag(TAG)
                        .syncFuture
            }
        } catch (e: Exception) {
            throw e
        }

    }



    data class ArticleResponse(
            val status : String,
            val totalResults : String,
            val articles : List<Article>
    )

}