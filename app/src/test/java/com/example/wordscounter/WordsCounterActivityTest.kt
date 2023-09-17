package com.example.wordscounter

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric

@RunWith(AndroidJUnit4::class)
class WordsCounterActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(WordsCounterActivity::class.java)

    private val viewModel: WordsCounterViewModel = mockk()

    @Test
    fun test() {
        activityRule.scenario.moveToState(Lifecycle.State.CREATED).onActivity {

        }
    }
}