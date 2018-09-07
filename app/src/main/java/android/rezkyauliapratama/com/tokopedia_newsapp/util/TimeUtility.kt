package android.rezkyauliapratama.com.tokopedia_newsapp.util

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeUtility @Inject constructor(){

    fun convertDateToString(date: Date): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return simpleDateFormat.format(date)
    }

    fun getFriendlyDate(date: Date?): String {
        val simpleDateFormat = SimpleDateFormat("dd MMM yy, HH:mm", Locale.getDefault())
        var result = ""

        if (date != null) result = simpleDateFormat.format(date)

        return result
    }

    fun convertStringToDate(str: String ?): Date ?{
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",Locale.getDefault())
        var res : Date
        if (str != null)
            res = format.parse(str)

        return null;
    }
}