package b2d.l.mahtmagandhi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class NewPost extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
    }

    public void back(View view) {
        Toast.makeText(this, "post not implemented", Toast.LENGTH_SHORT).show();
        finish();
    }
}