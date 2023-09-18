package com.example.wordscounter

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
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
class WordsActivityTest {

    private val viewModel: WordsViewModel = mockk()

    private val words = (0 until 50).map { i -> Word("word #$i", i) }
    private val activity = WordsActivity(viewModel)

    @Before
    fun setUp() {
        mockkStatic(ReflectionHelpers::class)

        every { ReflectionHelpers.callConstructor(WordsActivity::class.java) } returns activity
        every { viewModel.words } returns MutableStateFlow(words)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `check if words are binding correctly`() = launchActivity {
        words.forEach { word ->
            val checkItem = actionOnItem<WordsAdapter.ViewHolder>(
                allOf(
                    hasDescendant(withText(word.word)),
                    hasDescendant(withText(word.count.toString()))
                ),
                scrollTo()
            )

            onView(withId(R.id.words)).perform(checkItem)
        }
    }

    private fun launchActivity(action: () -> Unit) = ActivityScenario
        .launchActivityForResult(WordsActivity::class.java)
        .moveToState(Lifecycle.State.RESUMED)
        .onActivity {
            action()
        }.close()
}