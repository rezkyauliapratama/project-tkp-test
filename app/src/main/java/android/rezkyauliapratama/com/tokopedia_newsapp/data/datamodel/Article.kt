package android.rezkyauliapratama.com.tokopedia_newsapp.data.datamodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Article(
        var author : String ?= null,
        var title : String ?= null,
        var description : String ?= null,
        var url : String ?= null,
        var urlToImage : String ?= null,
        var publishedAt : String ?= null
) : Parcelable