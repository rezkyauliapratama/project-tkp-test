package android.rezkyauliapratama.com.tokopedia_newsapp.di.application

import android.rezkyauliapratama.com.tokopedia_newsapp.base.BaseApplication
import android.rezkyauliapratama.com.tokopedia_newsapp.data.network.ApiRepository
import android.rezkyauliapratama.com.tokopedia_newsapp.di.viewmodel.ViewModelFactory
import android.rezkyauliapratama.com.tokopedia_newsapp.di.viewmodel.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, NetworkModule::class, ViewModelModule::class])
interface ApplicationComponent {
    fun inject(baseApplication: BaseApplication)

    fun getApiRepository() : ApiRepository
    fun getViewModelFactory() : ViewModelFactory


}