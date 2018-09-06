package android.rezkyauliapratama.com.tokopedia_newsapp.ui.article

import android.arch.lifecycle.MutableLiveData
import android.rezkyauliapratama.com.tokopedia_newsapp.base.BaseViewModel
import android.rezkyauliapratama.com.tokopedia_newsapp.data.datamodel.Article
import android.rezkyauliapratama.com.tokopedia_newsapp.data.network.ApiRepository
import android.rezkyauliapratama.com.tokopedia_newsapp.data.network.api.ArticleApi
import android.rezkyauliapratama.com.tokopedia_newsapp.ui.state.UiStatus
import android.rezkyauliapratama.com.tokopedia_newsapp.util.TimeUtility
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.error
import java.util.*
import javax.inject.Inject

class ArticleViewModel @Inject constructor(val apiRepository: ApiRepository, val timeUtility: TimeUtility) : BaseViewModel(){
    val articleResponseLD: MutableLiveData<List<Article>> = MutableLiveData()
    val uiStatusLD: MutableLiveData<UiStatus> = MutableLiveData()

    var index = 1
    lateinit var startDate:Date
    lateinit var endDate: Date
    fun retrieveData(source : String) {
        val calendar = Calendar.getInstance()

        calendar.let {
            if (index > 1){
                calendar.add(Calendar.YEAR,-index)
            }
            index++
            endDate = calendar.time
        }.run{
            calendar.add(Calendar.YEAR,-1)
            startDate = calendar.time
        }
        uiStatusLD.value = UiStatus.SHOW_LOADER
        compositeDisposable.add(apiRepository.article
                .getAllArticles(source, startDate,endDate).subscribeOn(Schedulers.io())
                .map { it ->
                    for (article: Article in it.articles){
                        timeUtility.run { convertStringToDate(article.publishedAt).also { article.publishedAt = getFriendlyDate(it) } }
                    }
                    it
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    articleResponseLD.value = response.articles
                            .also {
                                uiStatusLD.value = UiStatus.HIDE_LOADER
                            }
                }, { throwable ->
                    uiStatusLD.value = UiStatus.HIDE_LOADER
                    error { "error : "+ Gson().toJson(throwable) }
                }))

    }


}