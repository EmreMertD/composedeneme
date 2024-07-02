import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class DigitalNetworkHandlerTest {

    private val mockNetworkHandler: NetworkHandler = mock()
    private val mockDigitalNetwork: Network = mock()

    @Test
    fun testGet() = runTest {
        val route = "test/route"
        val headerMap = hashMapOf<String, String>()
        val queryMap = hashMapOf<String, String>()
        val expectedResponse = ApiResult.Success(ApiResponse("success"))

        whenever(mockNetworkHandler.get<ApiResponse>(
            route = route,
            headerMap = headerMap,
            queryMap = queryMap
        )).thenReturn(expectedResponse)

        val result = DigitalNetworkHandler.get<ApiResponse>(
            route = route,
            headerMap = headerMap,
            queryMap = queryMap,
            digitalNetwork = mockDigitalNetwork
        )

        assertEquals(expected = expectedResponse, actual = result)
    }

    @Test
    fun testPost() = runTest {
        val route = "test/route"
        val headerMap = hashMapOf<String, String>()
        val queryMap = hashMapOf<String, String>()
        val body = hashMapOf<String, Any>()
        val expectedResponse = ApiResult.Success(ApiResponse("success"))

        whenever(mockNetworkHandler.post<ApiResponse>(
            route = route,
            headerMap = headerMap,
            queryMap = queryMap,
            body = body
        )).thenReturn(expectedResponse)

        val result = DigitalNetworkHandler.post<ApiResponse>(
            route = route,
            headerMap = headerMap,
            queryMap = queryMap,
            body = body,
            digitalNetwork = mockDigitalNetwork
        )

        assertEquals(expected = expectedResponse, actual = result)
    }
}
