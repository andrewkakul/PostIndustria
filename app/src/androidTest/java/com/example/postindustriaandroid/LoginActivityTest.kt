package com.example.postindustriaandroid

import android.app.Activity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.example.postindustriaandroid.ui.BaseActivity
import com.example.postindustriaandroid.ui.LogInActivity
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

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
        assertEquals(getActivityInstance()!!.javaClass, BaseActivity::class.java)
    }

    @Test
    fun start_without_login(){
        onView(withId(R.id.login_layout))
            .check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.logIn_btn)).perform(click())
        assertEquals(getActivityInstance()!!.javaClass, LogInActivity::class.java)
    }

    private fun getActivityInstance(): Activity? {
        val currentActivity = arrayOf<Activity?>(null)
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val resumedActivities: Collection<*> =
                ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
            if (resumedActivities.iterator().hasNext()) {
                currentActivity[0] = resumedActivities.iterator().next() as Activity?
            }
        }
        return currentActivity[0]
    }
}