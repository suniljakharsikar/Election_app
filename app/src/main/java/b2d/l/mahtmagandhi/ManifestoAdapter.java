package b2d.l.mahtmagandhi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ManifestoAdapter extends RecyclerView.Adapter<ManifestoAdapter.ViewHolder> {
    List<ManifestoResponseModel.Data> data;


    public ManifestoAdapter(List<ManifestoResponseModel.Data> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ManifestoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.manifestoraw, parent, false);

        // Return a new holder instance
        return new ManifestoAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         ManifestoResponseModel.Data model = data.get(position);
         holder.titleMfs.setText(model.getTitle());
         holder.detailMfs.setText(model.getDescription());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleMfs;
        TextView detailMfs;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

             titleMfs = itemView.findViewById(R.id.textView_title_manifesto);
             detailMfs = itemView.findViewById(R.id.tv_desc_manifesto);
        }
    }
}
