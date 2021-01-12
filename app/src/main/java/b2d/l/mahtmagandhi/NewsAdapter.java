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

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private Context context;
    private List<NewsUpdateResponseModel.Data> chatData;
    private String s;
    private String s1;
    private String s2;
    private Boolean passToken;
    private AVLoadingIndicatorView avi;


    public NewsAdapter(Context context, List<NewsUpdateResponseModel.Data> chatData, String s, String s1, String s2, boolean pass, AVLoadingIndicatorView avi) {

        this.context = context;
        this.chatData = chatData;
        this.s = s;
        Boolean passToken = pass;
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

        NewsUpdateResponseModel.Data x = chatData.get(position);
        // holder.username.setText(x.getTitle());

        holder.share.setOnClickListener(view -> {
            try {
                Uri uri = null;
                if (x.getImage_name() != null && Patterns.WEB_URL.matcher(x.getImage_name().toString()).matches())
                    uri = Utility.INSTANCE.saveBitmap(
                            view.getContext(),
                            holder.descIv,
                            Bitmap.CompressFormat.JPEG,
                            "image/jpeg",
                            "",
                            "statement"
                    );

                Utility.INSTANCE.share(Html.fromHtml(x.getDescription()).toString(), uri, view.getContext());

            } catch (IOException e) {

            }
//                Toast.makeText(context, "Sharing", Toast.LENGTH_SHORT).show();

        });

//        holder.imageView.setImageResource(x.getImage());
        holder.likesCountTv.setText(x.getLikes() + "");
       /* holder.dislikesCountTv.setText(x.getDislike() + "");
        holder.commentBtnTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Comment.class);
                intent.putExtra("pos", x.getId());
                intent.putExtra("url", s1);
                intent.putExtra("newposturl", s2);
                intent.putExtra("passToken", passToken);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        })*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.descTv.setText(Html.fromHtml(x.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.descTv.setText(Html.fromHtml(x.getDescription()));
        }
        Object img = x.getImage_name();



        if (img != null && img.toString().contains("election")) {
            if (!img.toString().contains("https")) img = Url.http + img;
        }
            else if (img != null && !img.toString().contains("election"))img = Url.burl+img;

        if (img==null)holder.descIv.setVisibility(View.GONE);
        else holder.descIv.setVisibility(View.VISIBLE);

        Glide.with(context).load(img).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.descIv);

        Object finalImg = img;
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri uri = null;
                    if (finalImg != null && finalImg.toString().length() > 0)
                        uri = Utility.INSTANCE.saveBitmap(
                                view.getContext(),
                                holder.descIv,
                                Bitmap.CompressFormat.JPEG,
                                "image/jpeg",
                                "",
                                "statement"
                        );

                    Utility.INSTANCE.share(Html.fromHtml(x.getDescription()).toString(), uri, view.getContext());

                } catch (IOException e) {

                }
//                Toast.makeText(context, "Sharing", Toast.LENGTH_SHORT).show();

            }
        });
        if (x.getImage_name() == null) holder.descIv.setVisibility(View.GONE);
        else holder.descIv.setVisibility(View.VISIBLE);
        Glide.with(context).load(img).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.descIv);
       if (x.getUserData()!=null && x.getUserData().size()>0) {
           Glide.with(context).load(x.getUserData().get(0).getUser_image()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.descIv);
           holder.usernameTv.setText(x.getUserData().get(0).getUser_name());
       }
/*
        holder.dislikeBtnTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (x.getUnlikeStatus() == 1) {
                    Toast.makeText(context, "you already disliked", Toast.LENGTH_SHORT).show();
                    return;
                }
                like_dislike(2, x, holder.likesCountTv, holder.likesCountTv);

            }
        });
*/

        holder.like.setOnClickListener(view -> {
            if (x.getLikeStatus() == 1) {
                Toast.makeText(context, "you already liked", Toast.LENGTH_SHORT).show();
                return;
            }
            like_dislike(1, x, holder.likesCountTv, holder.likesCountTv);

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

    private void like_dislike(int i, NewsUpdateResponseModel.Data x, TextView likes, TextView dilikes) {
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, response -> {
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

        }, error -> Toast.makeText(context, "" + error.toString(), Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() {
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
        ImageView descIv, avtarIv;
        TextView likesCountTv, usernameTv, descTv, timeTv;
        LinearLayout like, share;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descIv = itemView.findViewById(R.id.imageView_desc_news);
            avtarIv = itemView.findViewById(R.id.iv_avtar_news);
            timeTv = itemView.findViewById(R.id.tv_time_news);
            likesCountTv = itemView.findViewById(R.id.textView_like_count);
            //dislikesCountTv = itemView.findViewById(R.id.textView_disike_count);
            //commentCountTv = itemView.findViewById(R.id.textView_count_comment);
            //dislikeBtnTv = itemView.findViewById(R.id.textView_btn_dislike_commu_talk);
            //commentBtnTv = itemView.findViewById(R.id.textView_btn_comment_commu_talk);
            usernameTv = itemView.findViewById(R.id.tv_name_news);
            descTv = itemView.findViewById(R.id.tv_des_news);
            like = itemView.findViewById(R.id.like_news);
            share = itemView.findViewById(R.id.dislike_news);


        }
    }
}