package com.example.wordscounter.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.core.view.isVisible
import com.example.wordscounter.R
import com.example.wordscounter.databinding.ActivityWordsFrequencyBinding


class WordsFrequencyActivity(
    private val viewModel: WordsFrequencyViewModel,
) : AppCompatActivity() {
    private var optionsMenu: Menu? = null
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityWordsFrequencyBinding.inflate(layoutInflater)
        val wordsFrequencyAdapter = WordsFrequencyAdapter()

        setContentView(viewBinding.root)
        setSupportActionBar(viewBinding.toolbar)

        viewBinding.wordsFrequency.adapter = wordsFrequencyAdapter

        viewModel.getWordsFrequency().observeFlow(this) { wordsFrequency ->
            wordsFrequencyAdapter.submitList(wordsFrequency, viewModel::onListAnimationCompleted)
        }

        viewModel.getSortType().observeFlow(this) { sortType ->
            optionsMenu?.forEach { menuItem ->
                menuItem.isVisible = menuItem.itemId == sortType?.menuId
                if (menuItem.isVisible) {
                    menuItem.title?.toString()?.let(::showToast)
                }
            }
        }

        viewModel.isProgress().observeFlow(this) { isProgress ->
            viewBinding.progress.isVisible = isProgress
            if (!isProgress) {
                viewBinding.wordsFrequency.scrollToPosition(0)
            }
        }
    }

    private fun showToast(message: String) {
        toast?.cancel()
        toast = Toast
            .makeText(this, message, Toast.LENGTH_SHORT)
            .apply { show() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        optionsMenu = menu
        menuInflater.inflate(R.menu.menu_words_frequency, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.changeSort()
        return true
    }
}