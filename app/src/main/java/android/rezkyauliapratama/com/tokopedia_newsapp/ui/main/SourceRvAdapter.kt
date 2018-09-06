package android.rezkyauliapratama.com.tokopedia_newsapp.ui.main

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.rezkyauliapratama.com.tokopedia_newsapp.BR
import android.rezkyauliapratama.com.tokopedia_newsapp.R
import android.rezkyauliapratama.com.tokopedia_newsapp.data.datamodel.Source
import android.rezkyauliapratama.com.tokopedia_newsapp.databinding.ListItemSourceBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SourceRvAdapter(private val lifecycle:LifecycleOwner,private val mainViewModel: MainViewModel, private val clickListener : (String) -> Unit) : RecyclerView.Adapter<SourceRvAdapter.ViewHolder>() {

    val listItem : MutableList<Source> = mutableListOf()

    init {

        mainViewModel.sourceResponseLD.observe(lifecycle, Observer {
            listItem.clear()
                if (it != null) {
                    if (it?.status.equals("ok") ){
                        listItem.addAll(it.sources)
                    }
                }
            notifyDataSetChanged()
        })
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_source, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = listItem.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(listItem[position],clickListener)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private var binding:ListItemSourceBinding = ListItemSourceBinding.bind(itemView)

        fun bindItem(source: Source, clickListener: (String) -> Unit){
            binding.setVariable(BR.source,source)
            binding.executePendingBindings()
            binding.root.setOnClickListener{
                clickListener(source.url.run { if(startsWith("www.")) substring(4) else this })
            }
        }
    }


}