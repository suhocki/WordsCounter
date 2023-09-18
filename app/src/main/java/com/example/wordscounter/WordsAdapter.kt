package com.example.wordscounter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscounter.databinding.ItemWordBinding

class WordsAdapter : ListAdapter<Word, WordsAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder = ItemWordBinding
        .inflate(LayoutInflater.from(parent.context), parent, false)
        .let(::ViewHolder)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ViewHolder(
        private val viewBinding: ItemWordBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(item: Word) = with(viewBinding) {
            word.text = item.word
            count.text = item.count.toString()
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Word>() {
        override fun areItemsTheSame(oldItem: Word, newItem: Word) =
            oldItem.word == newItem.word

        override fun areContentsTheSame(oldItem: Word, newItem: Word) =
            oldItem.count == newItem.count
    }
}