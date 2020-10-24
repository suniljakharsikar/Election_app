package b2d.l.mahtmagandhi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    ArrayList<HomelistData> homelistDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        recyclerView = findViewById(R.id.rv);
// use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        homelistDatas = new ArrayList<>();
        homelistDatas.add(new HomelistData("About Us", R.drawable.info));
        homelistDatas.add(new HomelistData("Meetings", R.drawable.suitcase));
        homelistDatas.add(new HomelistData("Community Chat", R.drawable.chatboxes));
        homelistDatas.add(new HomelistData("News & Update", R.drawable.newspaper));
        homelistDatas.add(new HomelistData("Our Works", R.drawable.groupwork));
        homelistDatas.add(new HomelistData("Vision & Mission", R.drawable.vision));
        homelistDatas.add(new HomelistData("Problem / Suggestion", R.drawable.problem));
        homelistDatas.add(new HomelistData("Manifesto", R.drawable.menifesto));
        homelistDatas.add(new HomelistData("Survey", R.drawable.sarve));
        homelistDatas.add(new HomelistData("Committee Member", R.drawable.commetee));
        homelistDatas.add(new HomelistData("Voting Guide", R.drawable.vote));
        homelistDatas.add(new HomelistData("Helpline", R.drawable.helpline));
        homelistDatas.add(new HomelistData("Setting & Profile", R.drawable.setting));
        RecyclerView.Adapter mAdapter = new HomeAdapter(this, homelistDatas, recyclerView);
        recyclerView.setAdapter(mAdapter);

    }
    public void back(View view) {
        finish();
    }
}