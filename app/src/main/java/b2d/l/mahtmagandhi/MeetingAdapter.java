package b2d.l.mahtmagandhi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.ViewHolder> implements View.OnClickListener {

    private List<Meeting> meetings;
    private Context context;
    private RecyclerView mRecyclerView;


    public MeetingAdapter(List<Meeting> meetings, Context context, RecyclerView mRecyclerView) {
        this.meetings = meetings;

        this.context = context;
        this.mRecyclerView = mRecyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.raw_meeting, parent, false);

        contactView.setOnClickListener(this);
        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Meeting meeting = meetings.get(position);
        holder.title.setText(meeting.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MeetingDetails.class);
                intent.putExtra("meet", meeting);
                context.startActivity(intent);
            }
        });

        String rawDate = meeting.getMeeting_date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat transDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        try {
            Date date = simpleDateFormat.parse(rawDate);
            String transDate = transDateFormat.format(date);
            meeting.setMeeting_date(transDate);

            holder.date.setText(transDate);
        } catch (ParseException e) {
            e.printStackTrace();
            holder.date.setText("");

        }
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat transTimeFormat = new SimpleDateFormat("hh:mm a");

        String rawTime = meeting.getMeeting_time();
        try {
            Date timeD = simpleTimeFormat.parse(rawTime);
            String time = transTimeFormat.format(timeD);
            meeting.setMeeting_time(time);
            holder.time.setText(time);


        } catch (ParseException e) {
            e.printStackTrace();
            holder.time.setText("");

        }

        holder.addressTextview.setText(meeting.getAddress());

        holder.addressTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap(holder.itemView.getContext(),meeting.getAddress());
            }
        });


        holder.mapImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap(holder.itemView.getContext(),meeting.getAddress());
            }
        });


    }

    private void openMap(Context context, String address) {
        Intent searchAddress = new  Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+address));
       context.startActivity(searchAddress);
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }

    @Override
    public void onClick(View view) {
        int itemPosition = mRecyclerView.getChildLayoutPosition(view);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, date, time,addressTextview;
        ImageView mapImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView26);
            date = itemView.findViewById(R.id.textView27);
            time = itemView.findViewById(R.id.textView28);
            addressTextview = itemView.findViewById(R.id.textView_location_addr);
            mapImageView = itemView.findViewById(R.id.imageView_map_meeting);
        }
    }
}
