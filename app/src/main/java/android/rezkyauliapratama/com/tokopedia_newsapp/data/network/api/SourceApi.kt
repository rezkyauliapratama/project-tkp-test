package android.rezkyauliapratama.com.tokopedia_newsapp.data.network.api

import android.rezkyauliapratama.com.tokopedia_newsapp.data.datamodel.Source
import android.rezkyauliapratama.com.tokopedia_newsapp.data.network.NewsUrl
import com.google.gson.Gson
import com.rezkyaulia.android.light_optimization_data.NetworkClient
import io.reactivex.Single
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import javax.inject.Inject

@Suppress("UNREACHABLE_CODE")
class SourceApi @Inject constructor(private val networkClient: NetworkClient) : AnkoLogger {

    val TAG : String  = SourceApi::class.java.simpleName

    fun getAll(): Single<SourcesResponse> {
        return Single.create<SourcesResponse> { emitter ->
            try {
                retrieveAllSources()
                        .also { Gson().toJson(it) }
                        .apply { emitter.onSuccess(this) }

            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    private fun retrieveAllSources() : SourcesResponse
    {

        try
        {
            return with(networkClient){
                cancelByTag(TAG)
                withUrl(NewsUrl.getSources())
                        .initAs(SourcesResponse::class.java)
                        .setTag(TAG)
                        .syncFuture
            }
        } catch (e: Exception) {
            throw e
        }

    }


    data class SourcesResponse(
            val status : String,
            val sources : List<Source>
    )
}