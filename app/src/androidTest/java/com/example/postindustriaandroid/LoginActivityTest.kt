package com.example.postindustriaandroid

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.postindustriaandroid.ui.LogInActivity
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    private lateinit var login: String

    @Before
    fun setUp() {
        ActivityScenario.launch(LogInActivity::class.java)
        login = "TestLogin"
    }

    @Test
    fun start_with_login(){
        onView(withId(R.id.login_layout))
            .check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.loIn_ET)).perform(typeText(login))
        onView(withId(R.id.logIn_btn)).perform(click())
    }

    @Test
    fun start_without_login(){
        onView(withId(R.id.login_layout))
            .check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.logIn_btn)).perform(click())
    }
}