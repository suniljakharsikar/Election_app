package b2d.l.mahtmagandhi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private Context context;
    private ArrayList<CommentData> data;
    private int userid;
    private boolean resol;


    public CommentAdapter(Context context, ArrayList<CommentData> data, int userid, boolean resol) {
        this.context = context;
        this.data = data;
        this.userid = userid;
        this.resol = resol;
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

        if (commentData.getUser_id() == userid) {
//            holder.mcvMe.setVisibility(View.VISIBLE);
//            holder.mcvOther.setVisibility(View.GONE);
            holder.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.commentself));
//            holder.commentMe.setText(commentData.getComment());
//            holder.nameMe.setText(commentData.getUsername());

        } else {
//            holder.mcvMe.setVisibility(View.GONE);


        }
        holder.mcvOther.setVisibility(View.VISIBLE);
        holder.comment.setText(commentData.getComment());
        holder.name.setText(commentData.getUsername());
        if (resol && position == data.size() - 1) {
            holder.resold.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void reol(boolean b) {
        resol = b;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView comment, name/*,commentMe*//*,nameMe*/;
        ConstraintLayout constraintLayout, clMe;
        MaterialCardView mcvOther/*,mcvMe*/;
        LinearLayout resold;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.textView25);
//            commentMe = itemView.findViewById(R.id.textView_me_cmt);
            name = itemView.findViewById(R.id.textView24);
//            nameMe = itemView.findViewById(R.id.textView_me_name);
            constraintLayout = itemView.findViewById(R.id.clay);
//            clMe = itemView.findViewById(R.id.clay_me);
//            mcvMe = itemView.findViewById(R.id.mdc_me);
            mcvOther = itemView.findViewById(R.id.mdc_other);
            resold = itemView.findViewById(R.id.resol);

        }
    }
}
