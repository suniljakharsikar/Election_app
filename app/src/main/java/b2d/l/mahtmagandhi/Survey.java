package b2d.l.mahtmagandhi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

public class Survey extends AppCompatActivity {
    RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    ArrayList<String> homelistDatas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        recyclerView = findViewById(R.id.rv4);
// use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        homelistDatas = new ArrayList<>();
        homelistDatas.add("How to develop country ?");
        homelistDatas.add("Is this possible ?");
        homelistDatas.add("New rule is effective ?");
        homelistDatas.add("Traffic Rule is right ?");
        homelistDatas.add("How to develop country ?");
        homelistDatas.add("Why Join our Party ?");

        Surveydapter mAdapter = new Surveydapter(this, homelistDatas, recyclerView);
        recyclerView.setAdapter(mAdapter);
    }
    public void back(View view) {
        finish();
    }
}