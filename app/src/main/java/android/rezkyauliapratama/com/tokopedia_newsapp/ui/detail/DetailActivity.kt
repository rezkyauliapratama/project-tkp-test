package android.rezkyauliapratama.com.tokopedia_newsapp.ui.detail

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.rezkyauliapratama.com.tokopedia_newsapp.BR
import android.rezkyauliapratama.com.tokopedia_newsapp.R
import android.rezkyauliapratama.com.tokopedia_newsapp.base.BaseActivity
import android.rezkyauliapratama.com.tokopedia_newsapp.databinding.ActivityDetailBinding
import android.webkit.WebSettings
import android.webkit.WebView
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.error


class DetailActivity: BaseActivity<ActivityDetailBinding, DetailViewModel>(){
    private lateinit var url : String

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
        url = intent.getStringExtra("url")



        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true

        error { "url : "+url }
        webView.loadUrl(url)
    }

}