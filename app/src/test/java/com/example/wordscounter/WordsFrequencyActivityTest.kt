package com.example.wordscounter

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wordscounter.domain.WordFrequency
import com.example.wordscounter.ui.WordsFrequencyActivity
import com.example.wordscounter.ui.WordsFrequencyAdapter
import com.example.wordscounter.ui.WordsFrequencyViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.MutableStateFlow
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.util.ReflectionHelpers

@RunWith(AndroidJUnit4::class)
class WordsFrequencyActivityTest {

    private val viewModel: WordsFrequencyViewModel = mockk(relaxed = true)

    private val words = (0 until 50).map { i -> WordFrequency("word #$i", i) }
    private val activity = WordsFrequencyActivity(viewModel)

    @Before
    fun setUp() {
        mockkStatic(ReflectionHelpers::class)

        every { ReflectionHelpers.callConstructor(WordsFrequencyActivity::class.java) } returns activity
        every { viewModel.getWordsFrequency() } returns MutableStateFlow(words)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `check if words are binding correctly`() = launchActivity {
        words.forEach { word ->
            val checkItem = actionOnItem<WordsFrequencyAdapter.ViewHolder>(
                allOf(
                    hasDescendant(withText(word.word)),
                    hasDescendant(withText(word.count.toString()))
                ),
                scrollTo()
            )

            onView(withId(R.id.words_frequency)).perform(checkItem)
        }
    }

    private fun launchActivity(block: () -> Unit) = ActivityScenario
        .launchActivityForResult(WordsFrequencyActivity::class.java)
        .moveToState(Lifecycle.State.RESUMED)
        .onActivity { block() }
        .close()
}