package b2d.l.mahtmagandhi;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private Context context;
    private ArrayList<CommentData> data;
    private int userid;


    public CommentAdapter(Context context, ArrayList<CommentData> data, int userid) {
        this.context = context;
        this.data = data;
        this.userid = userid;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.raw_comment, parent, false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CommentData commentData = data.get(position);
        holder.comment.setText(commentData.getComment());
        holder.name.setText(commentData.getUsername());
        if (commentData.getUser_id() == userid) {

            holder.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.commentself));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                holder.itemView.setForegroundGravity(GravityCompat.END);
//                holder.itemView.setForegroundGravity(Gravity.END);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView comment, name;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.textView25);
            name = itemView.findViewById(R.id.textView24);
            constraintLayout = itemView.findViewById(R.id.clay);

        }
    }
}
