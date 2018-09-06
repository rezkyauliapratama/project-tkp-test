package android.rezkyauliapratama.com.tokopedia_newsapp.ui.article

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.rezkyauliapratama.com.tokopedia_newsapp.BR
import android.rezkyauliapratama.com.tokopedia_newsapp.R
import android.rezkyauliapratama.com.tokopedia_newsapp.base.BaseActivity
import android.rezkyauliapratama.com.tokopedia_newsapp.databinding.ActivityArticleBinding
import android.rezkyauliapratama.com.tokopedia_newsapp.ui.state.UiStatus
import android.rezkyauliapratama.com.tokopedia_newsapp.util.TimeUtility
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_article.*
import javax.inject.Inject

class ArticleActivity : BaseActivity<ActivityArticleBinding,ArticleViewModel>(){

    @Inject
    lateinit var timeUtility: TimeUtility

    private lateinit var adapter : ArticleRvAdapter
    private lateinit var id : String

    override fun getLayoutId(): Int {
        return R.layout.activity_article
    }

    override fun initViewModel(): ArticleViewModel {
        return ViewModelProviders.of(this, viewModelFactory).get(ArticleViewModel::class.java)

    }

    override fun initBindingVariable(): Int {
        return BR.article
    }

    override fun inject() {
        initActivityComponent()?.inject(this)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        id = intent.getStringExtra("id")


        initView()
        initRv()
        initObserver()

    }

    private fun initView() {
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.retrieveData(id)
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
        adapter = ArticleRvAdapter(this,viewModel,timeUtility){ id: String -> eventClicked(id) }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

    }

    private fun eventClicked(id: String) {

    }
}