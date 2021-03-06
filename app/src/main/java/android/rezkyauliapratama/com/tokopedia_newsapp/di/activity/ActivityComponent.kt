package android.rezkyauliapratama.com.tokopedia_newsapp.di.activity

import android.rezkyauliapratama.com.tokopedia_newsapp.di.application.ApplicationComponent
import android.rezkyauliapratama.com.tokopedia_newsapp.ui.article.ArticleActivity
import android.rezkyauliapratama.com.tokopedia_newsapp.ui.detail.DetailActivity
import android.rezkyauliapratama.com.tokopedia_newsapp.ui.main.MainActivity
import dagger.Component

@PerActivity
@Component(dependencies = [ApplicationComponent::class], modules = [ActivityModule::class])
interface ActivityComponent{
    fun inject(mainActivity: MainActivity)
    fun inject(mainActivity: ArticleActivity)
    fun inject(detailActivity: DetailActivity)

}