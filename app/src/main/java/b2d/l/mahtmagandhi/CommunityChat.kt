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
import b2d.l.mahtmagandhi.NewPost
import b2d.l.mahtmagandhi.Utility.customSnackBar
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_community_chat.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*

class CommunityChat : AppCompatActivity() {
    var linearLayoutManager: LinearLayoutManager? = null
    var chatData: ArrayList<ChatData>? = null
    private var preferences: SharedPreferences? = null
    companion object{
        var recyclerView: RecyclerView? = null

    }

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
        setContentView(R.layout.activity_community_chat)
        avi = findViewById(R.id.avi)

        stopAnim()
        textView40.text = intent.getStringExtra("title")
        /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        val floatingActionButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        floatingActionButton.setOnClickListener { //                startActivity(new Intent(CommunityChat.this, NewPost.class));
            startActivityForResult(Intent(this@CommunityChat, NewPost::class.java), 1010)
        }
        recyclerView = findViewById(R.id.rv4)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView!!.setLayoutManager(linearLayoutManager)
        recyclerView!!.setHasFixedSize(true)
        //recyclerView.setItemViewCacheSize(50);
        val itemViewType = 0
        recyclerView!!.getRecycledViewPool().setMaxRecycledViews(itemViewType, 0)
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val job = GlobalScope.async {
            return@async Utility.isInternetAvailable()
        }
        job.invokeOnCompletion {
            val isInternet = job.getCompleted()
            if (isInternet) {
                reload()

            } else {
                stopAnim()
                customSnackBar(recyclerView!!, this@CommunityChat, "No Internet Available.", ContextCompat.getColor(this@CommunityChat, R.color.error), R.drawable.ic_error)
            }
        }
    }

    override fun onResume() {
        super.onResume()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && requestCode == 1010 && data.getBooleanExtra("reload", false)) {
            customSnackBar(recyclerView!!, this@CommunityChat, "Successfully Submitted", ContextCompat.getColor(this@CommunityChat, R.color.success), R.drawable.ic_success)
            val job = GlobalScope.async {
                return@async Utility.isInternetAvailable()
            }
            job.invokeOnCompletion {
                val isInternet = job.getCompleted()
                if (isInternet) {
                    reload()

                } else {
                    stopAnim()
                    customSnackBar(recyclerView!!, this@CommunityChat, "No Internet Available.", ContextCompat.getColor(this@CommunityChat, R.color.error), R.drawable.ic_error)
                }
            }           // // Toast.makeText(this, "runnind code", // Toast.LENGTH_SHORT).show();
        }
    }

    private fun reload() {
        GlobalScope.launch(Dispatchers.Main) {
            val url = Url.baseurl + "/ctalk_data"
            val json = JSONObject()
            startAnim()
            val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, json, Response.Listener { response -> //stopAnim();
                Log.d("ashok_chat", response.toString())
                try {
                    val gson = Gson()
                    val (data, _, _, success) = gson.fromJson(response.toString(), ChatDataResponseModel::class.java)
                    if (success) {
                        /* JSONArray data = response.getJSONArray("data");
                              chatData = new ArrayList<>();
                              for (int i = 0; i < data.length(); i++) {
                                  JSONObject jsonObject = data.getJSONObject(i);

                                  ChatData c = gson.fromJson(jsonObject.toString(), ChatData.class);
                                  chatData.add(c);
                              }*/
                        val communityChatAdapter = CommunityChatAdapter(this@CommunityChat, data, "/ctalk_like_unlike_post", "/ctalk_comments", "/ctalk_comments_post", false, avi)
                        recyclerView!!.adapter = communityChatAdapter
                    } else {
                        // if (Datas.DEBUG)
                        // Toast.makeText(CommunityChat.this, "" + response.getString("message"), // Toast.LENGTH_SHORT).show();
                        //login page
                        val preferences = PreferenceManager.getDefaultSharedPreferences(this@CommunityChat)
                        val editor = preferences.edit()
                        editor.clear()
                        editor.apply()
                        startActivity(Intent(this@CommunityChat, LoginActivity::class.java))
                    finish()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                stopAnim()
            }, Response.ErrorListener { error ->
                stopAnim()
                if (Datas.DEBUG) // Toast.makeText(CommunityChat.this, "" + error.toString(), // Toast.LENGTH_SHORT).show();
                    customSnackBar(recyclerView!!, this@CommunityChat, error.toString(), ContextCompat.getColor(this@CommunityChat, R.color.error), R.drawable.ic_error)
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
            MySingleton.getInstance(this@CommunityChat).addToRequestQueue(jsonObjectRequest)
        }

    }

    fun back(view: View?) {
        finish()
    }
}