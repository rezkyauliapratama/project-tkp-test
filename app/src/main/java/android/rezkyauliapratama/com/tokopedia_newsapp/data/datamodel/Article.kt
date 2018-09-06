package android.rezkyauliapratama.com.tokopedia_newsapp.data.datamodel

data class Article(
        val source : Source,
        val author : String,
        val title : String,
        val description : String,
        val url : String,
        val urlToImage : String,
        var publishedAt : String
)