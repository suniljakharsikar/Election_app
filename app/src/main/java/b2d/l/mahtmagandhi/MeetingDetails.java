package b2d.l.mahtmagandhi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MeetingDetails extends AppCompatActivity {

    TextView title, date, time, dis;
    private Meeting meet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_details);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        meet = (Meeting) getIntent().getSerializableExtra("meet");
        title = findViewById(R.id.textView26);
        date = findViewById(R.id.textView27);
        time = findViewById(R.id.textView28);
        dis = findViewById(R.id.textView_des_meeting_detail);
        if (meet != null) {
            title.setText(meet.getTitle());
            date.setText(meet.getMeeting_date());
            time.setText(meet.getMeeting_time());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dis.setText(Html.fromHtml(meet.getDescription(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                dis.setText(Html.fromHtml(meet.getDescription()));
            }
        }

    }

    public void back(View view) {
        finish();
    }

    public void map(View view) {
//        meet.setLatitude("27.6225423");
//        meet.setLongitude("75.1662117");
        if ((meet.getLatitude().equals("null") || meet.getLongitude().equals("null")) && meet.getAddress().length()==0) {
            Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
        }else if (meet.getAddress().length()>0){
                openMap(this,meet.getAddress());
        }
        else {
            String strUri = "http://maps.google.com/maps?q=loc:" + meet.getLatitude() + "," + meet.getLongitude() + " (" + meet.getTitle() + ")";
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);
        }
    }


    private void openMap(Context context, String address) {
        Intent searchAddress = new  Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+address));
        context.startActivity(searchAddress);
    }
}