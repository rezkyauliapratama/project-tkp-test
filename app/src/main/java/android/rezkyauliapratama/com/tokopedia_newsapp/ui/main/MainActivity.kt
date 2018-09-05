package android.rezkyauliapratama.com.tokopedia_newsapp.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.rezkyauliapratama.com.tokopedia_newsapp.BR
import android.rezkyauliapratama.com.tokopedia_newsapp.R
import android.rezkyauliapratama.com.tokopedia_newsapp.base.BaseActivity
import android.rezkyauliapratama.com.tokopedia_newsapp.base.BaseActivity_MembersInjector
import android.rezkyauliapratama.com.tokopedia_newsapp.databinding.ActivityMainBinding
import android.rezkyauliapratama.com.tokopedia_newsapp.ui.state.UiStatus
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.error

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    private lateinit var adapter : SourceRvAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initViewModel(): MainViewModel {
        return ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

    }

    override fun initBindingVariable(): Int {
        return BR.viewModel
    }

    override fun inject() {
        initActivityComponent()?.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initRv()
        initObserver()

    }

    private fun initView() {
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.retrieveData()
        }
    }

    private fun initObserver() {
        viewModel.uiStatusLD.observe(this, Observer {
            when(it){
                UiStatus.SHOW_LOADER -> swipeRefreshLayout.isRefreshing = true
                UiStatus.HIDE_LOADER -> swipeRefreshLayout.isRefreshing = false
                else ->{
                    error { "cannot found any state Ui" }
                }
            }
        })
    }


    private fun initRv() {
        adapter = SourceRvAdapter(this,viewModel){ id: String -> eventClicked(id) }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

    }

    private fun eventClicked(id: String) {

    }
}
