import org.junit.Test
import kotlin.test.assertEquals

class CustomJsonBeautifierTest {

    @Test
    fun `beautifyJsonString should format JSON string with proper indentation`() {
        // Given
        val jsonString = """{"name":"John","age":30,"city":"New York"}"""
        val expectedFormattedJson = """
            {
                "name": "John",
                "age": 30,
                "city": "New York"
            }
        """.trimIndent()

        // When
        val formattedJson = CustomJsonBeautifier.beautifyJsonString(jsonString)

        // Then
        assertEquals(expectedFormattedJson, formattedJson)
    }

    @Test
    fun `beautifyJsonString should handle nested JSON objects correctly`() {
        // Given
        val jsonString = """{"name":"John","address":{"city":"New York","zip":"10001"}}"""
        val expectedFormattedJson = """
            {
                "name": "John",
                "address": {
                    "city": "New York",
                    "zip": "10001"
                }
            }
        """.trimIndent()

        // When
        val formattedJson = CustomJsonBeautifier.beautifyJsonString(jsonString)

        // Then
        assertEquals(expectedFormattedJson, formattedJson)
    }

    @Test
    fun `beautifyJsonString should handle empty JSON objects correctly`() {
        // Given
        val jsonString = """{}"""
        val expectedFormattedJson = """
            {
                
            }
        """.trimIndent()

        // When
        val formattedJson = CustomJsonBeautifier.beautifyJsonString(jsonString)

        // Then
        assertEquals(expectedFormattedJson, formattedJson)
    }

    @Test
    fun `beautifyJsonString should handle empty JSON arrays correctly`() {
        // Given
        val jsonString = """[]"""
        val expectedFormattedJson = """
            [
                
            ]
        """.trimIndent()

        // When
        val formattedJson = CustomJsonBeautifier.beautifyJsonString(jsonString)

        // Then
        assertEquals(expectedFormattedJson, formattedJson)
    }

    @Test
    fun `beautifyJsonString should handle nested JSON arrays correctly`() {
        // Given
        val jsonString = """{"name":"John","phones":["123-4567","234-5678"]}"""
        val expectedFormattedJson = """
            {
                "name": "John",
                "phones": [
                    "123-4567",
                    "234-5678"
                ]
            }
        """.trimIndent()

        // When
        val formattedJson = CustomJsonBeautifier.beautifyJsonString(jsonString)

        // Then
        assertEquals(expectedFormattedJson, formattedJson)
    }
}


--------------------------

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

data class ApiError(val code: Int, val message: String)

data class ApiResponse<T>(val data: T?, val error: ApiError? = null)

class ApiResponseTest {

    @Test
    fun `ApiResponse should contain correct data`() {
        // Given
        val data = "Success"
        val response = ApiResponse(data = data)

        // When & Then
        assertEquals(data, response.data)
        assertNull(response.error)
    }

    @Test
    fun `ApiResponse should contain correct error`() {
        // Given
        val error = ApiError(code = 404, message = "Not Found")
        val response = ApiResponse<String>(data = null, error = error)

        // When & Then
        assertNull(response.data)
        assertEquals(error, response.error)
    }

    @Test
    fun `ApiResponse should contain both data and error`() {
        // Given
        val data = "Partial Success"
        val error = ApiError(code = 202, message = "Accepted with errors")
        val response = ApiResponse(data = data, error = error)

        // When & Then
        assertEquals(data, response.data)
        assertEquals(error, response.error)
    }

    @Test
    fun `ApiResponse should contain null data and error by default`() {
        // Given
        val response = ApiResponse<String>(data = null)

        // When & Then
        assertNull(response.data)
        assertNull(response.error)
    }
}


----------------------------------

import org.junit.Test
import kotlin.test.assertEquals
import java.util.UUID

class BaseInputDataTest {

    @Test
    fun `should create BaseInputData with correct data`() {
        // Given
        val baseUrl = "https://api.example.com"
        val callTimeout = 3000L
        val writeReadTimeout = 5000L
        val route = "/test"
        val headerMap = hashMapOf("Authorization" to "Bearer token")
        val queryMap = hashMapOf("query1" to "value1")
        val traceId = UUID.randomUUID().toString()

        // When
        val baseInputData = BaseInputData(
            baseUrl = baseUrl,
            callTimeout = callTimeout,
            writeReadTimeout = writeReadTimeout,
            route = route,
            headerMap = headerMap,
            queryMap = queryMap,
            traceId = traceId
        )

        // Then
        assertEquals(baseUrl, baseInputData.baseUrl)
        assertEquals(callTimeout, baseInputData.callTimeout)
        assertEquals(writeReadTimeout, baseInputData.writeReadTimeout)
        assertEquals(route, baseInputData.route)
        assertEquals(headerMap, baseInputData.headerMap)
        assertEquals(queryMap, baseInputData.queryMap)
        assertEquals(traceId, baseInputData.traceId)
    }
}
