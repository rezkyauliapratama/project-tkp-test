package android.rezkyauliapratama.com.tokopedia_newsapp.data.datamodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Source(
        var id : String,
        var name : String,
        var description : String,
        var url : String,
        var category : String,
        var language : String,
        var country : String
) : Parcelable