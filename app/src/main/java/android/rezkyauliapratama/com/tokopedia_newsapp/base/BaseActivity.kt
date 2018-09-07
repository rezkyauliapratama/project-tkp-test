package android.rezkyauliapratama.com.tokopedia_newsapp.base

import android.arch.lifecycle.ViewModel
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.rezkyauliapratama.com.tokopedia_newsapp.data.network.ApiRepository
import android.rezkyauliapratama.com.tokopedia_newsapp.di.activity.ActivityComponent
import android.rezkyauliapratama.com.tokopedia_newsapp.di.activity.ActivityModule
import android.rezkyauliapratama.com.tokopedia_newsapp.di.activity.DaggerActivityComponent
import android.rezkyauliapratama.com.tokopedia_newsapp.di.viewmodel.ViewModelFactory
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.AnkoLogger
import javax.inject.Inject

abstract class BaseActivity<T : ViewDataBinding, V: ViewModel> :  AppCompatActivity(), AnkoLogger {

    //inisialisasi viewModelFactory , apiRepository
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var apiRepository: ApiRepository

    //inisialisasi general type untuk viewDataBinding dan viewModel
    lateinit var viewDataBinding: T
    lateinit var viewModel : V


    //abstract method untuk di turunkan di dalam subclass
    /**
     * @return layout resource id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract fun initViewModel(): V

    /**
     * @return binding variable resources
     */
    abstract fun initBindingVariable() : Int

    abstract fun inject()

    private var activityComponent: ActivityComponent ?= null

    //function untuk init activity component
    fun initActivityComponent(): ActivityComponent? {
        if (activityComponent == null)
            activityComponent = DaggerActivityComponent.builder()
                    .applicationComponent(BaseApplication.component)
                    .activityModule(ActivityModule(this))
                    .build()

        return activityComponent
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        inject()

        super.onCreate(savedInstanceState)

        performDataBinding()


    }

    //function untuk mengeksekusi data binding viewModel kedalam layout
    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this,getLayoutId())
        this.viewModel =  initViewModel()
        viewDataBinding.setVariable(initBindingVariable(), viewModel)
        viewDataBinding.executePendingBindings()

    }

}