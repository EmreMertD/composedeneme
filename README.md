package com.dbssoftware.composedeneme

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.assertIsDisplayed
import org.junit.Rule
import org.junit.Test
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu

class TopBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun topBarTitleDisplaysCorrectly() {
        composeTestRule.setContent {
            TopBar(title = "Home")
        }

        composeTestRule.onNodeWithText("Home").assertIsDisplayed()
    }

    @Test
    fun navigationIconDisplaysAndClickable() {
        var clicked = false
        composeTestRule.setContent {
            TopBar(
                title = "Home",
                navigationIcon = Icons.Filled.Menu,
                navigationIconClick = { clicked = true }
            )
        }

        val navIcon = composeTestRule.onNode(hasContentDescription("Back"))
        navIcon.assertIsDisplayed()
        navIcon.performClick()
        assert(clicked)
    }
}
