package b2d.l.mahtmagandhi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Patterns;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ChatData> chatData;
    private String s;
    private String s1;
    private String s2;
    private Boolean passToken;
    private AVLoadingIndicatorView avi;


    public NewsAdapter(Context context, ArrayList<ChatData> chatData, String s, String s1, String s2, boolean pass, AVLoadingIndicatorView avi) {

        this.context = context;
        this.chatData = chatData;
        this.s = s;
        this.s1 = s1;
        this.s2 = s2;
        this.passToken = pass;
        this.avi = avi;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.raw_new_news, parent, false);

//        contactView.setOnClickListener(i);
        // Return a new holder instance
        return new NewsAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final ChatData x = chatData.get(position);
       // holder.username.setText(x.getTitle());

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void like_dislike(int i, ChatData x, TextView likes, TextView dilikes) {
        String url = Url.baseurl + s;
        JSONObject json = new JSONObject();
        try {
            json.put("parentId", x.getId());
            json.put("type", i);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (avi != null) {
            avi.setVisibility(View.VISIBLE);
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        if (i == 1) {//like increase
                            x.setLikes(x.getLikes() + 1);
                            x.setLikeStatus(1);
//                            notifyDataSetChanged();
//                            notifyItemChanged(position);
                            likes.setText(x.getLikes() + "");
                            if (x.getUnlikeStatus() == 1) {
                                x.setDislike(x.getDislike() - 1);
                                x.setUnlikeStatus(0);
//                            notifyItemChanged(position);
                                dilikes.setText(x.getDislike() + "");
                            }
                            if (avi != null)
                                avi.setVisibility(View.INVISIBLE);
                        }
                        if (i == 2) {
                            x.setDislike(x.getDislike() + 1);
                            x.setUnlikeStatus(1);
//                            notifyItemChanged(position);
                            dilikes.setText(x.getDislike() + "");
                            if (x.getLikeStatus() == 1) {
                                x.setLikes(x.getLikes() - 1);
                                x.setLikeStatus(0);
//                            notifyItemChanged(position);
                                likes.setText(x.getDislike() + "");
                            }
                            if (avi != null)
                                avi.setVisibility(View.INVISIBLE);

                        }
//                        Toast.makeText(context, "" + response.getString("message"), Toast.LENGTH_SHORT).show();

                        //// TODO: 19/12/20 refresh number of likes
                    } else {
                        Toast.makeText(context, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                        //login page
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.apply();
                        editor.clear();
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

    @Override
    public int getItemCount() {
        return chatData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView likes, dilikes, dis, username;
        LinearLayout comment, share;
        ImageView like, dislike;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            imageView = itemView.findViewById(R.id.imageView_desc_news);
//            likes = itemView.findViewById(R.id.textView_like_count);
//            //dilikes = itemView.findViewById(R.id.textView37);
//            //comment = itemView.findViewById(R.id.comment);
//            //dis = itemView.findViewById(R.id.textView35);
////            like = itemView.findViewById(R.id.tv_btn_like_news);
//           // dislike = itemView.findViewById(R.id.imageView46);
//            share = itemView.findViewById(R.id.tv_btn_share);
//            username = itemView.findViewById(R.id.tv_name_news);

        }
    }
}
