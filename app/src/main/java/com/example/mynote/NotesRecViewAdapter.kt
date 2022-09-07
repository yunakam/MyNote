package com.example.mynote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mynote.databinding.NoteListItemBinding

class NotesRecViewAdapter : ListAdapter<Note, NotesRecViewAdapter.NoteHolder>(DiffCallback()) {

    private lateinit var listener: RecyclerClickListener
    fun setItemListener(listener: RecyclerClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NoteListItemBinding.inflate(inflater, parent, false)

//        val noteDelete = noteHolder.itemView.findViewById<ImageView>(R.id.note_delete)
//        noteDelete.setOnClickListener {
//            listener.onItemRemoveClick(noteHolder.adapterPosition)
//        }
//
//        val note = noteHolder.itemView.findViewById<CardView>(R.id.note)
//        note.setOnClickListener {
//            listener.onItemClick(noteHolder.adapterPosition)
//        }

        return NoteHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
//        val currentItem = getItem(position)
//        val noteText = holder.itemView.findViewById<TextView>(R.id.note_text)
//        noteText.text = currentItem.noteText
        holder.bind(getItem(position))
    }

    inner class NoteHolder(
        private val binding: NoteListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Note) {
            binding.txtNote.text = item.noteText.toString()
            binding.txtSource.text = item.source.toString()
            binding.txtDate.text = item.dateAdded.toString()

            // Click the card to edit
            binding.noteCard.setOnClickListener {
                listener.onItemClick(this.bindingAdapterPosition)
            }

            // Delete button
            binding.noteDelete.setOnClickListener {
                listener.onItemRemoveClick(this.bindingAdapterPosition)
            }
        }
    }


    class DiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note) =
            oldItem.dateAdded == newItem.dateAdded

        override fun areContentsTheSame(oldItem: Note, newItem: Note) =
            oldItem == newItem
    }
}