package b2d.l.mahtmagandhi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.Serializable;

public class MeetingDetails extends AppCompatActivity {

    TextView title, date, time, dis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_details);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        Meeting meet = (Meeting) getIntent().getSerializableExtra("meet");
        title = findViewById(R.id.textView26);
        date = findViewById(R.id.textView27);
        time = findViewById(R.id.textView28);
        dis = findViewById(R.id.textView_des_meeting_detail);
        if (meet != null) {
            title.setText(meet.getTitle());
            date.setText(meet.getMeeting_date());
            time.setText(meet.getMeeting_time());
            dis.setText(meet.getDescription());

        }
    }

    public void back(View view) {
        finish();
    }
}