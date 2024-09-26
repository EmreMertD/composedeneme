'''


    import io.mockk.*
    import kotlinx.coroutines.ExperimentalCoroutinesApi
    import kotlinx.coroutines.test.runBlockingTest
    import org.junit.Before
    import org.junit.Test
    import kotlin.test.assertEquals
    import kotlin.test.assertNotNull
    
    @OptIn(ExperimentalCoroutinesApi::class)
    class DigitalNetworkHandlerTest {

    private val mockNetworkHandler = mockk<NetworkHandler>()
    private val mockCallback = mockk<(InvokerResult<Any>) -> Unit>(relaxed = true)
    private val mockHeaders = mockk<Header>(relaxed = true)

    @Before
    fun setup() {
        mockkObject(NetworkHandler.Companion)
        every { NetworkHandler(any()) } returns mockNetworkHandler
    }

    @Test
    fun `successful GET request`() = runBlockingTest {
        // Arrange
        val mockResponse = mockk<NetworkResponse<Any>>()
        coEvery { mockNetworkHandler.get<Any>(any(), any(), any(), any()) } returns mockResponse
        coEvery { mockResponse.onSuccessSuspend(any()) } answers {
            secondArg<(Any) -> Unit>().invoke(mockk())
        }

        // Act
        DigitalNetworkHandler.request<Any, Any>(
            route = "test/route",
            method = Method.Get,
            headers = mockHeaders,
            callback = mockCallback
        )

        // Assert
        coVerify { mockCallback.invoke(any()) }
    }

    @Test
    fun `successful POST request`() = runBlockingTest {
        // Arrange
        val mockResponse = mockk<NetworkResponse<Any>>()
        coEvery { mockNetworkHandler.post<Any, Any>(any(), any(), any(), any(), any()) } returns mockResponse
        coEvery { mockResponse.onSuccessSuspend(any()) } answers {
            secondArg<(Any) -> Unit>().invoke(mockk())
        }

        // Act
        DigitalNetworkHandler.request<Any, Any>(
            route = "test/route",
            method = Method.Post,
            body = Any(),
            headers = mockHeaders,
            callback = mockCallback
        )

        // Assert
        coVerify { mockCallback.invoke(any()) }
    }

    @Test
    fun `POST request with null body should return error`() = runBlockingTest {
        // Act
        DigitalNetworkHandler.request<Any, Any>(
            route = "test/route",
            method = Method.Post,
            headers = mockHeaders,
            callback = mockCallback
        )

        // Assert
        coVerify {
            mockCallback.invoke(
                match { result ->
                    (result as InvokerResult.Error).error.message == "Body is required for POST method"
                }
            )
        }
    }

    @Test
    fun `token refresh on UNAUTHORIZED error`() = runBlockingTest {
        // Arrange
        val mockResponse = mockk<NetworkResponse<Any>>()
        coEvery { mockNetworkHandler.get<Any>(any(), any(), any(), any()) } returns mockResponse
        coEvery { mockResponse.onErrorSuspend(any()) } answers {
            firstArg<(Int, String) -> Unit>().invoke(HttpStatus.UNAUTHORIZED.value(), "Unauthorized")
        }
        coEvery { TokenManager.refreshToken(any()) } answers {
            firstArg<() -> Unit>().invoke()
        }

        // Act
        DigitalNetworkHandler.request<Any, Any>(
            route = "test/route",
            method = Method.Get,
            headers = mockHeaders,
            callback = mockCallback
        )

        // Assert
        coVerify { TokenManager.refreshToken(any()) }
        coVerify { mockCallback.invoke(any()) }
    }

    @Test
    fun `onFailureSuspend is invoked on exception`() = runBlockingTest {
        // Arrange
        val mockResponse = mockk<NetworkResponse<Any>>()
        coEvery { mockNetworkHandler.get<Any>(any(), any(), any(), any()) } throws Exception("Network error")

        // Act
        DigitalNetworkHandler.request<Any, Any>(
            route = "test/route",
            method = Method.Get,
            headers = mockHeaders,
            callback = mockCallback
        )

        // Assert
        coVerify {
            mockCallback.invoke(
                match { result ->
                    (result as InvokerResult.Error).error.message == "Network error"
                }
            )
        }
    }
    
        @Test
    fun `UNAUTHORIZED error persists after token refresh`() = runBlockingTest {
        // Arrange
        val mockResponse = mockk<NetworkResponse<Any>>()
        coEvery { mockNetworkHandler.get<Any>(any(), any(), any(), any()) } returns mockResponse
        coEvery { mockResponse.onErrorSuspend(any()) } answers {
            firstArg<(Int, String) -> Unit>().invoke(HttpStatus.UNAUTHORIZED.value(), "Unauthorized")
        }
        coEvery { TokenManager.refreshToken(any()) } answers {
            firstArg<() -> Unit>().invoke()
        }
        // Simulate UNAUTHORIZED again after refresh
        coEvery { mockNetworkHandler.get<Any>(any(), any(), any(), any()) } returns mockResponse
        coEvery { mockResponse.onErrorSuspend(any()) } answers {
            firstArg<(Int, String) -> Unit>().invoke(HttpStatus.UNAUTHORIZED.value(), "Unauthorized Again")
        }
    
        // Act
        DigitalNetworkHandler.request<Any, Any>(
            route = "test/route",
            method = Method.Get,
            headers = mockHeaders,
            callback = mockCallback
        )
    
        // Assert
        coVerify { mockCallback.invoke(match { result ->
            (result as InvokerResult.Error).error.message == "Unauthorized Again"
        }) }
    }
    
    @Test
    fun `token refresh throws exception`() = runBlockingTest {
        // Arrange
        val mockResponse = mockk<NetworkResponse<Any>>()
        coEvery { mockNetworkHandler.get<Any>(any(), any(), any(), any()) } returns mockResponse
        coEvery { mockResponse.onErrorSuspend(any()) } answers {
            firstArg<(Int, String) -> Unit>().invoke(HttpStatus.UNAUTHORIZED.value(), "Unauthorized")
        }
        coEvery { TokenManager.refreshToken(any()) } throws Exception("Refresh Token Failed")
    
        // Act
        DigitalNetworkHandler.request<Any, Any>(
            route = "test/route",
            method = Method.Get,
            headers = mockHeaders,
            callback = mockCallback
        )
    
        // Assert
        coVerify {
            mockCallback.invoke(
                match { result ->
                    (result as InvokerResult.Error).error.message == "Refresh Token Failed"
                }
            )
        }
    }
    
    @Test
    fun `responseType not null for GET request`() = runBlockingTest {
        // Arrange
        val mockResponse = mockk<NetworkResponse<Any>>()
        val mockType = mockk<Type>()
        coEvery { mockNetworkHandler.get<Any>(any(), any(), any(), any()) } returns mockResponse
        coEvery { mockResponse.onSuccessSuspend(any()) } answers {
            secondArg<(Any) -> Unit>().invoke(mockk())
        }
    
        // Act
        DigitalNetworkHandler.request<Any, Any>(
            route = "test/route",
            method = Method.Get,
            headers = mockHeaders,
            responseType = mockType,
            callback = mockCallback
        )
    
        // Assert
        coVerify { mockCallback.invoke(any()) }
    }
    
    @Test
    fun `callback is null does not crash`() = runBlockingTest {
        // Arrange
        val mockResponse = mockk<NetworkResponse<Any>>()
        coEvery { mockNetworkHandler.get<Any>(any(), any(), any(), any()) } returns mockResponse
        coEvery { mockResponse.onSuccessSuspend(any()) } answers {
            secondArg<(Any) -> Unit>().invoke(mockk())
        }
    
        // Act
        DigitalNetworkHandler.request<Any, Any>(
            route = "test/route",
            method = Method.Get,
            headers = mockHeaders,
            callback = null
        )
    
        // Assert
        // No exception should be thrown, and code should run smoothly.
    }
    
    @Test
    fun `successful retry after token refresh`() = runBlockingTest {
        // Arrange
        val mockResponse = mockk<NetworkResponse<Any>>()
        coEvery { mockNetworkHandler.get<Any>(any(), any(), any(), any()) } returns mockResponse
        coEvery { mockResponse.onErrorSuspend(any()) } answers {
            firstArg<(Int, String) -> Unit>().invoke(HttpStatus.UNAUTHORIZED.value(), "Unauthorized")
        }
        coEvery { TokenManager.refreshToken(any()) } answers {
            firstArg<() -> Unit>().invoke()
        }
        // Simulate success after token refresh
        coEvery { mockNetworkHandler.get<Any>(any(), any(), any(), any()) } returns mockResponse
        coEvery { mockResponse.onSuccessSuspend(any()) } answers {
            secondArg<(Any) -> Unit>().invoke(mockk())
        }

    // Act
    DigitalNetworkHandler.request<Any, Any>(
        route = "test/route",
        method = Method.Get,
        headers = mockHeaders,
        callback = mockCallback
    )

    // Assert
    coVerify { mockCallback.invoke(match { result ->
        result is InvokerResult.Success
    }) }
    }




    }







'''
