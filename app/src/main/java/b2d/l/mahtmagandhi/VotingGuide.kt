package b2d.l.mahtmagandhi

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import b2d.l.mahtmagandhi.Utility.customSnackBar
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_voting_guide.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class VotingGuide : YouTubeBaseActivity() {
    var textView: TextView? = null
    private var avi: ProgressBar? = null
    private var youTubePlayerView: YouTubePlayerView? = null
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
        setContentView(R.layout.activity_voting_guide)
        avi = findViewById(R.id.avi)
        youTubePlayerView = findViewById(R.id.youtube_voting_guide)
        youTubePlayerView!!.visibility = View.INVISIBLE


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
        textView = findViewById(R.id.tv)
        val job = GlobalScope.async {
            return@async Utility.isInternetAvailable()
        }
        job.invokeOnCompletion {
            val isInternet = job.getCompleted()
            if (isInternet) {
                fetchData()

            } else {
                stopAnim()
                customSnackBar(textView!!, this, "No Internet Available.", ContextCompat.getColor(this, R.color.error), R.drawable.ic_error)
            }
        }    }

    private fun fetchData() {
        val url = Url.baseurl + "/guide"
        val json = JSONObject()
        startAnim()
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, json, Response.Listener { response ->
            Log.d("ashok_voting", response.toString())
            try {
                if (response.getBoolean("success")) {
                    val description = response.getJSONArray("data").getJSONObject(0).getString("description")
                    val media = response.getJSONArray("data").getJSONObject(0).getString("media")
                    val mediaType = response.getJSONArray("data").getJSONObject(0).optInt("media_type")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        textView!!.text = Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT)
                    } else {
                        textView!!.text = Html.fromHtml(description)
                    }

                    if (mediaType==2){
                        youTubePlayerView!!.visibility = View.INVISIBLE
                        imageView_voting_guide.visibility = View.VISIBLE
                        Glide.with(this).load(Url.burl+media).into(imageView_voting_guide)

                    }else if (mediaType==1){
                        youTubePlayerView!!.visibility = View.VISIBLE
                        imageView_voting_guide.visibility = View.INVISIBLE
                    }else{
                        youTubePlayerView!!.visibility = View.GONE
                        imageView_voting_guide.visibility = View.GONE
                    }

                    youTubePlayerView!!.initialize("AIzaSyD5IrFsnu-cC6ODQM1r_KcIg_G584aBvzY", object : YouTubePlayer.OnInitializedListener {
                        override fun onInitializationSuccess(provider: YouTubePlayer.Provider, youTubePlayer: YouTubePlayer, b: Boolean) {
                            try {
                                val ms = media.split("=").toTypedArray()
                                if (ms.size > 0 && mediaType==1) {
                                    youTubePlayerView!!.visibility = View.VISIBLE

                                    youTubePlayer.loadVideo(ms[1])
                                    youTubePlayer.play()
                                }
                            } catch (e: Exception) {
                            }
                        }

                        override fun onInitializationFailure(provider: YouTubePlayer.Provider, youTubeInitializationResult: YouTubeInitializationResult) {}
                    })

                    //textView.setText(description);
//                        Toast.makeText(VotingGuide.this, "" + response.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(VotingGuide.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                    //login page
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@VotingGuide)
                    val editor = preferences.edit()
                    editor.clear()
                    editor.apply()
                    startActivity(Intent(this@VotingGuide, LoginActivity::class.java))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                //Toast.makeText(VotingGuide.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
            }
            stopAnim()
        }, Response.ErrorListener { error ->
            stopAnim()
            customSnackBar(textView!!, this@VotingGuide, error.toString(),
                    ContextCompat.getColor(this@VotingGuide, R.color.error), R.drawable.ic_error)
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val header: MutableMap<String, String> = HashMap()
                header["Content-Type"] = "application/json"
                val preferences = PreferenceManager.getDefaultSharedPreferences(this@VotingGuide)
                header["token"] = preferences.getString(Datas.token, "")!!
                header["lid"] = preferences.getString(Datas.lagnuage_id, "1")!!
                return header
            }
        }
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun back(view: View?) {
        finish()
    }
}