package com.example.wordscounter.domain

class WordsFrequencyCalculator {
    private val wordFrequencyMap = HashMap<String, Int>()

    fun addWords(words: List<String>) {
        words.forEach { word ->
            val lowercaseWord = word.lowercase()
            val count = wordFrequencyMap.getOrDefault(lowercaseWord, 0)
            wordFrequencyMap[lowercaseWord] = count + 1
        }
    }

    fun getWordsFrequency() = wordFrequencyMap
        .map { (word, frequency) -> WordFrequency(word, frequency) }
}