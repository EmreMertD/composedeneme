    import okhttp3.mockwebserver.MockResponse
    import okhttp3.mockwebserver.MockWebServer
    import org.junit.After
    import org.junit.Before
    import org.junit.Test
    import io.mockk.*
    import kotlinx.coroutines.runBlocking
    import kotlin.test.assertEquals
    import kotlin.test.assertNotNull
    
    class DigitalNetworkHandlerTest {

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        // MockWebServer'ı başlatıyoruz
        mockWebServer = MockWebServer()
        mockWebServer.start()

        // DigitalNetworkHandler'ı MockWebServer adresine yönlendirin
        every { provideNetwork() } returns mockWebServer.url("/").toString()
    }

    @After
    fun teardown() {
        // Testlerden sonra MockWebServer'ı kapatın
        mockWebServer.shutdown()
    }

    @Test
    fun `test successful GET request`() = runBlocking {
        // MockWebServer'a başarıyla dönecek bir yanıt ekliyoruz
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("""{"message": "success"}""")
        )

        // Mock callback
        val mockCallback = mockk<(InvokerResult<Any>) -> Unit>(relaxed = true)

        // DigitalNetworkHandler üzerinden GET isteği yap
        DigitalNetworkHandler.request<Any, Any>(
            route = "test/route",
            method = Method.Get,
            callback = mockCallback
        )

        // Gelen isteği doğrula
        val recordedRequest = mockWebServer.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/test/route", recordedRequest.path)

        // Callback'in başarıyla çağrıldığını doğrula
        verify { mockCallback.invoke(match { it is InvokerResult.Success }) }
    }

    @Test
    fun `test successful POST request`() = runBlocking {
        // MockWebServer'a başarıyla dönecek bir yanıt ekliyoruz
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("""{"message": "success"}""")
        )

        // Mock callback
        val mockCallback = mockk<(InvokerResult<Any>) -> Unit>(relaxed = true)
        val requestBody = mapOf("key" to "value")

        // DigitalNetworkHandler üzerinden POST isteği yap
        DigitalNetworkHandler.request<Any, Any>(
            route = "test/route",
            method = Method.Post,
            body = requestBody,
            callback = mockCallback
        )

        // Gelen isteği doğrula
        val recordedRequest = mockWebServer.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/test/route", recordedRequest.path)
        assertEquals("""{"key":"value"}""", recordedRequest.body.readUtf8())

        // Callback'in başarıyla çağrıldığını doğrula
        verify { mockCallback.invoke(match { it is InvokerResult.Success }) }
    }

    @Test
    fun `test unauthorized error response`() = runBlocking {
        // MockWebServer'a 401 dönecek bir yanıt ekliyoruz
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(401)
                .setBody("""{"error": "Unauthorized"}""")
        )

        // Mock callback
        val mockCallback = mockk<(InvokerResult<Any>) -> Unit>(relaxed = true)

        // DigitalNetworkHandler üzerinden GET isteği yap
        DigitalNetworkHandler.request<Any, Any>(
            route = "test/route",
            method = Method.Get,
            callback = mockCallback
        )

        // Gelen isteği doğrula
        val recordedRequest = mockWebServer.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/test/route", recordedRequest.path)

        // Callback'in hata ile çağrıldığını doğrula
        verify { mockCallback.invoke(match { it is InvokerResult.Error }) }
    }

    @Test
    fun `test internal server error response`() = runBlocking {
        // MockWebServer'a 500 dönecek bir yanıt ekliyoruz
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody("""{"error": "Internal Server Error"}""")
        )

        // Mock callback
        val mockCallback = mockk<(InvokerResult<Any>) -> Unit>(relaxed = true)

        // DigitalNetworkHandler üzerinden GET isteği yap
        DigitalNetworkHandler.request<Any, Any>(
            route = "test/route",
            method = Method.Get,
            callback = mockCallback
        )

        // Gelen isteği doğrula
        val recordedRequest = mockWebServer.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/test/route", recordedRequest.path)

        // Callback'in hata ile çağrıldığını doğrula
        verify { mockCallback.invoke(match { it is InvokerResult.Error }) }
    }
    }
