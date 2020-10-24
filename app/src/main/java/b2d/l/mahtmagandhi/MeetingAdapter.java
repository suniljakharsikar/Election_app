package b2d.l.mahtmagandhi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.ViewHolder> implements View.OnClickListener {

    private int[] x;
    private Context context;
    private RecyclerView mRecyclerView;


    public MeetingAdapter(int[] x, Context context, RecyclerView mRecyclerView) {

        this.x = x;
        this.context = context;
        this.mRecyclerView = mRecyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.meetingrow, parent, false);

        contactView.setOnClickListener(this);
        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.textView.setText("Meeting " + position);
    }

    @Override
    public int getItemCount() {
        return x.length;
    }

    @Override
    public void onClick(View view) {
        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
        Intent intent = new Intent(context, MeetingDetails.class);
        intent.putExtra("position", itemPosition);
        context.startActivity(intent);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView26);
        }
    }
}
