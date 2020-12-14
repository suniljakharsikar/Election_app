package b2d.l.mahtmagandhi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.ViewHolder> implements View.OnClickListener {

    private ArrayList<Meeting> meetings;
    private Context context;
    private RecyclerView mRecyclerView;


    public MeetingAdapter(ArrayList<Meeting> meetings, Context context, RecyclerView mRecyclerView) {
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

        Meeting meeting = meetings.get(position);
        holder.title.setText(meeting.getTitle());

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


    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }

    @Override
    public void onClick(View view) {
        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
        Intent intent = new Intent(context, MeetingDetails.class);
        intent.putExtra("meet", meetings.get(itemPosition));
        context.startActivity(intent);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, date, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView26);
            date = itemView.findViewById(R.id.textView27);
            time = itemView.findViewById(R.id.textView28);
        }
    }
}
