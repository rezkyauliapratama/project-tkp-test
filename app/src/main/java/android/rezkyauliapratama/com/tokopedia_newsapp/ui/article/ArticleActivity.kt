package android.rezkyauliapratama.com.tokopedia_newsapp.ui.article

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.rezkyauliapratama.com.tokopedia_newsapp.BR
import android.rezkyauliapratama.com.tokopedia_newsapp.R
import android.rezkyauliapratama.com.tokopedia_newsapp.base.BaseActivity
import android.rezkyauliapratama.com.tokopedia_newsapp.databinding.ActivityArticleBinding
import android.rezkyauliapratama.com.tokopedia_newsapp.ui.detail.DetailActivity
import android.rezkyauliapratama.com.tokopedia_newsapp.ui.state.UiStatus
import android.rezkyauliapratama.com.tokopedia_newsapp.util.DimensionConverter
import android.rezkyauliapratama.com.tokopedia_newsapp.util.TimeUtility
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_article.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity
import javax.inject.Inject

class ArticleActivity : BaseActivity<ActivityArticleBinding,ArticleViewModel>(){

    @Inject
    lateinit var timeUtility: TimeUtility

    private lateinit var adapter : ArticleRvAdapter
    private lateinit var data : Array<String>

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

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        data = intent.getStringArrayExtra("data")
        viewModel.restoreFromBundle(savedInstanceState,data.get(0))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = data.get(1)


        initView()
        initRv()
        initObserver()

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.getItemId()) {
            android.R.id.home -> {
                // ProjectsActivity is my 'home' activity
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        et_search.clearFocus()
        layout.requestFocus()
    }

    private fun initView() {
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.retrieveData(data.get(0))
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

        viewModel.queryLD.observe(this, Observer {
            et_search.setText(it)
        })

        viewModel.rvStateLD.observe(this, Observer {
            recyclerView.layoutManager.onRestoreInstanceState(it)
        })
    }


    private fun initRv() {
        //konversi nilai 96dp ke dalam float, nilai ini akan digunakan untuk mengeset width dari imageview di list item
        val width = DimensionConverter.instance?.stringToDimension("96dp", resources.getDisplayMetrics())?.toInt()

        adapter = ArticleRvAdapter(this,viewModel,width){ id: String?, title: String -> eventClicked(id, title) }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

    }

    private fun eventClicked(url: String?, title : String) {
        ctx.startActivity<DetailActivity>("data".to(arrayOf(url,title)))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val listState =  recyclerView.layoutManager.onSaveInstanceState();
        outState.putParcelable("liststate", listState);
        viewModel.saveToBundle(outState)
    }
}