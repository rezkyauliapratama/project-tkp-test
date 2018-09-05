package android.rezkyauliapratama.com.tokopedia_newsapp.di.application

import android.content.Context
import com.rezkyaulia.android.light_optimization_data.NetworkClient
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule{

    @Singleton
    @Provides
    internal fun provideHttpClient(@ApplicationContext context: Context): NetworkClient {
        return NetworkClient(context)
    }

}