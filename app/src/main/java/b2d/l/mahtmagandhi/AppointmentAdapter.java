package b2d.l.mahtmagandhi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Appointment_data> data;

    public AppointmentAdapter(Context context, ArrayList<Appointment_data> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.raw_appointment, parent, false);

//        contactView.setOnClickListener(i);
        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Appointment_data appointment_data = data.get(position);
        holder.date.setText(appointment_data.getDate());
        holder.status.setText(appointment_data.getStatus());
        holder.detail.setText(appointment_data.getDetail());
        int color;

        switch (holder.status.getText().toString()) {
            case "Pending":
                color = context.getResources().getColor(R.color.pending);
                break;
            case "Expired":
                color = context.getResources().getColor(R.color.expired);
                break;
            default:
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.textView9);
            status = itemView.findViewById(R.id.textView13);
            detail = itemView.findViewById(R.id.textView14);
        }
    }
}
