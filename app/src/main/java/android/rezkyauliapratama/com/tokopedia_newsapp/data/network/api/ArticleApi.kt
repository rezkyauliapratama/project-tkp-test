package android.rezkyauliapratama.com.tokopedia_newsapp.data.network.api

import android.rezkyauliapratama.com.tokopedia_newsapp.data.datamodel.Article
import android.rezkyauliapratama.com.tokopedia_newsapp.data.network.NewsUrl
import android.rezkyauliapratama.com.tokopedia_newsapp.util.TimeUtility
import com.google.gson.Gson
import com.rezkyaulia.android.light_optimization_data.NetworkClient
import io.reactivex.ObservableSource
import io.reactivex.Single
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import java.util.*
import javax.inject.Inject

class ArticleApi @Inject constructor(private val networkClient: NetworkClient) : AnkoLogger{
    val TAG : String  = ArticleApi::class.java.simpleName


    //public function yang digunakan aplikasi untuk mengambil data articles dari API dengan cara menjalankan getAllArticles
    fun getAllArticles(source: String) : Single<ArticleResponse>{
        return Single.create<ArticleResponse> { emitter ->
            try {
                retrieveAllArticles(source)
                        .also { Gson().toJson(it) }
                        .apply { emitter.onSuccess(this) }

            } catch (e: Exception) {
                emitter.onError(e)
            }
        }

    }

    //public function yang digunakan aplikasi untuk mengambil data articles dari API dengan cara menjalankan getAllArticles
    fun getAllArticles(source: String, q : String) : ObservableSource<ArticleResponse>{
        return ObservableSource {
            emitter ->
            try {
                retrieveAllArticles(source,q)
                        .also { Gson().toJson(it) }
                        .apply { emitter.onNext(this) }

            } catch (e: Exception) {
                emitter.onError(e)
            }
        }

    }

    //function untuk mengambil data article dari api
    private fun retrieveAllArticles(source : String) : ArticleResponse
    {
        try
        {
            return with(networkClient){
                cancelByTag(TAG)
                withUrl(NewsUrl.getArticles(source))
                        .initAs(ArticleResponse::class.java)
                        .setTag(TAG)
                        .syncFuture
            }
        } catch (e: Exception) {
            throw e
        }

    }

    //function untuk mengambil data article dari api
    private fun retrieveAllArticles(source : String, q : String) : ArticleResponse
    {
        try
        {
            return with(networkClient){
                cancelByTag(TAG+"query")
                withUrl(NewsUrl.getArticles(source,q))
                        .initAs(ArticleResponse::class.java)
                        .setTag(TAG+"query")
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