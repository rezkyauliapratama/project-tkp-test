package android.rezkyauliapratama.com.tokopedia_newsapp.di.activity

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(val activity: Activity){

    @Provides
    @ActivityContext
    fun providesContext(): Context = activity

    @Provides
    fun provideActivity(): Activity = activity
}