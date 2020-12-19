package b2d.l.mahtmagandhi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    ArrayList<HomelistData> homelistDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        View decorView = getWindow().getDecorView();
        //((CardView) findViewById(R.id.cardView3)).setContentPadding(0,140,0,0);
        final String TAG = "Home";
        MotionLayout motionLayout = ((MotionLayout) findViewById(R.id.ml_home));


/*// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = getActionBar();
        actionBar.hide();*/
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        recyclerView = findViewById(R.id.rv_home);
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

        final CardView cardView = findViewById(R.id.cardView3);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int dy, dx;

            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {

                    case RecyclerView.SCROLL_STATE_IDLE: {
                        Log.d(TAG, "onScrollStateChanged: " + layoutManager.findFirstVisibleItemPosition());
                        if (layoutManager.findFirstVisibleItemPosition() > 0) {
                            float dip = 48f;
                            Resources r = getResources();
                            float px = TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    dip,
                                    r.getDisplayMetrics()
                            );

                            cardView.setContentPadding(0, (int) px, 0, 0);
                        } else {
                            float dip = 140f;
                            Resources r = getResources();
                            float px = TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    dip,
                                    r.getDisplayMetrics()
                            );

                            cardView.setContentPadding(0, (int) px, 0, 0);

                        }

                               /* RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)recyclerView.getLayoutParams();

                                int marginTopPx = (int) (dip * getResources().getDisplayMetrics().density + 0.5f);
                                layoutParams.setMargins(0, marginTopPx, 0, 0);
                                recyclerView.setLayoutParams(layoutParams);*/
                        break;
                    }

                    case RecyclerView.SCROLL_STATE_DRAGGING: {
                        if (layoutManager.findFirstVisibleItemPosition() > 0) {
                            float dip = 48f;
                            Resources r = getResources();
                            float px = TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    dip,
                                    r.getDisplayMetrics()
                            );

                            cardView.setContentPadding(0, (int) px, 0, 0);
                        } else {
                            float dip = 140f;
                            Resources r = getResources();
                            float px = TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    dip,
                                    r.getDisplayMetrics()
                            );

                            cardView.setContentPadding(0, (int) px, 0, 0);

                        }

                        break;

                    }
                    case RecyclerView.SCROLL_STATE_SETTLING: {
                        Log.d(TAG, "onScrollStateChanged: SCROLL_STATE_SETTLING");
                        if (layoutManager.findFirstVisibleItemPosition() > 0) {
                            float dip = 48f;
                            Resources r = getResources();
                            float px = TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    dip,
                                    r.getDisplayMetrics()
                            );

                            cardView.setContentPadding(0, (int) px, 0, 0);
                        } else {
                            float dip = 140f;
                            Resources r = getResources();
                            float px = TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    dip,
                                    r.getDisplayMetrics()
                            );

                            cardView.setContentPadding(0, (int) px, 0, 0);

                        }

                        break;

                    }
                }
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, "onScrolled: " + dx + ", " + dy);

                this.dx = dx;
                this.dy = dy;


            }
        });

    }

    public void back(View view) {
        finish();
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void appointment(View view) {
        startActivity(new Intent(this, ChatOnProblem.class));

    }
}