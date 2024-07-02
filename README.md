import org.junit.Assert.*
import org.junit.Test

class HeaderTest {

  @Test
  fun testBuilderEmpty() {
    val header = Header.Builder().build()
    assertEquals(hashMapOf(), header.headers)
    assertFalse(header.useDefaults)
  }

  @Test
  fun testBuilderWithDefaults() {
    val header = Header.Builder().useDefaults().build()
    val expectedDefaults = hashMapOf(
      "CONTENT_TYPE" to "application/json",
      "GUID" to "[A GUID]", // Use a placeholder for dynamically generated value
      "DIALECT" to "default",
      "CHANNEL" to "default_channel",
      "ACCEPT" to "application/json",
      "TENANT_COMPANY_ID" to "default_company_id",
      "TENANT_GEO_LOCATION" to "default_geo_location",
      "IP" to "default_ip"
    )
    assertTrue(header.useDefaults)
    assertEquals(expectedDefaults, header.headers)
  }

  @Test
  fun testBuilderWithCustomHeaders() {
    val header = Header.Builder()
      .put("Authorization", "Bearer some_token")
      .put("X-Custom-Header", "custom_value")
      .build()
    assertEquals(hashMapOf(
      "Authorization" to "Bearer some_token",
      "X-Custom-Header" to "custom_value"
    ), header.headers)
    assertFalse(header.useDefaults)
  }

  @Test
  fun testBuilderWithMixedHeaders() {
    val header = Header.Builder()
      .useDefaults()
      .put("Authorization", "Bearer some_token")
      .build()
    val expectedDefaults = hashMapOf(
      "CONTENT_TYPE" to "application/json",
      "GUID" to "[A GUID]", // Use a placeholder for dynamically generated value
      "DIALECT" to "default",
      "CHANNEL" to "default_channel",
      "ACCEPT" to "application/json",
      "TENANT_COMPANY_ID" to "default_company_id",
      "TENANT_GEO_LOCATION" to "default_geo_location",
      "IP" to "default_ip",
      "Authorization" to "Bearer some_token"
    )
    assertTrue(header.useDefaults)
    assertEquals(expectedDefaults, header.headers)
  }

  @Test
  fun testPutMethod() {
    val header = Header.Builder().build()
    val modifiedHeader = header.put("X-Test-Header", "test_value")
    assertNotSame(header, modifiedHeader) // Ensure new object is returned
    assertEquals(hashMapOf("X-Test-Header" to "test_value"), modifiedHeader.headers)
  }

  @Test
  fun testPutAllMethod() {
    val header = Header.Builder().build()
    val additionalHeaders = hashMapOf("X-Test1" to "value1", "X-Test2" to "value2")
    val modifiedHeader = header.putAll(additionalHeaders)
    assertNotSame(header, modifiedHeader) // Ensure new object is returned
    assertEquals(additionalHeaders, modifiedHeader.headers)
  }

  @Test
  fun testGetMethod() {
    val header = Header.Builder().put("X-Test-Header", "test_value").build()
    assertEquals("test_value", header["X-Test-Header"])
    assertNull(header["Non-Existent-Header"])
  }

  @Test
  fun testGetAllMethod() {
    val header = Header.Builder()
      .put("X-Test1", "value1")
      .put("X-Test2", "value2")
      .build()
    assertEquals(hashMapOf("X-Test1" to "value1", "X-Test2" to "value2"), header.all)
  }
}
