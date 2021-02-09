package b2d.l.mahtmagandhi

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import b2d.l.mahtmagandhi.Utility.customSnackBar
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*

class NewsUpdate : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var linearLayoutManager: LinearLayoutManager? = null
    var chatData: ArrayList<ChatData>? = null
    private var preferences: SharedPreferences? = null
    private var avi: ProgressBar? = null
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
        setContentView(R.layout.activity_news_update)
        avi = findViewById(R.id.avi)
        startAnim()
        /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/recyclerView = findViewById(R.id.rv_news)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView!!.setLayoutManager(linearLayoutManager)
        val itemViewType = 0
        recyclerView!!.getRecycledViewPool().setMaxRecycledViews(itemViewType, 0)
        chatData = ArrayList()
        val job = GlobalScope.async {
            return@async Utility.isInternetAvailable()
        }
        job.invokeOnCompletion {
            val isInternet = job.getCompleted()

            GlobalScope.launch(Dispatchers.Main) {
            if (isInternet) {
                initView()


            } else {
                stopAnim()
                customSnackBar(recyclerView!!, this@NewsUpdate, "No Internet Available.", ContextCompat.getColor(this@NewsUpdate, R.color.error), R.drawable.ic_error)
            }
            }
        }
    }

    private fun initView() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val url = Url.baseurl + "/nevent_data"
        val json = JSONObject()
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, json, Response.Listener { response ->
            Log.d("ashok_news", response.toString())
            try {
                val gson = Gson()
                val (data, _, _, success) = gson.fromJson(response.toString(), NewsUpdateResponseModel::class.java)
                if (success) {
                    val communityChatAdapter = NewsAdapter(baseContext, data, "/nevent_like_unlike_post", "/nevent_comments", "/nevent_comments_post", false, avi)
                    recyclerView!!.adapter = communityChatAdapter
                } else {
                    // // Toast.makeText(getBaseContext(), "" + response.getString("message"), // Toast.LENGTH_SHORT).show();
                    //login page
                    val preferences = PreferenceManager.getDefaultSharedPreferences(baseContext)
                    val editor = preferences.edit()
                    editor.clear()
                    editor.apply()
                    startActivity(Intent(baseContext, LoginActivity::class.java))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            stopAnim()
        }, Response.ErrorListener { error ->
            stopAnim()
            customSnackBar(recyclerView!!, this@NewsUpdate, error.toString(),
                    ContextCompat.getColor(this@NewsUpdate, R.color.error), R.drawable.ic_error)
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val header: MutableMap<String, String> = HashMap()
                header["Content-Type"] = "application/json"
                header["token"] = preferences!!.getString(Datas.token, "")!!
                header["lid"] = preferences!!.getString(Datas.lagnuage_id, "1")!!
                return header
            }
        }
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun back(view: View?) {
        finish()
    }
}