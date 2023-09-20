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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelStore.put("$viewModel", viewModel)

        val viewBinding = ActivityWordsFrequencyBinding.inflate(layoutInflater)
        val wordsFrequencyAdapter = WordsFrequencyAdapter()
        var toast: Toast? = null

        setContentView(viewBinding.root)
        setSupportActionBar(viewBinding.toolbar)

        viewBinding.wordsFrequency.adapter = wordsFrequencyAdapter

        viewModel.getWordsFrequency().observeFlow(this) { wordsFrequency ->
            wordsFrequencyAdapter.submitList(wordsFrequency, viewModel::onListAnimationCompleted)
        }

        viewModel.isProgress().observeFlow(this) { isProgress ->
            viewBinding.progress.isVisible = isProgress
            if (!isProgress) {
                viewBinding.wordsFrequency.scrollToPosition(0)
            }
        }

        viewModel.getInfoMessage().observeFlow(this) { message ->
            toast?.cancel()
            toast = Toast
                .makeText(this, message, Toast.LENGTH_SHORT)
                .apply { show() }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_words_frequency, menu)

        viewModel.getSortType().observeFlow(this) { sortType ->
            menu.forEach { menuItem ->
                menuItem.isVisible = menuItem.itemId == sortType.menuId
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.changeSort()
        return true
    }
}