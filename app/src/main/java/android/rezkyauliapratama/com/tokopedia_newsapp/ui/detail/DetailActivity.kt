package android.rezkyauliapratama.com.tokopedia_newsapp.ui.detail

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.rezkyauliapratama.com.tokopedia_newsapp.BR
import android.rezkyauliapratama.com.tokopedia_newsapp.R
import android.rezkyauliapratama.com.tokopedia_newsapp.base.BaseActivity
import android.rezkyauliapratama.com.tokopedia_newsapp.databinding.ActivityDetailBinding
import android.view.MenuItem
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_article.*
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.error


class DetailActivity: BaseActivity<ActivityDetailBinding, DetailViewModel>(){
    private lateinit var data : Array<String>

    override fun getLayoutId(): Int {
        return R.layout.activity_detail;
    }

    override fun initViewModel(): DetailViewModel {
        return ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)

    }

    override fun initBindingVariable(): Int {
        return BR.viewModel
    }

    override fun inject() {
        initActivityComponent()?.inject(this)

    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data = intent.getStringArrayExtra("data")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = data.get(1)



        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true

        webView.loadUrl(data.get(0))
        webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                progressBar.visibility = View.GONE
            }
        }

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

}