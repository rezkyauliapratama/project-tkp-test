package android.rezkyauliapratama.com.tokopedia_newsapp.ui.article

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.rezkyauliapratama.com.tokopedia_newsapp.BR
import android.rezkyauliapratama.com.tokopedia_newsapp.R
import android.rezkyauliapratama.com.tokopedia_newsapp.data.datamodel.Article
import android.rezkyauliapratama.com.tokopedia_newsapp.databinding.ListItemArticleBinding
import android.rezkyauliapratama.com.tokopedia_newsapp.util.TimeUtility
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso

class ArticleRvAdapter(private val lifecycle: LifecycleOwner, private val articleViewModel: ArticleViewModel,
                       private val timeUtility: TimeUtility, private val clickListener : (String) -> Unit) : RecyclerView.Adapter<ArticleRvAdapter.ViewHolder>() {

    val listItem : MutableList<Article> = mutableListOf()

    init {
        articleViewModel.articleResponseLD.observe(lifecycle, Observer {
            listItem.clear()
            it?.apply { listItem.addAll(this) }
            notifyDataSetChanged()
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_article, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = listItem.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(listItem[position],timeUtility,clickListener)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private var binding: ListItemArticleBinding = ListItemArticleBinding.bind(itemView)

        fun bindItem(article: Article, timeUtility: TimeUtility, clickListener: (String) -> Unit){

            Picasso
                    .get()
                    .load(article.urlToImage)
                    .resize(92, 0)
                    .onlyScaleDown() // the image will only be resized if it's bigger than 6000x2000 pixels.
                    .centerCrop()
                    .into(binding.ivPicture);
            binding.setVariable(BR.article,article)
            binding.executePendingBindings()
            binding.root.setOnClickListener{
                clickListener(article.url)
            }
        }
    }


}