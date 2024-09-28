package org.hyperskill.secretdiary

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class MainActivity : AppCompatActivity() {

    companion object {
        const val PREF_DIARY = "PREF_DIARY"
        const val KEY_DIARY_TEXT = "KEY_DIARY_TEXT"
    }

    private lateinit var sharedPrefs: SharedPreferences

    private lateinit var etNewWriting: EditText
    private lateinit var btnSave: Button
    private lateinit var btnUndo: Button
    private lateinit var tvDiary: TextView

    private lateinit var notes: String
    private val separator = "\n\n"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPrefs = getSharedPreferences(PREF_DIARY, Context.MODE_PRIVATE)
        notes = sharedPrefs.getString(KEY_DIARY_TEXT, "").toString()

        etNewWriting = findViewById(R.id.etNewWriting)
        btnSave = findViewById(R.id.btnSave)
        btnUndo = findViewById(R.id.btnUndo)
        tvDiary = findViewById(R.id.tvDiary)
        tvDiary.text = notes

        btnSave.setOnClickListener {
            val input = etNewWriting.text
            if (input.trim().isEmpty()) {
                Toast.makeText(this, R.string.input_empty, Toast.LENGTH_SHORT).show()
            } else {
                val current = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                if (notes.isNotEmpty()) {
                    notes = "$separator$notes"
                }
                notes = "${current.date} ${current.time.toString().subSequence(0, 8)}\n$input$notes"
                etNewWriting.text.clear()
                saveNotes()
            }
        }

        btnUndo.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_title))
                .setMessage(getString(R.string.dialog_message))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    if (notes.isNotEmpty()) {
                        notes = if (notes.contains(separator)) {
                            notes.substring(notes.indexOf(separator) + separator.length)
                        } else {
                            ""
                        }
                        saveNotes()
                    }
                }
                .setNegativeButton(getString(R.string.no), null)
                .show()
        }

        /*
            Tests for android can not guarantee the correctness of solutions that make use of
            mutation on "static" variables to keep state. You should avoid using those.
            Consider "static" as being anything on kotlin that is transpiled to java
            into a static variable. That includes global variables and variables inside
            singletons declared with keyword object, including companion object.
            This limitation is related to the use of JUnit on tests. JUnit re-instantiate all
            instance variable for each test method, but it does not re-instantiate static variables.
            The use of static variable to hold state can lead to state from one test to spill over
            to another test and cause unexpected results.
            Using mutation on static variables to keep state
            is considered a bad practice anyway and no measure
            attempting to give support to that pattern will be made.
         */
    }

    private fun saveNotes() {
        tvDiary.text = notes
        sharedPrefs.edit().putString(KEY_DIARY_TEXT, notes).apply()
    }
}