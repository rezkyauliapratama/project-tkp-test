package android.rezkyauliapratama.com.tokopedia_newsapp.base

import android.app.Application
import android.rezkyauliapratama.com.tokopedia_newsapp.di.application.ApplicationComponent
import android.rezkyauliapratama.com.tokopedia_newsapp.di.application.ApplicationModule
import android.rezkyauliapratama.com.tokopedia_newsapp.di.application.DaggerApplicationComponent
import android.rezkyauliapratama.com.tokopedia_newsapp.di.application.NetworkModule

class BaseApplication : Application() {

    companion object {
        lateinit var component : ApplicationComponent
    }
    override fun onCreate() {
        super.onCreate()
        component = initDagger(this)
        component.inject(this)
    }

    private fun initDagger(app: BaseApplication): ApplicationComponent =
            DaggerApplicationComponent.builder()
                    .applicationModule(ApplicationModule(app))
                    .networkModule(NetworkModule())
                    .build()

}
