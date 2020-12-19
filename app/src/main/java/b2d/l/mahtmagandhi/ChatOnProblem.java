package b2d.l.mahtmagandhi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class ChatOnProblem extends AppCompatActivity {

    RecyclerView recyclerView;
    private EditText comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_on_problem);
        recyclerView = findViewById(R.id.rvchatonproblem);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        comment = findViewById(R.id.editTextTextMultiLine2);
        ArrayList<CommentData> data = new ArrayList<>();
        data.add(new CommentData("Pankaj Choudhary ", "Lorem ipsum dolor sit amet, "));
        data.add(new CommentData("Pankaj Choudhary ", "Lorem ipsum dolor sit amet, "));
        data.add(new CommentData("Pankaj Choudhary ", "Lorem ipsum dolor sit amet, "));
        data.add(new CommentData("Pankaj Choudhary ", "Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet, , "));
        RecyclerView.Adapter adapter = new CommentAdapter(this, data, 1);
        recyclerView.setAdapter(adapter);
    }

    public void back(View view) {
        finish();
    }
}