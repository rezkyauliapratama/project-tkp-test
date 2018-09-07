package android.rezkyauliapratama.com.tokopedia_newsapp.base

import android.app.Application
import android.rezkyauliapratama.com.tokopedia_newsapp.di.application.ApplicationComponent
import android.rezkyauliapratama.com.tokopedia_newsapp.di.application.ApplicationModule
import android.rezkyauliapratama.com.tokopedia_newsapp.di.application.DaggerApplicationComponent
import android.rezkyauliapratama.com.tokopedia_newsapp.di.application.NetworkModule
import com.app.infideap.stylishwidget.view.Stylish

class BaseApplication : Application() {

    companion object {
        lateinit var component : ApplicationComponent
    }
    override fun onCreate() {
        super.onCreate()

        val fontFolder = "fonts/Exo_2/Exo2-"
        Stylish.getInstance().set(
                fontFolder + "Regular.ttf",
                fontFolder + "Medium.ttf",
                fontFolder + "Italic.ttf",
                fontFolder + "MediumItalic.ttf"
        )

        component = initDagger(this)
        component.inject(this)
    }

    private fun initDagger(app: BaseApplication): ApplicationComponent =
            DaggerApplicationComponent.builder()
                    .applicationModule(ApplicationModule(app))
                    .networkModule(NetworkModule())
                    .build()

}
