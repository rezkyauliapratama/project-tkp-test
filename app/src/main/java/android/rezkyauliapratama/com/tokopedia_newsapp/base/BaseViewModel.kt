package android.rezkyauliapratama.com.tokopedia_newsapp.base

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

abstract class BaseViewModel : ViewModel(), AnkoLogger {
    var compositeDisposable: CompositeDisposable

    init {
        error {"init"}
        compositeDisposable = CompositeDisposable()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}