package com.example.wordscounter.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscounter.databinding.ItemWordFrequencyBinding
import com.example.wordscounter.domain.WordFrequency

class WordsFrequencyAdapter :
    ListAdapter<WordFrequency, WordsFrequencyAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder = ItemWordFrequencyBinding
        .inflate(LayoutInflater.from(parent.context), parent, false)
        .let(::ViewHolder)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class ViewHolder(
        private val viewBinding: ItemWordFrequencyBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(item: WordFrequency) = with(viewBinding) {
            word.text = item.word
            count.text = item.count.toString()
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<WordFrequency>() {
        override fun areItemsTheSame(oldItem: WordFrequency, newItem: WordFrequency) =
            oldItem.word == newItem.word

        override fun areContentsTheSame(oldItem: WordFrequency, newItem: WordFrequency) =
            oldItem.count == newItem.count
    }
}