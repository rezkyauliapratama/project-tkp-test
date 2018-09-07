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
import android.rezkyauliapratama.com.tokopedia_newsapp.ui.article.ArticleActivity
import android.rezkyauliapratama.com.tokopedia_newsapp.ui.state.UiStatus
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity

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

        viewModel.restoreFromBundle(savedInstanceState)


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
                UiStatus.EMPTY -> {
                    layout_status.visibility = View.VISIBLE
                    lottieView.playAnimation()
                    tv_status.text = getString(R.string.sorrycannotfounddata)
                }
                UiStatus.NO_NETWORK -> {
                    layout_status.visibility = View.VISIBLE
                    lottieView.playAnimation()
                    tv_status.text = getString(R.string.sorrypleasecheckyourinternet)
                }
                UiStatus.ERROR_LOAD -> {
                    layout_status.visibility = View.VISIBLE
                    lottieView.playAnimation()
                    tv_status.text = getString(R.string.apologiesfortheinconvenience)
                }
                UiStatus.HIDE_STATUS -> {
                    layout_status.visibility = View.GONE
                    tv_status.text = ""
                }
                else ->{
                    error { "cannot found any state Ui" }
                }
            }
        })

        viewModel.rvStateLD.observe(this, Observer {
            recyclerView.layoutManager.onRestoreInstanceState(it)
        })
    }


    private fun initRv() {
        adapter = SourceRvAdapter(this,viewModel){ id: String, name : String -> eventClicked(id, name) }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

    }

    private fun eventClicked(id: String, name : String) {
        ctx.startActivity<ArticleActivity>("data".to(arrayOf(id,name)))

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable("liststate", recyclerView.layoutManager.onSaveInstanceState())
        viewModel.saveToBundle(outState)
    }


}
