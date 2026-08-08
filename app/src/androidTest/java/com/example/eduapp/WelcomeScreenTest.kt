package com.example.eduapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

/**
 * GUI tests. These run on a device or emulator and drive the real UI,
 * unlike the model tests which run on the JVM.
 */
class WelcomeScreenTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun blankNameShowsAnError() {
        rule.onNodeWithText("Start playing").performClick()
        rule.onNodeWithText("Please enter a name").assertIsDisplayed()
    }

    @Test
    fun theNameFieldAcceptsTyping() {
        rule.onNodeWithText("What should we call you?").performTextInput("Testy")
        rule.onNodeWithText("Testy").assertIsDisplayed()
    }

    @Test
    fun thePrivacyNoticeIsVisible() {
        rule.onNodeWithText(
            "Your name is only stored on this device. Nothing is sent anywhere."
        ).assertIsDisplayed()
    }
}
