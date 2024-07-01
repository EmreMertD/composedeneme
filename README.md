import org.junit.Test
import kotlin.test.assertEquals

class ContactDataResponseTest {

    @Test
    fun `should create ContactDataOutput with correct data`() {
        // Given
        val contactInfo = ContactInfo(
            contactDetailType = "Phone",
            countryCode = "1",
            number = "1234567890",
            receivesNotifications = true
        )
        val contactDataOutput = ContactDataOutput(
            id = "123",
            isPreferential = true,
            contact = contactInfo
        )

        // When
        val expectedId = "123"
        val expectedIsPreferential = true
        val expectedContactInfo = contactInfo

        // Then
        assertEquals(expectedId, contactDataOutput.id)
        assertEquals(expectedIsPreferential, contactDataOutput.isPreferential)
        assertEquals(expectedContactInfo, contactDataOutput.contact)
    }

    @Test
    fun `should create ContactDataResponse with correct data`() {
        // Given
        val contactInfo1 = ContactInfo(
            contactDetailType = "Phone",
            countryCode = "1",
            number = "1234567890",
            receivesNotifications = true
        )
        val contactDataOutput1 = ContactDataOutput(
            id = "123",
            isPreferential = true,
            contact = contactInfo1
        )

        val contactInfo2 = ContactInfo(
            contactDetailType = "Email",
            countryCode = "1",
            number = "example@example.com",
            receivesNotifications = false
        )
        val contactDataOutput2 = ContactDataOutput(
            id = "124",
            isPreferential = false,
            contact = contactInfo2
        )

        val contactDataResponse = ContactDataResponse(
            data = listOf(contactDataOutput1, contactDataOutput2)
        )

        // When
        val expectedData = listOf(contactDataOutput1, contactDataOutput2)

        // Then
        assertEquals(expectedData, contactDataResponse.data)
    }

    @Test
    fun `should create ContactInfo with correct data`() {
        // Given
        val contactInfo = ContactInfo(
            contactDetailType = "Phone",
            countryCode = "1",
            number = "1234567890",
            receivesNotifications = true
        )

        // When
        val expectedContactDetailType = "Phone"
        val expectedCountryCode = "1"
        val expectedNumber = "1234567890"
        val expectedReceivesNotifications = true

        // Then
        assertEquals(expectedContactDetailType, contactInfo.contactDetailType)
        assertEquals(expectedCountryCode, contactInfo.countryCode)
        assertEquals(expectedNumber, contactInfo.number)
        assertEquals(expectedReceivesNotifications, contactInfo.receivesNotifications)
    }
}
