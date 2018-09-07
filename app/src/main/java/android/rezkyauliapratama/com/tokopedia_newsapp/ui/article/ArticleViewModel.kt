package android.rezkyauliapratama.com.tokopedia_newsapp.ui.article

import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import android.os.Parcelable
import android.rezkyauliapratama.com.tokopedia_newsapp.base.BaseViewModel
import android.rezkyauliapratama.com.tokopedia_newsapp.data.datamodel.Article
import android.rezkyauliapratama.com.tokopedia_newsapp.data.network.ApiRepository
import android.rezkyauliapratama.com.tokopedia_newsapp.data.network.api.ArticleApi
import android.rezkyauliapratama.com.tokopedia_newsapp.data.network.api.SourceApi
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
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ArticleViewModel @Inject constructor(val apiRepository: ApiRepository, val timeUtility: TimeUtility) : BaseViewModel(){
    val articleResponseLD: MutableLiveData<List<Article>> = MutableLiveData()
    val queryLD: MutableLiveData<String> = MutableLiveData()
    val rvStateLD: MutableLiveData<Parcelable> = MutableLiveData()
    val uiStatusLD: MutableLiveData<UiStatus> = MutableLiveData()

    val subject = PublishSubject.create<String>()

    lateinit var sourceStr: String
    var query = ""

    val ARG1 : String = "ARG1"
    val ARG2 : String = "ARG2"

    init {
        compositeDisposable.add(
                subject.
                        debounce(300, TimeUnit.MILLISECONDS)
                        .filter(Predicate { it: String ->
                            return@Predicate it.isNotEmpty()
                        })
                        .distinctUntilChanged()
                        .switchMap(Function<String, ObservableSource<ArticleApi.ArticleResponse>> { it ->
                            return@Function apiRepository.article.getAllArticles(sourceStr,it)

                        })
                        .map { it ->
                            for (article: Article in it.articles){
                                timeUtility.run { convertStringToDate(article.publishedAt).also { article.publishedAt = getFriendlyDate(it) } }
                            }
                            it
                        }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ response ->
                            if (response != null){
                                articleResponseLD.value = response.articles
                                        .also {
                                            uiStatusLD.value = UiStatus.HIDE_LOADER
                                        }
                            }else{
                                uiStatusLD.value = UiStatus.EMPTY
                            }

                        }, { throwable ->
                            uiStatusLD.value = UiStatus.HIDE_LOADER

                            if (throwable.localizedMessage.toLowerCase().contains("connection")){
                                uiStatusLD.value =UiStatus.NO_NETWORK
                            }else{
                                uiStatusLD.value =UiStatus.ERROR_LOAD
                            }
                            error { "error : "+ Gson().toJson(throwable) }
                        })

        )
    }
    fun retrieveData(source : String) {

        this.sourceStr = source

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
                    if (response != null){
                        articleResponseLD.value = response.articles
                                .also {
                                    uiStatusLD.value = UiStatus.HIDE_STATUS

                                    uiStatusLD.value = UiStatus.HIDE_LOADER
                                }
                    }else{
                        uiStatusLD.value = UiStatus.EMPTY
                    }

                }, { throwable ->
                    uiStatusLD.value = UiStatus.HIDE_LOADER

                    if (throwable.localizedMessage.toLowerCase().contains("connection")){
                        uiStatusLD.value =UiStatus.NO_NETWORK
                    }else{
                        uiStatusLD.value =UiStatus.ERROR_LOAD
                    }
                    error { "error retrieve : "+ Gson().toJson(throwable) }
                }))

    }


    fun search(s: String) {
        query = s
        if (s.isEmpty()){
            retrieveData(sourceStr)
        }else{
            uiStatusLD.value = UiStatus.SHOW_LOADER
            subject.onNext(s)
        }

    }

    fun saveToBundle(outState: Bundle) {
        if (articleResponseLD.value != null) {
            error { "put parcelable" }

            outState.putString(ARG1,  query)
        }
    }

    fun restoreFromBundle(savedInstanceState: Bundle?, source : String) {

        error { "restore from bundle" }
        this.sourceStr = source
        if (savedInstanceState != null){
            if (articleResponseLD.value == null) {
                error { "articleResponseLD.value == null" }
                if(savedInstanceState.containsKey(ARG1) && savedInstanceState.containsKey("liststate")){
                    query = savedInstanceState.getString(ARG1)
                    queryLD.value = query
                    rvStateLD.value = savedInstanceState.getParcelable("liststate")
                }
            }
        }else{
            retrieveData(source)
        }

    }

}