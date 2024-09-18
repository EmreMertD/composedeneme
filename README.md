import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SecuredRetrofitClientTest {

    private lateinit var securedRetrofitClient: SecuredRetrofitClient
    private val mockService = mock(RetrofitService::class.java)
    private val gson = mock(com.google.gson.Gson::class.java)

    @Before
    fun setUp() {
        // securedRetrofitClient sınıfını mock service ile başlatıyoruz
        securedRetrofitClient = SecuredRetrofitClient(
            firstdomain = "https://example.com",
            certificateHashValues = emptyList()
        ).apply {
            service = mockService
        }
    }

    @Test
    fun `test get request success`() = runBlocking {
        val mockResponse = Response.success("Mock Response Body")

        `when`(mockService.get(anyString(), anyMap(), anyMap())).thenReturn(mockResponse)

        val result: String = securedRetrofitClient.get(
            route = "/test",
            queries = emptyMap(),
            headers = emptyMap()
        )

        assertEquals("Mock Response Body", result)
    }

    @Test
    fun `test get request failure - non 2xx response`() = runBlocking {
        val mockResponse = Response.error<String>(500, ResponseBody.create(null, "Error"))

        `when`(mockService.get(anyString(), anyMap(), anyMap())).thenReturn(mockResponse)

        assertFailsWith<HttpClientException> {
            securedRetrofitClient.get<String>(
                route = "/test",
                queries = emptyMap(),
                headers = emptyMap()
            )
        }
    }

    @Test
    fun `test getSync request success`() {
        val mockResponse = Response.success("Mock Sync Response")

        `when`(mockService.getSync(anyString(), anyMap(), anyMap())).thenReturn(mockResponse)

        val result: String = securedRetrofitClient.getSync(
            route = "/test",
            queries = emptyMap(),
            headers = emptyMap(),
            responseClass = String::class.java
        )

        assertEquals("Mock Sync Response", result)
    }

    @Test
    fun `test getSync request failure`() {
        val mockResponse = Response.error<String>(500, ResponseBody.create(null, "Sync Error"))

        `when`(mockService.getSync(anyString(), anyMap(), anyMap())).thenReturn(mockResponse)

        assertFailsWith<HttpClientException> {
            securedRetrofitClient.getSync<String>(
                route = "/test",
                queries = emptyMap(),
                headers = emptyMap(),
                responseClass = String::class.java
            )
        }
    }

    @Test
    fun `test post request success`() = runBlocking {
        val mockResponse = Response.success("Mock Post Response")

        `when`(mockService.post(anyString(), anyMap(), anyMap())).thenReturn(mockResponse)

        val result: String = securedRetrofitClient.post(
            route = "/test",
            queries = emptyMap(),
            headers = emptyMap()
        )

        assertEquals("Mock Post Response", result)
    }

    @Test
    fun `test post request failure`() = runBlocking {
        val mockResponse = Response.error<String>(500, ResponseBody.create(null, "Post Error"))

        `when`(mockService.post(anyString(), anyMap(), anyMap())).thenReturn(mockResponse)

        assertFailsWith<HttpClientException> {
            securedRetrofitClient.post<String>(
                route = "/test",
                queries = emptyMap(),
                headers = emptyMap()
            )
        }
    }
}
