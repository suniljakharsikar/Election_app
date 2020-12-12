package b2d.l.mahtmagandhi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class CreateProblemAndSuggestionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_problem_and_suggestion)
    }

    fun back(view: View) {
        finish()
    }
}