import com.google.gson.JsonElement
import com.google.gson.JsonParser
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import retrofit2.Response
import kotlin.test.assertEquals
import io.kotest.assertions.throwables.shouldThrow

class SecuredRetrofitClientTest {

    private lateinit var securedRetrofitClient: SecuredRetrofitClient
    private val mockService = mock(RetrofitService::class.java)
    private val mockGson = mock(com.google.gson.Gson::class.java)

    @Before
    fun setUp() {
        // securedRetrofitClient sınıfını mock service ile başlatıyoruz
        securedRetrofitClient = SecuredRetrofitClient(
            firstdomain = "https://example.com",
            certificateHashValues = emptyList()
        ).apply {
            // `service` val olarak tanımlandığı için mock yapılamıyor,
            // ancak testin içinde mock kullanacağımızdan dolayı RetrofitService'i simüle edeceğiz.
            val field = SecuredRetrofitClient::class.java.getDeclaredField("service")
            field.isAccessible = true
            field.set(this, mockService)
        }
    }

    @Test
    fun `test get request success`() = runBlocking {
        // Mock JSON response
        val mockJsonResponse = JsonParser.parseString("{\"message\": \"Mock Response Body\"}")

        val mockResponse = Response.success(mockJsonResponse)

        `when`(mockService.get(anyString(), anyMap(), anyMap())).thenReturn(mockResponse)
        `when`(mockGson.fromJson<JsonElement>(any(), eq(JsonElement::class.java))).thenReturn(mockJsonResponse)

        val result: JsonElement = securedRetrofitClient.get(
            route = "/test",
            queries = emptyMap(),
            headers = emptyMap()
        )

        assertEquals(mockJsonResponse, result)
    }

    @Test
    fun `test get request failure - non 2xx response`() = runBlocking {
        // Mock error response
        val mockErrorResponse = Response.error<JsonElement>(500, ResponseBody.create(null, "Error"))

        `when`(mockService.get(anyString(), anyMap(), anyMap())).thenReturn(mockErrorResponse)

        shouldThrow<HttpClientException> {
            securedRetrofitClient.get<JsonElement>(
                route = "/test",
                queries = emptyMap(),
                headers = emptyMap()
            )
        }
    }

    @Test
    fun `test getSync request success`() {
        // Mock JSON response
        val mockJsonResponse = JsonParser.parseString("{\"message\": \"Mock Sync Response\"}")

        val mockResponse = Response.success(mockJsonResponse)

        `when`(mockService.getSync(anyString(), anyMap(), anyMap())).thenReturn(mockResponse)
        `when`(mockGson.parseElement(any(), eq(JsonElement::class.java))).thenReturn(mockJsonResponse)

        val result: JsonElement = securedRetrofitClient.getSync(
            route = "/test",
            queries = emptyMap(),
            headers = emptyMap(),
            responseClass = JsonElement::class.java
        )

        assertEquals(mockJsonResponse, result)
    }

    @Test
    fun `test getSync request failure`() {
        val mockErrorResponse = Response.error<JsonElement>(500, ResponseBody.create(null, "Sync Error"))

        `when`(mockService.getSync(anyString(), anyMap(), anyMap())).thenReturn(mockErrorResponse)

        shouldThrow<HttpClientException> {
            securedRetrofitClient.getSync<JsonElement>(
                route = "/test",
                queries = emptyMap(),
                headers = emptyMap(),
                responseClass = JsonElement::class.java
            )
        }
    }

    @Test
    fun `test post request success`() = runBlocking {
        val mockJsonResponse = JsonParser.parseString("{\"message\": \"Mock Post Response\"}")

        val mockResponse = Response.success(mockJsonResponse)

        `when`(mockService.post(anyString(), anyMap(), anyMap())).thenReturn(mockResponse)
        `when`(mockGson.fromJson<JsonElement>(any(), eq(JsonElement::class.java))).thenReturn(mockJsonResponse)

        val result: JsonElement = securedRetrofitClient.post(
            route = "/test",
            queries = emptyMap(),
            headers = emptyMap()
        )

        assertEquals(mockJsonResponse, result)
    }

    @Test
    fun `test post request failure`() = runBlocking {
        val mockErrorResponse = Response.error<JsonElement>(500, ResponseBody.create(null, "Post Error"))

        `when`(mockService.post(anyString(), anyMap(), anyMap())).thenReturn(mockErrorResponse)

        shouldThrow<HttpClientException> {
            securedRetrofitClient.post<JsonElement>(
                route = "/test",
                queries = emptyMap(),
                headers = emptyMap()
            )
        }
    }
}
