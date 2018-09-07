package android.rezkyauliapratama.com.tokopedia_newsapp.data.datamodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class Article(
        val author : String,
        val title : String,
        val description : String,
        val url : String,
        val urlToImage : String,
        var publishedAt : String
)