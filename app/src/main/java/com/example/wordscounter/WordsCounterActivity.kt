package com.example.wordscounter

import android.content.res.AssetManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset

class WordsCounterActivity(
    private val viewModel: WordsCounterViewModel,
) : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_words_counter)
        readAndProcessTextFile("Romeo-and-Juliet.txt")
    }

    private fun readAndProcessTextFile(fileName: String): HashMap<String, Int> {
        val wordFrequencyMap = HashMap<String, Int>()
        try {
            val assetManager: AssetManager = assets

            val t = SystemClock.elapsedRealtime()
            val (charset) = Charset.availableCharsets().values.associateWith { charset ->
                val lines = InputStreamReader(assetManager.open(fileName), charset).readLines()
                lines.count("^[0-9a-zA-Z[:punct:]\\s]+$".toRegex()::matches)
            }.maxBy { (_, validLines) -> validLines }

            Log.d("mytag", (SystemClock.elapsedRealtime() - t).toString())
            val inputStreamReader = InputStreamReader(assetManager.open(fileName), charset)
            val bufferedReader = BufferedReader(inputStreamReader)

            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                // Split the line into words by spaces and punctuation
                val words = Regex("((\\b[^\\s]+\\b)((?<=\\.\\w).)?)").findAll(line.orEmpty()).toList().map { it.value }

                // Count word occurrences
                words.forEach { word ->
                    val lowercaseWord = word.toLowerCase()
                    val count = wordFrequencyMap.getOrDefault(lowercaseWord, 0)
                    wordFrequencyMap[lowercaseWord] = count + 1
                }
            }

            // Close the streams
            bufferedReader.close()
            inputStreamReader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return wordFrequencyMap
    }
}