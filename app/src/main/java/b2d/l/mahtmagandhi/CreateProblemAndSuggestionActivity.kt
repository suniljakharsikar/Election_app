package b2d.l.mahtmagandhi

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class CreateProblemAndSuggestionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_problem_and_suggestion)
    }

    fun back(view: View) {
        finish()
    }

    fun isNullOrEmpty(str: String?): Boolean {
        return if (str != null && !str.isEmpty()) false else true
    }
}