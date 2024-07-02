
import org.junit.Test
import kotlin.test.assertEquals

class CallCenterCallResultTest {

    @Test
    fun testCallCenterCallResult() {
        val campaignResponseList = listOf(
            CampaignResponseList(1L, 2L, "Response 1"),
            CampaignResponseList(3L, 4L, "Response 2")
        )

        val result = Result(0L, 1L, "Success")

        val callCenterCallResult = CallCenterCallResult(campaignResponseList, result)

        // Verify campaign response list
        assertEquals(2, callCenterCallResult.campaignResponseList.size)
        assertEquals(1L, callCenterCallResult.campaignResponseList[0].responseNum)
        assertEquals(2L, callCenterCallResult.campaignResponseList[0].followUpCode)
        assertEquals("Response 1", callCenterCallResult.campaignResponseList[0].responseNameText)
        assertEquals(3L, callCenterCallResult.campaignResponseList[1].responseNum)
        assertEquals(4L, callCenterCallResult.campaignResponseList[1].followUpCode)
        assertEquals("Response 2", callCenterCallResult.campaignResponseList[1].responseNameText)

        // Verify result
        assertEquals(0L, callCenterCallResult.result.returnCode)
        assertEquals(1L, callCenterCallResult.result.reasonCode)
        assertEquals("Success", callCenterCallResult.result.messageText)
    }
}


-----------------

import org.junit.Test
import kotlin.test.assertEquals

class ContactDataResponseTest {

    @Test
    fun testContactDataResponse() {
        val contactInfo1 = ContactInfo(
            contactDetailType = "Mobile",
            countryCode = "US",
            number = "1234567890",
            receivesNotifications = true
        )

        val contactInfo2 = ContactInfo(
            contactDetailType = "Home",
            countryCode = "US",
            number = "0987654321",
            receivesNotifications = false
        )

        val contactDataOutput1 = ContactDataOutput(
            id = "1",
            isPreferential = true,
            contact = contactInfo1
        )

        val contactDataOutput2 = ContactDataOutput(
            id = "2",
            isPreferential = false,
            contact = contactInfo2
        )

        val contactDataResponse = ContactDataResponse(
            data = listOf(contactDataOutput1, contactDataOutput2)
        )

        // Verify contact data response
        assertEquals(2, contactDataResponse.data.size)

        // Verify first contact data output
        val output1 = contactDataResponse.data[0]
        assertEquals("1", output1.id)
        assertEquals(true, output1.isPreferential)
        assertEquals("Mobile", output1.contact.contactDetailType)
        assertEquals("US", output1.contact.countryCode)
        assertEquals("1234567890", output1.contact.number)
        assertEquals(true, output1.contact.receivesNotifications)

        // Verify second contact data output
        val output2 = contactDataResponse.data[1]
        assertEquals("2", output2.id)
        assertEquals(false, output2.isPreferential)
        assertEquals("Home", output2.contact.contactDetailType)
        assertEquals("US", output2.contact.countryCode)
        assertEquals("0987654321", output2.contact.number)
        assertEquals(false, output2.contact.receivesNotifications)
    }
}

