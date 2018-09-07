package android.rezkyauliapratama.com.tokopedia_newsapp.ui.article

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Parcelable
import android.rezkyauliapratama.com.tokopedia_newsapp.BR
import android.rezkyauliapratama.com.tokopedia_newsapp.R
import android.rezkyauliapratama.com.tokopedia_newsapp.base.BaseActivity
import android.rezkyauliapratama.com.tokopedia_newsapp.databinding.ActivityArticleBinding
import android.rezkyauliapratama.com.tokopedia_newsapp.ui.detail.DetailActivity
import android.rezkyauliapratama.com.tokopedia_newsapp.ui.state.UiStatus
import android.rezkyauliapratama.com.tokopedia_newsapp.util.TimeUtility
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_article.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.error
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.startActivity
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
        return BR.viewModel
    }

    override fun inject() {
        initActivityComponent()?.inject(this)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        id = intent.getStringExtra("id")

        viewModel.restoreFromBundle(savedInstanceState,id)

        initView()
        initRv()
        initObserver()

    }

    override fun onResume() {
        super.onResume()
        et_search.clearFocus()
        layout.requestFocus()
    }

    private fun initView() {
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.retrieveData(id)
        }

        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.search(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
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

        viewModel.queryLD.observe(this, Observer {
            et_search.setText(it)
        })

        viewModel.rvStateLD.observe(this, Observer {
            recyclerView.layoutManager.onRestoreInstanceState(it)
        })
    }


    private fun initRv() {
        adapter = ArticleRvAdapter(this,viewModel,timeUtility){ id: String? -> eventClicked(id) }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

    }

    private fun eventClicked(url: String?) {
        ctx.startActivity<DetailActivity>("url".to(url))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        error { "onSaveInstanceState" }
        val listState =  recyclerView.layoutManager.onSaveInstanceState();
        outState.putParcelable("liststate", listState);
        viewModel.saveToBundle(outState)
    }
}