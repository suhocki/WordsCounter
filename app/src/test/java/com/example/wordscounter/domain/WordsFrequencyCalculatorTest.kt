package com.example.wordscounter.domain

import com.example.wordscounter.domain.model.WordFrequency
import org.junit.Assert.assertEquals
import org.junit.Test

class WordsFrequencyCalculatorTest {
    private val calculator = WordsFrequencyCalculator()

    @Test
    fun `get words frequency`() {
        calculator.addWords(listOf("a", "A"))
        calculator.addWords(listOf("a", "aA", "Ab"))
        calculator.addWords(listOf("aB"))

        assertEquals(
            setOf(
                WordFrequency("a", 3),
                WordFrequency("aa", 1),
                WordFrequency("ab", 2),
            ),
            calculator.getWordsFrequency().toSet()
        )
    }
}