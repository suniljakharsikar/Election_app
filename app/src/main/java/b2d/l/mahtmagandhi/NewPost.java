package b2d.l.mahtmagandhi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewPost extends AppCompatActivity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        editText = findViewById(R.id.et_prob_sugg);
    }

    public void back(View view) {
        Toast.makeText(this, "post not implemented", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void newposting(View view) {

        String s = editText.getText().toString();
        if (isNullOrEmpty(s)) {
            Toast.makeText(this, "Please write something before post", Toast.LENGTH_SHORT).show();
        }

    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}