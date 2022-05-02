package com.example.storey.ui.activity

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.storey.R
import com.example.storey.helper.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityEndTest {

    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun loadStories() {
        onView(withId(R.id.rv)).check(matches(isDisplayed()))
        onView(withId(R.id.rv)).perform(ViewActions.swipeUp(),
            ViewActions.swipeUp(),
            ViewActions.swipeUp(),
            ViewActions.swipeDown(),
            ViewActions.swipeDown(),
            ViewActions.swipeDown())
    }

    @Test
    fun openAllStoriesMaps() {
        Intents.init()
        onView(withId(R.id.open_maps)).perform(click())
        intended(hasComponent(MapsActivity::class.java.name))
        Intents.release()
    }

    @Test
    fun loadDetailStory() {
        Intents.init()
        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
            0,
            click()))
        intended(hasComponent(DetailActivity::class.java.name))
        Intents.release()
    }



}