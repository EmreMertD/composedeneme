import org.junit.Test
import kotlin.test.assertEquals

class CallCenterCallResultTest {

    @Test
    fun `should create CallCenterCallResult with correct data`() {
        // Given
        val result = Result(returnCode = 200L, reasonCode = 100L, messageText = "Success")
        val campaignResponseList = listOf(
            CampaignResponseList(responesNum = 1L, followUpCode = 101L, responseNameText = "Campaign 1"),
            CampaignResponseList(responesNum = 2L, followUpCode = 102L, responseNameText = "Campaign 2")
        )

        // When
        val callCenterCallResult = CallCenterCallResult(campaignResponseList = campaignResponseList, result = result)

        // Then
        assertEquals(result, callCenterCallResult.result)
        assertEquals(campaignResponseList, callCenterCallResult.campaignResponseList)
    }

    @Test
    fun `should create CampaignResponseList with correct data`() {
        // Given
        val responesNum = 1L
        val followUpCode = 101L
        val responseNameText = "Campaign 1"

        // When
        val campaignResponseList = CampaignResponseList(responesNum = responesNum, followUpCode = followUpCode, responseNameText = responseNameText)

        // Then
        assertEquals(responesNum, campaignResponseList.responesNum)
        assertEquals(followUpCode, campaignResponseList.followUpCode)
        assertEquals(responseNameText, campaignResponseList.responseNameText)
    }

    @Test
    fun `should create Result with correct data`() {
        // Given
        val returnCode = 200L
        val reasonCode = 100L
        val messageText = "Success"

        // When
        val result = Result(returnCode = returnCode, reasonCode = reasonCode, messageText = messageText)

        // Then
        assertEquals(returnCode, result.returnCode)
        assertEquals(reasonCode, result.reasonCode)
        assertEquals(messageText, result.messageText)
    }
}
