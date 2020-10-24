package b2d.l.mahtmagandhi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

public class CommitteeMember extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<CommitteeMemberData> committeeMemberData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_committee_member);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        recyclerView = findViewById(R.id.rv4);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        committeeMemberData = new ArrayList<>();
        committeeMemberData.add(new CommitteeMemberData("Vinita Kumari", "Sarpanch Jhotwara", R.drawable.pro1));
        committeeMemberData.add(new CommitteeMemberData("Ravindra Kumar", "Advocate", R.drawable.pro2));
        committeeMemberData.add(new CommitteeMemberData("Dr. Vinay", "Doctor Child Speciality", R.drawable.pro3));
        CommitteeMemberAdapter committeeMemberAdapter = new CommitteeMemberAdapter(this, committeeMemberData);
        recyclerView.setAdapter(committeeMemberAdapter);
    }
    public void back(View view) {
        finish();
    }
}