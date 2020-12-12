package b2d.l.mahtmagandhi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ProblemSuggestion extends AppCompatActivity {

    private RecyclerView rvProblems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_suggestion2);

        initView();
    }

    private void initView() {
        rvProblems = findViewById(R.id.rv_probs);
        rvProblems.setLayoutManager(new LinearLayoutManager(this));
        rvProblems.setAdapter(new ProblemSuggestionRecyclerViewAdapter());
    }

    public void back(View view) {
        finish();
    }

    public void createNewProbSug(View view) {
        startActivity(new Intent(this,CreateProblemAndSuggestionActivity.class));
    }
}