package android.rezkyauliapratama.com.tokopedia_newsapp.data.datamodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Source(
        val id : String,
        val name : String,
        val description : String,
        var url : String,
        val category : String,
        val language : String,
        val country : String
) : Parcelable