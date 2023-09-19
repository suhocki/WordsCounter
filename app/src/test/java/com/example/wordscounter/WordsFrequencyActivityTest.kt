package com.example.wordscounter

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wordscounter.domain.Sort
import com.example.wordscounter.domain.WordFrequency
import com.example.wordscounter.ui.WordsFrequencyActivity
import com.example.wordscounter.ui.WordsFrequencyAdapter
import com.example.wordscounter.ui.WordsFrequencyViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
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
    fun `check if words are bounded`() = launchActivity {
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

    @Test
    fun `check if progress is shown`() {
        every { viewModel.isProgress() } returns MutableStateFlow(true)

        launchActivity {
            onView(withId(R.id.progress)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun `check if sort button is shown`() {
        every { viewModel.getSortType() } returns MutableStateFlow(Sort.ALPHABETICALLY)

        launchActivity {
            onView(withId(R.id.sort_alphabetically)).check(matches(isDisplayed()))
            onView(withId(R.id.sort_by_frequency)).check(doesNotExist())
            onView(withId(R.id.sort_by_char_length)).check(doesNotExist())
        }
    }

    @Test
    fun `check viewModel is invoked on sort click`() {
        every { viewModel.getSortType() } returns MutableStateFlow(Sort.CHAR_LENGTH)

        launchActivity {
            onView(withId(R.id.sort_by_char_length)).perform(click())

            verify { viewModel.changeSort() }
        }
    }

    @Test
    fun `check viewModel is invoked on list animation completed`() {
        launchActivity {
            verify { viewModel.onListAnimationCompleted() }
        }
    }

    private fun launchActivity(block: () -> Unit) = ActivityScenario
        .launchActivityForResult(WordsFrequencyActivity::class.java)
        .moveToState(Lifecycle.State.RESUMED)
        .onActivity { block() }
        .close()
}