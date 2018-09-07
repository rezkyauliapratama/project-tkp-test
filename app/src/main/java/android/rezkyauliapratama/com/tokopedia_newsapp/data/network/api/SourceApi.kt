package android.rezkyauliapratama.com.tokopedia_newsapp.data.network.api

import android.os.Parcelable
import android.rezkyauliapratama.com.tokopedia_newsapp.data.datamodel.Source
import android.rezkyauliapratama.com.tokopedia_newsapp.data.network.NewsUrl
import com.google.gson.Gson
import com.rezkyaulia.android.light_optimization_data.NetworkClient
import io.reactivex.Single
import kotlinx.android.parcel.Parcelize
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import javax.inject.Inject

@Suppress("UNREACHABLE_CODE")
class SourceApi @Inject constructor(private val networkClient: NetworkClient) : AnkoLogger {

    val TAG : String  = SourceApi::class.java.simpleName

    //public function yang digunakan oleh aplikasi untuk mengekseksu method api retrieveAllSources
    fun getAll(): Single<SourcesResponse> {
        return Single.create<SourcesResponse> { emitter ->
            try {
                retrieveAllSources()
                        .apply { emitter.onSuccess(this) }

            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    //function untuk mengambil data source dari api
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


    @Parcelize
    data class SourcesResponse (
            var status : String,
            var sources : List<Source>
    ) : Parcelable
}