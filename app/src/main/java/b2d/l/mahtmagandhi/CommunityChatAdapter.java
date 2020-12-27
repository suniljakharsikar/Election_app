package b2d.l.mahtmagandhi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommunityChatAdapter extends RecyclerView.Adapter<CommunityChatAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ChatData> chatData;
    private String s;
    private String s1;
    private String s2;


    public CommunityChatAdapter(Context context, ArrayList<ChatData> chatData, String s, String s1, String s2) {

        this.context = context;
        this.chatData = chatData;
        this.s = s;
        this.s1 = s1;
        this.s2 = s2;
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final ChatData x = chatData.get(position);
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri uri = null;
                    if ( Patterns.WEB_URL.matcher(x.getImage_name()).matches())
                        uri = Utility.INSTANCE.saveBitmap(
                                view.getContext(),
                                holder.imageView,
                                Bitmap.CompressFormat.JPEG,
                                "image/jpeg",
                                "",
                                "statement"
                        );

                    Utility.INSTANCE.share(Html.fromHtml(x.getDescription()).toString(),uri,view.getContext());

                } catch (IOException e) {

                }
//                Toast.makeText(context, "Sharing", Toast.LENGTH_SHORT).show();
              /*  val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        "Send a simple text"
                )
                sendIntent.type = "text/plain"
                startActivity(sendIntent)*/
            }
        });

//        holder.imageView.setImageResource(x.getImage());
        holder.likes.setText(x.getLikes() + "");
        holder.dilikes.setText(x.getDislike() + "");
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Comment.class);
                intent.putExtra("pos", x.getId());
                intent.putExtra("url", s1);
                intent.putExtra("newposturl", s2);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.dis.setText(Html.fromHtml(x.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.dis.setText(Html.fromHtml(x.getDescription()));
        }
        Glide.with(context).load(x.getImage_name()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);

        holder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (x.getUnlikeStatus() == 1) {
                    Toast.makeText(context, "you already disliked", Toast.LENGTH_SHORT).show();
                    return;
                }
                like_dislike(2, x);
                x.setDislike(x.getDislike() + 1);
                x.setUnlikeStatus(1);
                notifyDataSetChanged();
            }
        });

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (x.getLikeStatus() == 1) {
                    Toast.makeText(context, "you already liked", Toast.LENGTH_SHORT).show();
                    return;
                }
                like_dislike(1, x);
                x.setLikes(x.getLikes() + 1);
                x.setLikeStatus(1);
                notifyDataSetChanged();
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri uri = null;
                    
                     uri = Utility.INSTANCE.saveBitmap(
                            view.getContext(),
                            holder.imageView,
                            Bitmap.CompressFormat.JPEG,
                            "image/jpeg",
                            "",
                            "statement"
                    );

                    Utility.INSTANCE.share(Html.fromHtml(x.getDescription()).toString(),uri,view.getContext());

                } catch (IOException e) {

                }
//                Toast.makeText(context, "Sharing", Toast.LENGTH_SHORT).show();
              /*  val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        "Send a simple text"
                )
                sendIntent.type = "text/plain"
                startActivity(sendIntent)*/
            }
        });
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
    private void like_dislike(int i, ChatData x) {
        String url = Url.baseurl + s;
        JSONObject json = new JSONObject();
        try {
            json.put("parentId", x.getId());
            json.put("type", i);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        Toast.makeText(context, "" + response.getString("message"), Toast.LENGTH_SHORT).show();

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
        TextView likes, dilikes, dis;
        LinearLayout comment, share;
        ImageView like, dislike;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView44);
            likes = itemView.findViewById(R.id.textView36);
            dilikes = itemView.findViewById(R.id.textView37);
            comment = itemView.findViewById(R.id.comment);
            dis = itemView.findViewById(R.id.textView35);
            like = itemView.findViewById(R.id.imageView45);
            dislike = itemView.findViewById(R.id.imageView46);
            share = itemView.findViewById(R.id.share);

        }
    }
}
