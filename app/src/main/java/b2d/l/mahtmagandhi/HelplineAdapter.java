package b2d.l.mahtmagandhi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HelplineAdapter extends RecyclerView.Adapter<HelplineAdapter.ViewHolder> {
    List<HelplineResponseModel.Data> data;

    public HelplineAdapter(List<HelplineResponseModel.Data> data) {
        this.data = data;
    }


    @NonNull
    @Override
    public HelplineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.raw_helpline, parent, false);

//        contactView.setOnClickListener(i);
        // Return a new holder instance
        return new HelplineAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.textView.setText(data.get(position).getTitle());
        holder.textView1.setText(data.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView, textView1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView43);
            textView1 = itemView.findViewById(R.id.textView44);

        }
    }
}
