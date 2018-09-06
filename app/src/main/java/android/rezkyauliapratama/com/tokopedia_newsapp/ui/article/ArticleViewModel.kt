package android.rezkyauliapratama.com.tokopedia_newsapp.ui.article

import android.arch.lifecycle.MutableLiveData
import android.rezkyauliapratama.com.tokopedia_newsapp.base.BaseViewModel
import android.rezkyauliapratama.com.tokopedia_newsapp.data.datamodel.Article
import android.rezkyauliapratama.com.tokopedia_newsapp.data.network.ApiRepository
import android.rezkyauliapratama.com.tokopedia_newsapp.data.network.api.ArticleApi
import android.rezkyauliapratama.com.tokopedia_newsapp.ui.state.UiStatus
import android.rezkyauliapratama.com.tokopedia_newsapp.util.TimeUtility
import android.text.Editable
import com.google.gson.Gson
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.jetbrains.anko.error
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ArticleViewModel @Inject constructor(val apiRepository: ApiRepository, val timeUtility: TimeUtility) : BaseViewModel(){
    val articleResponseLD: MutableLiveData<List<Article>> = MutableLiveData()
    val uiStatusLD: MutableLiveData<UiStatus> = MutableLiveData()

    val subject = PublishSubject.create<String>()
    lateinit var source: String


    init {
        compositeDisposable.add(
                subject.
                        debounce(300, TimeUnit.MILLISECONDS)
                        .filter(Predicate { it: String ->
                            return@Predicate it.isNotEmpty()
                        })
                        .distinctUntilChanged()
                        .switchMap(Function<String, ObservableSource<ArticleApi.ArticleResponse>> { it ->
                            return@Function apiRepository.article.getAllArticles(source,it)

                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ response ->
                            articleResponseLD.value = response.articles
                                    .also {
                                        uiStatusLD.value = UiStatus.HIDE_LOADER
                                    }
                        }, { throwable ->
                            uiStatusLD.value = UiStatus.HIDE_LOADER
                            error { "error : "+ Gson().toJson(throwable) }
                        })

        )
    }
    fun retrieveData(source : String) {

        this.source = source

        uiStatusLD.value = UiStatus.SHOW_LOADER
        compositeDisposable.add(apiRepository.article
                .getAllArticles(source).subscribeOn(Schedulers.io())
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
                    error { "error retrieve : "+ Gson().toJson(throwable) }
                }))

    }


    fun search(s: String) {
        if (s.isEmpty()){
            retrieveData(source)
        }else{
            uiStatusLD.value = UiStatus.SHOW_LOADER
            subject.onNext(s)
        }

    }


}