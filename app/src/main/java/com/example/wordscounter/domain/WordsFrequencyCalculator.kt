package com.example.wordscounter.domain

import com.example.wordscounter.domain.model.WordFrequency

class WordsFrequencyCalculator {
    private val wordFrequencyMap = HashMap<String, Int>()

    fun addWords(words: List<String>) {
        words
            .map { word -> word.lowercase() }
            .forEach { word ->
                wordFrequencyMap[word] = wordFrequencyMap
                    .getOrDefault(word, 0)
                    .inc()
            }
    }

    fun getWordsFrequency() = wordFrequencyMap
        .map { (word, frequency) -> WordFrequency(word, frequency) }
}