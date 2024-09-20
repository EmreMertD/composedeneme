import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class DigitalNetworkHandlerTest {

    private lateinit var digitalNetwork: DigitalNetwork
    private lateinit var networkHandler: NetworkHandler
    private lateinit var tokenManager: TokenManager
    private lateinit var digitalNetworkHandler: DigitalNetworkHandler

    @Before
    fun setUp() {
        digitalNetwork = mock(DigitalNetwork::class.java)
        networkHandler = mock(NetworkHandler::class.java)
        tokenManager = mock(TokenManager::class.java)
        
        // DigitalNetworkHandler instance'ını initialize ediyoruz
        digitalNetworkHandler = DigitalNetworkHandler(digitalNetwork)
    }

    @Test
    fun `test GET request success`() = runTest {
        // Başarılı bir GET isteği senaryosu
        val route = "test/route"
        val method = Method.Get
        val headers = mapOf("Authorization" to "Bearer token")
        val queries = mapOf("key" to "value")
        
        // Beklenen yanıt türü (mock response)
        val expectedResponse = mock(Any::class.java)

        // networkHandler.get çağrıldığında expectedResponse dönecek şekilde ayarlarız
        `when`(networkHandler.get<Any>(anyString(), anyMap(), anyMap(), any<Type>())).thenReturn(expectedResponse)

        // request fonksiyonunu çağırıyoruz
        digitalNetworkHandler.request<Any, Any>(
            route = route,
            method = method,
            headers = headers,
            queries = queries,
            callback = {
                // İsteğin başarılı olduğunu ve beklenen yanıtı aldığımızı doğruluyoruz
                assertEquals(InvokerResult.success(expectedResponse), it)
            }
        )
        
        // networkHandler.get metodunun çağrılıp çağrılmadığını kontrol edelim
        verify(networkHandler).get<Any>(route, headers as HashMap<String, String>, queries as HashMap<String, String>, null)
    }

    @Test
    fun `test POST request without body returns error`() = runTest {
        val route = "test/route"
        val method = Method.Post
        val headers = mapOf("Authorization" to "Bearer token")
        val queries = mapOf("key" to "value")

        digitalNetworkHandler.request<Any, Any>(
            route = route,
            method = method,
            headers = headers,
            queries = queries,
            body = null,
            callback = {
                // Boş body ile POST isteğinde bulunursak, 400 hata kodu döndürülmeli
                val error = (it as InvokerResult.Error).error
                assertEquals(400, error.code)
                assertEquals("Body is required for POST method", error.message)
            }
        )
    }

    @Test
    fun `test token refresh on 401 error`() = runTest {
        val route = "test/route"
        val method = Method.Get
        val headers = mapOf("Authorization" to "Bearer expired_token")
        val queries = mapOf("key" to "value")

        // 401 hatası alındığında, token yenileme işlemi tetiklenecek
        `when`(networkHandler.get<Any>(anyString(), anyMap(), anyMap(), any<Type>())).thenThrow(
            InvokerError(401, "Unauthorized")
        )

        // TokenManager'ın refreshToken fonksiyonunu mockluyoruz
        `when`(tokenManager.refreshToken(any())).thenAnswer {
            // token yenilendiğinde token değişecek
            digitalNetworkHandler.onceTokenRefreshed = true
            Unit
        }

        digitalNetworkHandler.request<Any, Any>(
            route = route,
            method = method,
            headers = headers,
            queries = queries,
            callback = {
                // Yenilenen token ile tekrar denenecek
                verify(tokenManager).refreshToken(any())
            }
        )
    }
}
