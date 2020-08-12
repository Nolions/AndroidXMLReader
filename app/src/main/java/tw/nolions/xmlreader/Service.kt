package tw.nolions.xmlreader

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("RssAtomFeed.ashx")
    suspend fun alert(@Query("AlertType") type: Int): Response<String>

}