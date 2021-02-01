package b2d.l.mahtmagandhi;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
    private Context context;
    private List<AppointmentModel.Data> data;

    public AppointmentAdapter( List<AppointmentModel.Data> data) {

        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.raw_appointment, parent, false);

//        contactView.setOnClickListener(i);
        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AppointmentModel.Data appointment_data = data.get(position);

        String ds = appointment_data.getApptDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat tranDateFormat = new SimpleDateFormat("dd MMM yyyy");
        Date date = null;
        try {
           date =  simpleDateFormat.parse(ds);
        } catch (ParseException e) {
            //e.printStackTrace();
        }
        String dates = tranDateFormat.format(date);

        holder.date.setText(dates+" "+appointment_data.getApptTime());
        //holder.status.setText(appointment_data.getBookedStatus());
        String msg = null;
        try {
             msg = URLDecoder.decode(appointment_data.getMessage(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            msg = appointment_data.getMessage();
        }

        holder.detail.setText(msg);
        int color;

        switch (appointment_data.getBookedStatus()) {
            case 0: {
                holder.status.setText("Pending");
                color = context.getResources().getColor(R.color.pending);
                holder.llStatus.setBackgroundResource(R.drawable.rectangle_curly_box_stroke_pending);
                holder.statusIcon.setImageResource(R.drawable.ic_pending);

                break;
            }
            case 1: {
                holder.status.setText("Booked");
                color = context.getResources().getColor(R.color.book);
                holder.llStatus.setBackgroundResource(R.drawable.rectangle_curly_box_stroke_booked);
                holder.statusIcon.setImageResource(R.drawable.ic_confirmed);

                break;
            }
            case 2:{
                holder.status.setText("Expired");
                color = context.getResources().getColor(R.color.expired);
                holder.llStatus.setBackgroundResource(R.drawable.rectangle_curly_box_stroke_expired);
                holder.statusIcon.setImageResource(R.drawable.ic_expired);

                break;
            }
            default:
                holder.status.setText("Booked");
                color = context.getResources().getColor(R.color.book);

        }

        holder.status.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, status, detail;
        LinearLayout llStatus;
        ImageView statusIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.textView9);
            status = itemView.findViewById(R.id.textView13);
            detail = itemView.findViewById(R.id.textView14);
            llStatus = itemView.findViewById(R.id.ll_status_aapt);
            statusIcon = itemView.findViewById(R.id.ic_status_apt);
        }
    }
}
