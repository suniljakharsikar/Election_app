package b2d.l.mahtmagandhi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        holder.textView.setText(committeeMemberData.get(position).getName());
        holder.textView1.setText(committeeMemberData.get(position).getPost());
        holder.imageView.setImageResource(committeeMemberData.get(position).getImage());
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
