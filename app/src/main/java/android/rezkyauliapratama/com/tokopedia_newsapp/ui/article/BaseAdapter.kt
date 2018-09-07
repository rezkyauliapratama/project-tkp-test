package android.rezkyauliapratama.com.tokopedia_newsapp.ui.article

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by Rezky Aulia Pratama on 6/9/18.
 */

abstract class BaseAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onLoadMoreListener: OnLoadMoreListener? = null
    private var isLoading: Boolean = false
    private val visibleThreshold = 5
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0

    fun setOnLoadMoreListener(mOnLoadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener

    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = recyclerView.layoutManager.itemCount
                lastVisibleItem = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                        onLoadMoreListener?.onLoadMore()

                    isLoading = true
                }
            }
        })
    }

    fun setLoaded() {
        isLoading = false
    }

}