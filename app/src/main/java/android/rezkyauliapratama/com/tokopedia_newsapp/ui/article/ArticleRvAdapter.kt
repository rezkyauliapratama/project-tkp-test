package android.rezkyauliapratama.com.tokopedia_newsapp.ui.article

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.os.Parcelable
import android.rezkyauliapratama.com.tokopedia_newsapp.BR
import android.rezkyauliapratama.com.tokopedia_newsapp.R
import android.rezkyauliapratama.com.tokopedia_newsapp.data.datamodel.Article
import android.rezkyauliapratama.com.tokopedia_newsapp.databinding.ListItemArticleBinding
import android.rezkyauliapratama.com.tokopedia_newsapp.util.DimensionConverter
import android.rezkyauliapratama.com.tokopedia_newsapp.util.TimeUtility
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_article.*

class ArticleRvAdapter(private val lifecycle: LifecycleOwner, private val articleViewModel: ArticleViewModel,
                       private val width: Int?, private val clickListener : (String?, String) -> Unit) : RecyclerView.Adapter<ArticleRvAdapter.ViewHolder>() {

    val listItem : MutableList<Article> = mutableListOf()
    var state: Parcelable ?= null
    lateinit var recyclerView: RecyclerView

    init {
        articleViewModel.articleResponseLD.observe(lifecycle, Observer {
            listItem.clear()
            it?.apply { listItem.addAll(this) }
            notifyDataSetChanged()
            recyclerView.layoutManager.onRestoreInstanceState(state)

        })

        articleViewModel.rvStateLD.observe(lifecycle, Observer {
            state = it
        })
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_article, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = listItem.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(listItem[position],width,clickListener)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private var binding: ListItemArticleBinding = ListItemArticleBinding.bind(itemView)

        fun bindItem(article: Article, width: Int?, clickListener: (String?, String) -> Unit){

            if (width != null) {
                Picasso
                        .get()
                        .load(article.urlToImage)
                        .placeholder(R.drawable.ic_image)
                        .resize(width, 0)
                        .onlyScaleDown()
                        .centerInside()
                        .into(binding.ivPicture)
            }
            binding.setVariable(BR.article,article)
            binding.executePendingBindings()
            binding.root.setOnClickListener{
                clickListener(article.url, article.title)
            }
        }
    }


}