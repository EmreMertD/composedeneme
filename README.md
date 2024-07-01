package com.dbssoftware.composedeneme

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TopBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun topBar_displaysTitle() {
        val title = "Test Title"
        composeTestRule.setContent {
            TopBar(title = title)
        }
        composeTestRule.onNodeWithText(title).assertIsDisplayed()
    }

    @Test
    fun topBar_navigateIconDisplayedAndClickable() {
        val title = "Test Title"
        var clickCount = 0
        val onClick = { clickCount++ }

        composeTestRule.setContent {
            TopBar(
                title = title,
                navigationIcon = Icons.Default.ArrowBack,
                navigationIconClick = onClick
            )
        }

        // Check that the navigation icon is displayed
        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()

        // Perform click and check that the click count is incremented
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        assert(clickCount == 1)
    }

    @Test
    fun topBar_noNavigationIcon() {
        val title = "Test Title"
        composeTestRule.setContent {
            TopBar(title = title)
        }

        // Check that the navigation icon is not displayed
        composeTestRule.onNodeWithContentDescription("Back").assertDoesNotExist()
    }
}

@Composable
fun TopBar(
    title: String,
    navigationIcon: ImageVector? = null,
    navigationIconClick: (() -> Unit)? = null
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = if (navigationIcon != null && navigationIconClick != null) {
            {
                IconButton(onClick = navigationIconClick) {
                    Icon(navigationIcon, contentDescription = "Back")
                }
            }
        } else null
    )
}


dependencies {
    implementation "androidx.compose.ui:ui:1.4.2"
    implementation "androidx.compose.ui:ui-tooling-preview:1.4.2"
    implementation "androidx.compose.material:material:1.4.2"
    testImplementation "androidx.test.ext:junit:1.1.5"
    androidTestImplementation "androidx.test.ext:junit:1.1.5"
    androidTestImplementation "androidx.test.espresso:espresso-core:3.5.1"
    androidTestImplementation "androidx.compose.ui:ui-test-junit4:1.4.2"
    debugImplementation "androidx.compose.ui:ui-tooling:1.4.2"
    debugImplementation "androidx.compose.ui:ui-test-manifest:1.4.2"
}
