package android.rezkyauliapratama.com.tokopedia_newsapp.di.viewmodel

import android.arch.lifecycle.ViewModel
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
}