import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class HeaderTest {

    @Test
    fun `test builder with defaults`() {
        val header = Header.Builder()
            .useDefaults()
            .build()

        assertEquals(HeaderConstants.CONTENT_TYPE_JSON_VAL, header[HeaderConstants.CONTENT_TYPE])
        assertNotNull(header[HeaderConstants.GUID])
        assertEquals(HeaderConstants.DIALECT_VAL, header[HeaderConstants.DIALECT])
        assertEquals(HeaderConstants.DEFAULT_CHANNEL, header[HeaderConstants.CHANNEL])
        assertEquals(HeaderConstants.CONTENT_TYPE_JSON_VAL, header[HeaderConstants.ACCEPT])
        assertEquals(HeaderConstants.DEFAULT_TENANT_COMPANY_ID, header[HeaderConstants.TENANT_COMPANY_ID])
        assertEquals(HeaderConstants.DEFAULT_TENANT_GEO_LOCATION, header[HeaderConstants.TENANT_GEO_LOCATION])
        assertEquals("127.0.0.1", header[HeaderConstants.IP])
    }

    @Test
    fun `test builder add header`() {
        val header = Header.Builder()
            .add("Custom-Header", "CustomValue")
            .build()

        assertEquals("CustomValue", header["Custom-Header"])
    }

    @Test
    fun `test builder add all headers`() {
        val headersToAdd = hashMapOf("Header1" to "Value1", "Header2" to "Value2")
        val header = Header.Builder()
            .addAll(headersToAdd)
            .build()

        assertEquals("Value1", header["Header1"])
        assertEquals("Value2", header["Header2"])
    }

    @Test
    fun `test header put`() {
        val header = Header(hashMapOf(), false)
            .put("New-Header", "NewValue")

        assertEquals("NewValue", header["New-Header"])
    }

    @Test
    fun `test header put all`() {
        val initialHeaders = hashMapOf("Initial-Header" to "InitialValue")
        val headersToAdd = hashMapOf("Header1" to "Value1", "Header2" to "Value2")
        val header = Header(initialHeaders, false)
            .putAll(headersToAdd)

        assertEquals("InitialValue", header["Initial-Header"])
        assertEquals("Value1", header["Header1"])
        assertEquals("Value2", header["Header2"])
    }

    @Test
    fun `test header get all`() {
        val headers = hashMapOf("Header1" to "Value1", "Header2" to "Value2")
        val header = Header(headers, false)

        assertEquals(headers, header.all)
    }
}
