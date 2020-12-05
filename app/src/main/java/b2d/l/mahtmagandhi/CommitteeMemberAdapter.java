package b2d.l.mahtmagandhi;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;

public class CommitteeMemberAdapter extends RecyclerView.Adapter<CommitteeMemberAdapter.ViewHolder> {
    private Context context;
    private ArrayList<CommitteeMemberData> committeeMemberData;

    public CommitteeMemberAdapter(Context context, ArrayList<CommitteeMemberData> committeeMemberData) {

        this.context = context;
        this.committeeMemberData = committeeMemberData;
    }

    @NonNull
    @Override
    public CommitteeMemberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.comitte_raw, parent, false);

//        contactView.setOnClickListener(i);
        // Return a new holder instance
        return new CommitteeMemberAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommitteeMemberData data = committeeMemberData.get(position);
        holder.textView.setText(data.getFirst_name() + " " + data.getLast_name());
        holder.textView1.setText(data.getDesignation());
        Glide.with(context).load(data.getImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);
//        holder.imageView.setImageResource(committeeMemberData.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return committeeMemberData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textView1;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView33);
            textView1 = itemView.findViewById(R.id.textView34);
            imageView = itemView.findViewById(R.id.profile_image);

        }
    }
}
