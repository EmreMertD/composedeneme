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
