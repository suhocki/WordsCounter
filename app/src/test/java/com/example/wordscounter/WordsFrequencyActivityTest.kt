package com.example.wordscounter

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wordscounter.domain.model.Sort
import com.example.wordscounter.domain.model.WordFrequency
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
    fun `words are bounded`() = launchActivity {
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
    fun `progress is shown`() {
        every { viewModel.isProgress() } returns MutableStateFlow(true)

        launchActivity {
            onView(withId(R.id.progress)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun `sort button is shown`() {
        every { viewModel.getSortType() } returns MutableStateFlow(Sort.CHAR_LENGTH)

        launchActivity {
            onView(withId(R.id.sort_by_char_length)).check(matches(isDisplayed()))
            onView(withId(R.id.sort_by_frequency)).check(doesNotExist())
            onView(withId(R.id.sort_alphabetically)).check(doesNotExist())
        }
    }

    @Test
    fun `viewModel is notified on sort click`() {
        every { viewModel.getSortType() } returns MutableStateFlow(Sort.CHAR_LENGTH)

        launchActivity {
            onView(withId(R.id.sort_by_char_length)).perform(click())

            verify { viewModel.changeSort() }
        }
    }

    @Test
    fun `viewModel is notified on list animation completed`() {
        launchActivity {
            verify { viewModel.onListAnimationCompleted() }
        }
    }

    @Test
    fun `viewModelStore is initialised`() {
        launchActivity {
            activity.viewModelStore.keys()
                .map(activity.viewModelStore::get)
                .contains(viewModel)
        }
    }

    private fun launchActivity(block: () -> Unit) = ActivityScenario
        .launchActivityForResult(WordsFrequencyActivity::class.java)
        .moveToState(Lifecycle.State.RESUMED)
        .onActivity { block() }
        .close()
}