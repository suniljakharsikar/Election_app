package b2d.l.mahtmagandhi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OurWorkAdapter extends RecyclerView.Adapter<OurWorkAdapter.ViewHolder> {
    private Context context;
    private ArrayList<OurWorkData> data;

    public OurWorkAdapter(Context context, ArrayList<OurWorkData> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public OurWorkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.raw_our_work, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final OurWorkData ourWorkData = data.get(position);
        holder.title.setText(ourWorkData.getTitle());
        holder.content.setText(ourWorkData.getContent());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent d = new Intent(context, OurWorkDetail.class);
                d.putExtra("title", ourWorkData.getTitle());
                d.putExtra("content", ourWorkData.getContent());
                context.startActivity(d);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView20);
            content = itemView.findViewById(R.id.textView21);
        }
    }
}
