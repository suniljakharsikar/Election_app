package b2d.l.mahtmagandhi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class CommunityChatAdapter extends RecyclerView.Adapter<CommunityChatAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ChatData> chatData;


    public CommunityChatAdapter(Context context, ArrayList<ChatData> chatData) {

        this.context = context;
        this.chatData = chatData;
    }

    @NonNull
    @Override
    public CommunityChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.raw_comunitytalk, parent, false);

//        contactView.setOnClickListener(i);
        // Return a new holder instance
        return new CommunityChatAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final ChatData x = chatData.get(position);
//        holder.imageView.setImageResource(x.getImage());
        holder.likes.setText(x.getLikes() + "");
        holder.dilikes.setText(x.getDislike() + "");
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Comment.class);
                intent.putExtra("pos", x.getId());
//                Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });
        holder.dis.setText(x.getDescription());
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = Url.baseurl + "/ctalk_like_unlike_post";
                JSONObject json = new JSONObject();
                try {
                    json.put("parentId", x.getId());
                    json.put("type", 1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {

                            } else {
                                Toast.makeText(context, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                                //login page
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear();
                                editor.apply();
                                context.startActivity(new Intent(context, LoginActivity.class));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(context, "" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> header = new HashMap<>();
                        header.put("Content-Type", "application/json");
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                        header.put("token", preferences.getString(Datas.token, ""));
                        header.put("lid", preferences.getString(Datas.lagnuage_id, "1"));
                        return header;
                    }
                };
                MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView likes, dilikes, dis;
        LinearLayout comment;
        ImageView like;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView44);
            likes = itemView.findViewById(R.id.textView36);
            dilikes = itemView.findViewById(R.id.textView37);
            comment = itemView.findViewById(R.id.comment);
            dis = itemView.findViewById(R.id.textView35);
            like = itemView.findViewById(R.id.imageView45);

        }
    }
}
