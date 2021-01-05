package b2d.l.mahtmagandhi;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CommitteeMemberAdapter extends RecyclerView.Adapter<CommitteeMemberAdapter.ViewHolder> {
    private Context context;
    private List<CommitteMembersResponseModel.Data> committeeMemberData;
    private String domain;

    public CommitteeMemberAdapter(Context context, List<CommitteMembersResponseModel.Data> committeeMemberData, String domain_name) {
        this.domain = domain_name;
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final CommitteMembersResponseModel.Data data = committeeMemberData.get(position);
        holder.textView.setText(data.getFirstName() + " " + data.getLastName());
        holder.textView1.setText(data.getDesignation());
        data.setImage(Url.burl + data.getImage());
        Glide.with(context).load(data.getImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);
        //Toast.makeText(context, "https://election.suniljakhar.in/"+data.getImage(), Toast.LENGTH_SHORT).show();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setImage(data.getImage());
                Intent intent = new Intent(holder.itemView.getContext(), CommitteeMemberDetailActivity.class);
                intent.putExtra("data", data);
                holder.itemView.getContext().startActivity(intent);
            }
        });
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
