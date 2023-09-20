package com.example.wordscounter.domain

enum class Sort(val comparator: Comparator<WordFrequency>) {
    ALPHABETICALLY(Comparator { o1, o2 -> o1.word.compareTo(o2.word, ignoreCase = true) }),
    FREQUENCY(Comparator { o1, o2 -> o2.count.compareTo(o1.count) }),
    CHAR_LENGTH(Comparator { o1, o2 -> o2.word.length.compareTo(o1.word.length) }),
}