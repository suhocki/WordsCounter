package com.example.wordscounter.domain

enum class Sort(val comparator: Comparator<WordFrequency>) {
    ALPHABETICALLY(Comparator { book1, book2 -> book1.word.compareTo(book2.word) }),
    FREQUENCY(Comparator { book1, book2 -> book2.count.compareTo(book1.count) }),
    CHAR_LENGTH(Comparator { book1, book2 -> book2.word.length.compareTo(book1.word.length) }),
}