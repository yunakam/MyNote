package com.example.mynote

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
import com.example.mynote.databinding.ActivityAddNoteBinding
import java.util.*

@SuppressLint("RestrictedApi")
class AddNoteActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "AddNoteActivity"
    }

    private lateinit var binding: ActivityAddNoteBinding

//    private lateinit var addNoteBackground: RelativeLayout
//    private lateinit var addNoteWindowBg: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.i(TAG, "AddNoteActivity launched")

//        addNoteBackground = findViewById(R.id.add_note_background)
//        addNoteWindowBg = findViewById(R.id.windowAddNote)

        setActivityStyle()

        binding.btnAddNote.setOnClickListener {
            Log.i(TAG, "btnAddNote clicked")
            Log.i(TAG, "noteText: " + binding.edtNoteText.text)

            // Return the note back to the NotesActivity
            val data = Intent()
            data.putExtra("note_text", binding.edtNoteText.text.toString())
            data.putExtra("source", binding.edtSource.text.toString())
            data.putExtra("tag", binding.edtTag.text.toString())

            setResult(Activity.RESULT_OK, data)
            // Close current window
            finish()
            onBackPressed()
        }
    }

    private fun setActivityStyle() {
        // Make the background full screen, over status bar
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        this.window.statusBarColor = Color.TRANSPARENT
        val winParams = this.window.attributes
        winParams.flags =
            winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        this.window.attributes = winParams

        // Fade animation for the background of Popup Window
        val alpha = 100 //between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), Color.TRANSPARENT, alphaColor)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            binding.bgAddNote.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()

        binding.windowAddNote.alpha = 0f
        binding.windowAddNote.animate().alpha(1f).setDuration(500)
            .setInterpolator(DecelerateInterpolator()).start()

        // Close window when you tap on the dim background
        binding.bgAddNote.setOnClickListener { onBackPressed() }
        binding.windowAddNote.setOnClickListener { /* Prevent activity from closing when you tap on the popup's window background */ }
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Fade animation for the background of Popup Window when you press the back button
        val alpha = 100 // between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), alphaColor, Color.TRANSPARENT)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            binding.bgAddNote.setBackgroundColor(
                animator.animatedValue as Int
            )
        }

        // Fade animation for the Popup Window when you press the back button
        binding.windowAddNote.animate().alpha(0f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()

        // After animation finish, close the Activity
        colorAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                finish()
                overridePendingTransition(0, 0)
            }
        })
        colorAnimation.start()
    }
}