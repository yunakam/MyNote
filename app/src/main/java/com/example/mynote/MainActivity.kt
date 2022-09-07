package com.example.mynote

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynote.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val TAG = "MainActivity"
        private const val CREATE_NEW_NOTE = 1000
    }

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { NotesRecViewAdapter() }
    private val noteDatabase by lazy { NoteDatabase.getDatabase(this).noteDao() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRecyclerView()

        binding.btnCreateNote.setOnClickListener(this)
        observeNotes()
    }

    private fun setRecyclerView() {
        binding.rvNote.layoutManager = LinearLayoutManager(this)

        adapter.setItemListener(object : RecyclerClickListener {

            // Tap the 'X' to delete the note.
            override fun onItemRemoveClick(position: Int) {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setMessage("Are you sure you want to delete?")
                    .setCancelable(false)
                    .setPositiveButton("YES") { _, _ ->
                        val noteList = adapter.currentList.toMutableList()
                        val id = noteList[position].id
                        val noteText = noteList[position].noteText
                        val source = noteList[position].source
                        val dateAdded = noteList[position].dateAdded
                        val tag = noteList[position].tag

                        val noteToRemove = Note(id, noteText, source, dateAdded, tag)
                        lifecycleScope.launch {
                            noteDatabase.deleteNote(noteToRemove)
                        }
                    }
                    .setNegativeButton("NO") { dialog, _ ->
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }

// before Room
//                // Get the list of notes
//                val notes = adapter.currentList.toMutableList()
//                notes.removeAt(position)
//                // Update RecyclerView
//                adapter.submitList(notes)

            // Tap the note to edit.
            override fun onItemClick(position: Int) {
                // Get the list of notes
                val notes = adapter.currentList.toMutableList()
                val intent = Intent(this@MainActivity, AddNoteActivity::class.java)
                intent.putExtra("id", notes[position].id)
                intent.putExtra("note_text", notes[position].noteText)
                intent.putExtra("source", notes[position].source)
                intent.putExtra("tag", notes[position].tag)

                editNoteResultLauncher.launch(intent)
            }
        })

        binding.rvNote.adapter = adapter
    }

    // Room: executed everytime an item changes in the list
    private fun observeNotes() {
        lifecycleScope.launch {
            noteDatabase.getNotes().collect { noteList ->
                if (noteList.isNotEmpty()) {
                    adapter.submitList(noteList)
                }
            }
        }
    }

    private val newNoteResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

//  Before introducing Room database
//                // Get the list of notes
//                val notes = adapter.currentList.toMutableList()
                // Get the new note from the AddNoteActivity
                val id = UUID.randomUUID().toString()
                val noteDateAdded = LocalDateTime.now()
                val noteText = result.data?.getStringExtra("note_text")
                val source = result.data?.getStringExtra("source")
                val tag = result.data?.getStringExtra("tag")
                // Add the new note at the top of the list
                val newNote = Note(id, noteText, source, noteDateAdded, tag)
//                notes.add(newNote)
//                // Update RecyclerView
//                adapter.submitList(notes)
                lifecycleScope.launch {
                    noteDatabase.addNote(newNote)
                }
            }
        }

    private val editNoteResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
//                // Get the list of notes
//                val notes = adapter.currentList.toMutableList()
                // Get the edited note from the AddNoteActivity
                val id = result.data?.getStringExtra("id")
                val editedNoteText = result.data?.getStringExtra("note_text")
                val editedSource = result.data?.getStringExtra("source")
                val noteDateAEdited = LocalDateTime.now()
                val editedTag = result.data?.getStringExtra("tag")

                val editedNote = Note(id!!, editedNoteText, editedSource, noteDateAEdited, editedTag)
                lifecycleScope.launch {
                    noteDatabase.updateNote(editedNote)
//                for (note in notes) {
//                    if (note.id == id) {
//                        note.noteText = editedNoteText ?: ""
//                        note.source = editedSource ?: ""
//                        note.dateAdded = LocalDateTime.now()
//                        note.tag = editedTag ?: ""
//
//                    }
//                }
//                // Update RecyclerView
//                adapter.submitList(notes)
//                adapter.notifyDataSetChanged()
                }
            }
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_note_menu_item) {
            val intent = Intent(this, AddNoteActivity::class.java)
            newNoteResultLauncher.launch(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        val intent = Intent(this, AddNoteActivity::class.java)
        newNoteResultLauncher.launch(intent)
    }
}
