package tw.nolions.xmlreader

import retrofit2.Response

object Repository {
    private val ns: String? = null

    private var apiService: Service? = null

    fun baseAPI(baseApi: String) {
        apiService = ApiProvider(baseApi).createService(Service::class.java)
    }

    suspend fun suspensionClasses(): RespDataModel<String> = genericDataModel(apiService?.alert(33))
}

private fun <T> genericDataModel(resp: Response<T>? = null): RespDataModel<T> {
    return RespDataModel(
        resp?.isSuccessful,
        resp?.code()!!,
        resp.body()
    )
}

data class RespDataModel<T>(
    val isSuccess: Boolean?,
    val code: Int = 0,
    val data: T? = null
)


