package b2d.l.mahtmagandhi

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.emoji.widget.EmojiAppCompatEditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import b2d.l.mahtmagandhi.ProblemsResponseModel.Data.ImageArr
import b2d.l.mahtmagandhi.Utility.customSnackBar
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.github.tntkhang.fullscreenimageview.library.FullScreenImageViewActivity
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import com.lopei.collageview.CollageView
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import org.apache.commons.lang3.StringEscapeUtils
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*

class Comment : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    private var preferences: SharedPreferences? = null
    var comment: EmojiAppCompatEditText? = null
    private var avi: ProgressBar? = null
    private var passToken: Boolean? = null
    var textView: TextView? = null
    var materialCardView: MaterialCardView? = null
    var comment_box: CardView? = null
    private var resol = false
    private var materialCardViewResolved: MaterialCardView? = null
    fun startAnim() {
        // avi.show();
        avi!!.visibility = View.VISIBLE
        // or avi.smoothToShow();
    }

    fun stopAnim() {
        avi!!.visibility = View.INVISIBLE
        //        avi.hide();
        // or avi.smoothToHide();
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        val sliderView = findViewById<SliderView>(R.id.imageSlider)
        textView = findViewById(R.id.textView30)
        comment_box = findViewById(R.id.cardView2)
        textView12.setText(intent.getStringExtra("page_name"))
        val adapter = SliderAdapterExample(this)
        //        StatusBarUtil.setTransparent(this);
        avi = findViewById(R.id.avi)
        materialCardViewResolved = findViewById(R.id.mcv_comment_resolved)
        if (intent != null && intent.hasExtra("isResolved")) {
            resol = intent.getBooleanExtra("isResolved", false)
            resolwork()
        }

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/recyclerView = findViewById(R.id.rv_comment)
        recyclerView!!.setLayoutManager(LinearLayoutManager(this))
        recyclerView!!.setNestedScrollingEnabled(false)
        comment = findViewById(R.id.editTextTextMultiLine2)

        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        passToken = intent.getBooleanExtra("passToken", true)
        materialCardView = findViewById(R.id.mcv_comment_resolved)
        if (passToken!!) { //from problem page
//            sliderView.setVisibility(View.VISIBLE);//// TODO: 13/1/21
            textView!!.setVisibility(View.VISIBLE)

//            // Toast.makeText(this, "comming from problem", // Toast.LENGTH_SHORT).show();
            val collageView = findViewById<View>(R.id.collageView) as CollageView
            val urls = ArrayList<String>()

            val imgs: ArrayList<ImageArr> = intent.getParcelableArrayListExtra("imgs")


            if (imgs.size > 0) {
                for (i in imgs.indices) {
                    urls.add(Url.burl + imgs[i].imageName)
                }
                Log.d("ashok_urls", urls.toString())
            } else {
                //vies disable
            }
            collageView
                    .photoMargin(1)
                    .photoPadding(3)
                    .backgroundColor(Color.WHITE) //                    .photoFrameColor(Color.BLUE)
                    .useFirstAsHeader(false) // makes first photo fit device widtdh and use full line
                    //                    .defaultPhotosForLine(5) // sets default photos number for line of photos (can be changed by program at runtime)
                    //                    .iconSelector(this, getResources().getDimensionPixelSize(R.dimen.icon_size))
                    .useCards(true) // adds cardview backgrounds to all photos
                    //                    .maxWidth(100) // will resize images if their side is bigger than 100
                    //                    .placeHolder(R.drawable.placeholder_photo) //adds placeholder resource
                    //                    .headerForm(CollageView.ImageForm.IMAGE_FORM_SQUARE) // sets form of image for header (if useFirstAsHeader == true)
                    //                    .photosForm(CollageView.ImageForm.IMAGE_FORM_HALF_HEIGHT) //sets form of image for other photos
                    .loadPhotos(urls) // here you can use Array/List of photo urls or array of resource ids
            collageView.setOnPhotoClickListener { position ->
                val url = urls[position]
                val fullImageIntent = Intent(this@Comment, FullScreenImageViewActivity::class.java)
                // uriString is an ArrayList<String> of URI of all images
                fullImageIntent.putExtra(FullScreenImageViewActivity.URI_LIST_DATA, urls)
                // pos is the position of image will be showned when open
                fullImageIntent.putExtra(FullScreenImageViewActivity.IMAGE_FULL_SCREEN_CURRENT_POS, position)
                startActivity(fullImageIntent)
            }

        } else {
//            sliderView.setVisibility(View.GONE);
            textView!!.setVisibility(View.GONE)
            //            materialCardView.setVisibility(View.GONE);
        }
        if (intent.hasExtra("title") && intent.hasExtra("dis"))
            textView!!.setText(intent.getStringExtra("title") + "\n" + intent.getStringExtra("dis"))
        else if (intent.hasExtra("dis"))
            textView!!.setText(intent.getStringExtra("dis"))
        val job = GlobalScope.async {
            return@async Utility.isInternetAvailable()
        }
        job.invokeOnCompletion {
            val isInternet = job.getCompleted()
            GlobalScope.launch(Dispatchers.Main) {
                if (isInternet) {
                    fetchComments()

                } else {
                    stopAnim()
                    customSnackBar(recyclerView!!, this@Comment, "No Internet Available.", ContextCompat.getColor(this@Comment, R.color.error), R.drawable.ic_error)
                }
            }

        }
    }

    private fun fetchComments() {
        val url = Url.baseurl + intent.getStringExtra("url") //"/ctalk_comments";
        //        // Toast.makeText(this, "" + url, // Toast.LENGTH_SHORT).show();
        val json = JSONObject()
        try {
            json.put("parentId", intent.getStringExtra("pos"))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        startAnim()
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, json, Response.Listener { response ->
            try {
                if (response.getBoolean("success")) {
                    Log.d("ashok_comment", response.toString())
                    val commentData = ArrayList<CommentData?>()
                    val data = response.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val gson = Gson()
                        val comment = gson.fromJson(data.getJSONObject(i).toString(), CommentData::class.java)
                        commentData.add(comment)
                    }
                    Collections.reverse(commentData)
                    val commentAdapter: RecyclerView.Adapter<*> = CommentAdapter(this@Comment, commentData, preferences!!.getInt(Datas.id, 1), resol)
                    recyclerView!!.setAdapter(commentAdapter)

//                        JSONArray data_images = response.getJSONArray("data_images");
                } else {
                    // Toast.makeText(Comment.this, "" + response.getString("message"), // Toast.LENGTH_SHORT).show();
                    //login page
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@Comment)
                    val editor = preferences.edit()
                    editor.clear()
                    editor.apply()
                    startActivity(Intent(this@Comment, LoginActivity::class.java))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            stopAnim()
        }, Response.ErrorListener { error ->
            customSnackBar(recyclerView!!, this@Comment, error.toString(), ContextCompat.getColor(this@Comment, R.color.error), R.drawable.ic_error)

            // // Toast.makeText(Comment.this, "" + error, // Toast.LENGTH_SHORT).show();
            stopAnim()
        }) {
            override fun getHeaders(): Map<String, String> {
                val header: MutableMap<String, String> = HashMap()
                header["Content-Type"] = "application/json"
                header["token"] = preferences!!.getString(Datas.token, "")!!
                header["lid"] = preferences!!.getString(Datas.lagnuage_id, "1")!!
                //                header.put("enc", "");
                return header
            }
        }
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    private fun resolwork() {
        if (!resol) {
            materialCardViewResolved!!.visibility = View.VISIBLE
        } else {
            materialCardViewResolved!!.visibility = View.GONE
            comment_box!!.visibility = View.GONE
            resol = true
        }
    }

    fun back(view: View?) {
        finish()
    }

    fun newcomment(view: View?) {
        val s = comment!!.text.toString()
        if (isNullOrEmpty(s)) {
            // Toast.makeText(this, "Please type comment first", // Toast.LENGTH_SHORT).show();
            return
        }
        val url = Url.baseurl + intent.getStringExtra("newposturl")
        val json = JSONObject()
        try {
            json.put("parentId", intent.getStringExtra("pos"))
            json.put("comment", StringEscapeUtils.escapeJava(s))
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        val job = GlobalScope.async {
            return@async Utility.isInternetAvailable()
        }
        job.invokeOnCompletion {
            val isInternet = job.getCompleted()
            GlobalScope.launch(Dispatchers.Main) {
                if (isInternet) {
                    startAnim()
                    val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, json, Response.Listener { response ->
                        try {
                            if (response.getBoolean("success")) {
                                val data = response.getJSONArray("data")
                                val gson = Gson()
                                val commentData = ArrayList<CommentData?>()
                                for (i in 0 until data.length()) {
                                    val comment = gson.fromJson(data.getJSONObject(i).toString(), CommentData::class.java)
                                    commentData.add(comment)
                                }
                                // commentData.add(new CommentData("xyz",s));
                                Collections.reverse(commentData)
                                val adapter: RecyclerView.Adapter<*> = CommentAdapter(this@Comment, commentData, preferences!!.getInt(Datas.id, 1), resol)
                                recyclerView!!.adapter = adapter
                                comment!!.setText("")
                            }
                            // Toast.makeText(Comment.this, "" + response.getString("message"), // Toast.LENGTH_SHORT).show();
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        stopAnim()
                    }, Response.ErrorListener { error ->
                        customSnackBar(recyclerView!!, this@Comment, error.toString(), ContextCompat.getColor(this@Comment, R.color.error), R.drawable.ic_error)

                        //// Toast.makeText(Comment.this, "" + error.getMessage(), // Toast.LENGTH_SHORT).show();
                        stopAnim()
                    }) {
                        override fun getHeaders(): Map<String, String> {
                            val header: MutableMap<String, String> = HashMap()
                            header["Content-Type"] = "application/json"
                            header["token"] = preferences!!.getString(Datas.token, "")!!
                            header["lid"] = preferences!!.getString(Datas.lagnuage_id, "1")!!
                            return header
                        }
                    }
                    MySingleton.getInstance(this@Comment).addToRequestQueue(jsonObjectRequest)

                } else {
                    stopAnim()
                    customSnackBar(recyclerView!!, this@Comment, "No Internet Available.", ContextCompat.getColor(this@Comment, R.color.error), R.drawable.ic_error)
                }
            }

        }

    }

    fun maringresol(view: View?) {
        val url = Url.baseurl + "/psuggestion_update_status"
        val jsonRequest = JSONObject()
        try {
            jsonRequest.put("pid", intent.getStringExtra("pos"))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val job = GlobalScope.async {
            return@async Utility.isInternetAvailable()
        }
        job.invokeOnCompletion {
            val isInternet = job.getCompleted()
            GlobalScope.launch(Dispatchers.Main) {
                if (isInternet) {
                    startAnim()
                    val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonRequest, Response.Listener { response ->
                        Log.d("ashok", response.toString())
                        try {
                            if (response.getBoolean("success")) {
                                // Toast.makeText(Comment.this, "success", // Toast.LENGTH_SHORT).show();
                                resol = true
                                resolwork()
                                materialCardViewResolved!!.visibility = View.GONE
                                if (recyclerView!!.adapter is CommentAdapter) {
                                    (recyclerView!!.adapter as CommentAdapter?)!!.reol(true)
                                    recyclerView!!.adapter!!.notifyDataSetChanged()
                                }

                            } else {
                                // Toast.makeText(Comment.this, "" + response.getString("message"), // Toast.LENGTH_SHORT).show();
                                //login page
                                val preferences = PreferenceManager.getDefaultSharedPreferences(this@Comment)
                                val editor = preferences.edit()
                                editor.clear()
                                editor.apply()
                                startActivity(Intent(this@Comment, LoginActivity::class.java))
                            }
                            Log.d("ashok", response.getString("message"))
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        stopAnim()
                    }, Response.ErrorListener { error -> // Toast.makeText(Comment.this, "" + error, // Toast.LENGTH_SHORT).show();
                        Log.d("ashok", error.toString())
                        stopAnim()
                    }) {
                        @Throws(AuthFailureError::class)
                        override fun getHeaders(): Map<String, String> {
                            val headers: MutableMap<String, String> = HashMap()
                            headers["Content-Type"] = "application/json"
                            headers["token"] = preferences!!.getString(Datas.token, "")!!
                            headers["lid"] = preferences!!.getString(Datas.lagnuage_id, "1")!!
                            Log.d("ashok", preferences!!.getString(Datas.lagnuage_id, "1"))
                            return headers
                        }
                    }
                    MySingleton.getInstance(this@Comment).addToRequestQueue(jsonObjectRequest)

                } else {
                    stopAnim()
                    customSnackBar(recyclerView!!, this@Comment, "No Internet Available.", ContextCompat.getColor(this@Comment, R.color.error), R.drawable.ic_error)
                }
            }

        }

    }

    companion object {
        fun isNullOrEmpty(str: String?): Boolean {
            return if (str != null && !str.isEmpty()) false else true
        }
    }
}