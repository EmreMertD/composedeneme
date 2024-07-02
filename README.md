import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class HeaderTest {

    @Test
    fun `test Header put function`() {
        val header = Header.Builder()
            .put("key1", "value1")
            .put("key2", "value2")
            .build()

        assertEquals("value1", header["key1"])
        assertEquals("value2", header["key2"])
    }

    @Test
    fun `test Header putAll function`() {
        val headersMap = hashMapOf("key1" to "value1", "key2" to "value2")
        val header = Header.Builder()
            .putAll(headersMap)
            .build()

        assertEquals("value1", header["key1"])
        assertEquals("value2", header["key2"])
    }

    @Test
    fun `test Header useDefaults function`() {
        val header = Header.Builder()
            .useDefaults()
            .build()

        assertNotNull(header["CONTENT_TYPE"])
        assertNotNull(header["GUID"])
        assertNotNull(header["DIALECT"])
        assertNotNull(header["CHANNEL"])
        assertNotNull(header["ACCEPT"])
        assertNotNull(header["TENANT_COMPANY_ID"])
        assertNotNull(header["TENANT_GEO_LOCATION"])
        assertNotNull(header["IP"])
    }

    @Test
    fun `test Header putIfAbsent function`() {
        val header = Header.Builder()
            .put("key1", "value1")
            .putIfAbsent("key1", "newValue1")
            .putIfAbsent("key2", "value2")
            .build()

        assertEquals("value1", header["key1"]) // should not be replaced
        assertEquals("value2", header["key2"]) // should be added
    }

    @Test
    fun `test Header get function`() {
        val header = Header.Builder()
            .put("key1", "value1")
            .build()

        assertEquals("value1", header["key1"])
        assertNull(header["key2"])
    }
}
