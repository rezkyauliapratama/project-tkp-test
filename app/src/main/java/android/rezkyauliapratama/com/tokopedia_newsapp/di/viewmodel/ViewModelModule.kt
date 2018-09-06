package android.rezkyauliapratama.com.tokopedia_newsapp.di.viewmodel

import android.arch.lifecycle.ViewModel
import android.rezkyauliapratama.com.tokopedia_newsapp.ui.article.ArticleViewModel
import android.rezkyauliapratama.com.tokopedia_newsapp.ui.detail.DetailViewModel
import android.rezkyauliapratama.com.tokopedia_newsapp.ui.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule{

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel : MainViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ArticleViewModel::class)
    abstract fun bindArticleViewModel(articleViewModel: ArticleViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    abstract fun bindDetailViewModel(detailViewModel: DetailViewModel) : ViewModel
}