package b2d.l.mahtmagandhi;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
    private static final int MAX_LINES = 3;
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

        holder.date.setText(dates);
        holder.timeTextView.setText(appointment_data.getApptTime());


        //holder.status.setText(appointment_data.getBookedStatus());
        String msg = null;
        msg = StringEscapeUtils.unescapeJava(appointment_data.getMessage());

        holder.detail.setText(msg);

        String finalMsg = msg;
        holder.detail.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                holder.detail.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int linecount = holder.detail.getLineCount();
                Log.d("Appointment", "Number of lines is " + linecount);
                if (linecount>MAX_LINES){
                    holder.detail.post(new Runnable() {
                        @Override
                        public void run() {
                            // Past the maximum number of lines we want to display.
                            if (holder.detail.getLineCount() > MAX_LINES) {
                                int lastCharShown = holder.detail.getLayout().getLineVisibleEnd(MAX_LINES - 1);

                                holder.detail.setMaxLines(MAX_LINES);

                                String moreString = context.getString(R.string.more);
                                String suffix = " " + moreString;

                                // 3 is a "magic number" but it's just basically the length of the ellipsis we're going to insert
                                String actionDisplayText = finalMsg.substring(0, lastCharShown - suffix.length() - 3) + "..." + suffix;

                                SpannableString truncatedSpannableString = new SpannableString(actionDisplayText);
                                int startIndex = actionDisplayText.indexOf(moreString);
                                truncatedSpannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context,R.color.colorPrimary)), startIndex, startIndex + moreString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                ClickableSpan clickableSpan = new ClickableSpan() {
                                    @Override
                                    public void onClick(View textView) {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
// Add the buttons
                                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // User clicked OK button
                                                dialog.dismiss();
                                            }
                                        });
                                        builder.setMessage(finalMsg);


// Create the AlertDialog
                                        AlertDialog dialog = builder.create();
                                        dialog.show();

                                    }
                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        super.updateDrawState(ds);
                                        ds.setUnderlineText(false);
                                    }
                                };
                                truncatedSpannableString.setSpan(clickableSpan, startIndex, startIndex + moreString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                holder.detail.setText(truncatedSpannableString);
                                holder.detail.setMovementMethod(LinkMovementMethod.getInstance());
                                holder.detail.setHighlightColor(Color.TRANSPARENT);
                            }
                        }
                    });
                    //AppHelper.makeTextViewResizable(holder.detail,3,"View More",true);
                    //notifyDataSetChanged();
                }

            }
        });




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
        TextView date, status, detail,timeTextView;
        LinearLayout llStatus;
        ImageView statusIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.textView9);
            status = itemView.findViewById(R.id.textView13);
            detail = itemView.findViewById(R.id.textView14);
            llStatus = itemView.findViewById(R.id.ll_status_aapt);
            statusIcon = itemView.findViewById(R.id.ic_status_apt);
            timeTextView = itemView.findViewById(R.id.textView_time_appointment);
        }
    }
}
